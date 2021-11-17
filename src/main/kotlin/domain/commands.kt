package domain

import storage.GameState
import Session
import domain.pieces.*


/**
 * Representation of command
 */
fun interface Command {

    /**
     * Executes this command passing it the given parameter
     * @param parameter the commands parameter, or null, if no parameter has been passed
     */
    fun execute(parameter: String?): Result<Session>

    /**
     * Overload of invoke operator, for convenience.
     */
    operator fun invoke(parameter: String? = null) = execute(parameter)
}


/**
 * Opens a new game with a new name unless a game with that name already exists.
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game name not specified
 */
class OpenCommand(private val db: GameState) : Command {

    override fun execute(parameter: String?): Result<Session> {
        requireNotNull(parameter) { "Missing game name." }

        val moves =
            if (db.getGame(parameter) != null) db.getAllMoves(parameter)
            else {
                db.createGame(parameter)
                emptyList()
            }

        return Result.success(
            Session(
                name = parameter,
                state = if (isWhiteTurn(moves)) SessionState.YOUR_TURN else SessionState.WAITING_FOR_OPPONENT,
                army = Army.WHITE,
                board = boardWithMoves(moves),
                moves = moves
            )
        )
    }
}


/**
 * Used by a second user to open the game with the name.
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game name not specified
 * @throws IllegalArgumentException if game name not recognized
 */
class JoinCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        requireNotNull(parameter) { "Missing game name." }
        requireNotNull(db.getGame(parameter)) { "Unknown game." }

        val moves = db.getAllMoves(parameter)

        return Result.success(
            chess.copy(
                name = db.getGame(parameter),
                state = if (isWhiteTurn(moves)) SessionState.WAITING_FOR_OPPONENT else SessionState.YOUR_TURN,
                army = Army.BLACK,
                board = boardWithMoves(moves),
                moves = moves
            )
        )
    }
}


/**
 * Executes a move command if it corresponds to the rules
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game has not been initialized yet
 * @throws IllegalArgumentException if it is not the user's turn
 * @throws IllegalArgumentException if move to be made not specified
 */
class PlayCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(chess.state != SessionState.LOGGING) { "Can't play without a game: try open or join commands." }
        require(chess.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        requireNotNull(parameter) { "Missing move." }

        val move = Move(parameter, chess.board!!)
        val newBoard = chess.board.makeMove(parameter)
        db.postMove(chess.name!!, move)

        return Result.success(
            chess.copy(
                state = SessionState.WAITING_FOR_OPPONENT,
                board = newBoard,
                moves = chess.moves + move
            )
        )
    }
}


/**
 * Updates the state of the game in MongoDB
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game has not been opened yet
 */
class RefreshCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(chess.state != SessionState.LOGGING) { "Can't refresh without a game: try open or join commands." }

        val moves = db.getAllMoves(chess.name!!)

        return Result.success(
            chess.copy(
                state = if (currentTurnArmy(moves) == chess.army) SessionState.YOUR_TURN else SessionState.WAITING_FOR_OPPONENT,
                board = boardWithMoves(moves),
                moves = moves
            )
        )
    }
}


/**
 * Gets all moves made since the beginning of the game
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game has not been opened yet
 */
class MovesCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(chess.state != SessionState.LOGGING) { "No game, no moves." }

        return Result.success(chess.copy(moves = db.getAllMoves(chess.name!!)))
    }
}


/**
 * Terminates the application by saving the actual state of the game
 */
class ExitCommand : Command {

    override fun execute(parameter: String?): Result<Session> {
        return Result.failure(Throwable("Exiting Game."))
    }
}


/**
 * Prints all the commands of the application.
 */
class HelpCommand(private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        return Result.success(chess)
    }
}


/**
 * Returns the army playing in the current turn
 * @param moves all game moves
 */
private fun currentTurnArmy(moves: List<Move>) = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Returns true if it's the white army turn
 * @param moves all game moves
 */
private fun isWhiteTurn(moves: List<Move>) = currentTurnArmy(moves) == Army.WHITE


/**
 * Returns a new board with all the moves in [moves]
 * @param moves moves to make
 * @return board with moves
 */
private fun boardWithMoves(moves: List<Move>): Board {
    var newBoard = Board()
    moves.forEach { move -> newBoard = newBoard.makeMove(move.toString()) }
    return newBoard
}

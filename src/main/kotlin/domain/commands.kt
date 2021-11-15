package domain

import storage.GameState
import Session
import domain.pieces.*


/**
 * Representation of command
 */
typealias Command = (Session, String?, GameState) -> Result<Session>
// TODO(Aks: is there a way to create a function typealias with optional parameters?)

/**
 * Opens a new game with a new name unless a game with that name already exists.
 * @param parameter new game name
 * @param db database where the moves are stored
 */
fun open(chess: Session, parameter: String?, db: GameState): Result<Session> {
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
        ))
}


/**
 * Used by a second user to open the game with the name.
 * @param parameter the game´s name
 * @param db database where the moves are stored
 */
fun join(chess: Session, parameter: String?, db: GameState): Result<Session> {
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


/**
 * Executes a move command if it corresponds to the rules
 * @param parameter the play to be made
 * @param db database where the moves are stored
 */
fun play(chess: Session, parameter: String?, db: GameState): Result<Session> {
    require(chess.state != SessionState.LOGGING) { "Can't play without a game: try open or join commands." }
    require(chess.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
    requireNotNull(parameter) { "Missing move." }

    val move = Move(parameter)
    val newBoard = chess.board?.makeMove(parameter)
    db.postMove(chess.name!!, move)

    return Result.success(
        chess.copy(
            state = SessionState.WAITING_FOR_OPPONENT,
            board = newBoard,
            moves = chess.moves + move
        )
    )
}


/**
 * Updates the state of the game in MongoDB
 * @param db database where the moves are stored
 */
fun refresh(chess: Session, parameter: String?, db: GameState): Result<Session> {
    require(chess.state != SessionState.LOGGING) { "Can't refresh without a game: try open or join commands." }

    val moves = db.getAllMoves(chess.name!!)

    return Result.success(
        chess.copy(
            state = if (turnArmy(moves) == chess.army) SessionState.YOUR_TURN else SessionState.WAITING_FOR_OPPONENT,
            board = boardWithMoves(moves),
            moves = moves
        )
    )
}


/**
 * Gets all moves made since the beginning of the game
 * @param db database where the moves are stored
 */
fun moves(chess: Session, parameter: String?, db: GameState): Result<Session> {
    require(chess.state != SessionState.LOGGING) { "No game, no moves." }

    return Result.success(chess.copy(moves = db.getAllMoves(chess.name!!)))
}


/**
 * Terminates the application by saving the actual state of the game
 */
fun exit(chess: Session, parameter: String?, db: GameState): Result<Session> {
    return Result.failure(Throwable("Exiting Game."))
}


/**
 * Prints all the commands of the application.
 */
fun help(chess: Session, parameter: String?, db: GameState): Result<Session> {
    return Result.success(chess)
}


/**
 * Returns the army playing in the current turn
 * @param moves all game moves
 */
private fun turnArmy(moves: List<Move>) = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Returns true if it's the white army turn
 * @param moves all game moves
 */
private fun isWhiteTurn(moves: List<Move>) = turnArmy(moves) == Army.WHITE


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

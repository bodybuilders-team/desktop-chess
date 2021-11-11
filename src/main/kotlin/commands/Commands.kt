package commands

import Board
import game_state.GameState
import Move
import Session
import pieces.Color


/**
 * Representation of command
 */
typealias Command = (Session, String?, GameState) -> Result<Session>


/**
 * Returns a map of commands
 * @return map of commands
 */
fun buildCommands(): Map<String, Command> {
    return mapOf(
        "open" to ::open,
        "join" to ::join,
        "play" to ::play,
        "refresh" to ::refresh,
        "moves" to ::moves,
        "exit" to ::exit
    )
}


/**
 * Opens a new game with a new name unless a game with that name already exists.
 * @param parameter new game name
 * @param db database where the moves are stored
 */
private fun open(chess: Session, parameter: String?, db: GameState): Result<Session> {
    requireNotNull(parameter) { "Missing game name." }

    var newSession = chess.copy(name = parameter, state = SessionState.PLAYING, army = Color.WHITE, board = Board())

    if (db.getGame(parameter) == null)
        db.createGame(parameter)
    else {
        val moves = db.getAllMoves(parameter).toList()
        var newBoard = Board()
        moves.forEach { move -> newBoard = newBoard.makeMove(move.toString()) }
        newSession = newSession.copy(board = newBoard, moves = moves)
    }

    return Result.success(newSession)
}


/**
 * Used by a second user to open the game with the name.
 * @param parameter the game´s name
 * @param db database where the moves are stored
 */
private fun join(chess: Session, parameter: String?, db: GameState): Result<Session> {
    requireNotNull(parameter) { "Missing game name." }
    requireNotNull(db.getGame(parameter)) { "Unknown game." }

    val moves = db.getAllMoves(parameter).toList()
    var newBoard = Board()
    moves.forEach { move -> newBoard = newBoard.makeMove(move.toString()) }

    return Result.success(chess.copy(
        name = db.getGame(parameter),
        state = if (moves.count() % 2 == 0) SessionState.WAITING_FOR_OPPONENT else SessionState.PLAYING,
        army = Color.BLACK,
        board = newBoard,
        moves = moves
    ))
}


/**
 * Executes a move command if it corresponds to the rules
 * @param parameter the play to be made
 * @param db database where the moves are stored
 */
private fun play(chess: Session, parameter: String?, db: GameState): Result<Session> {
    require(chess.state != SessionState.LOGGING) { "Can't play without a game: try open or join commands." }
    require(chess.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
    requireNotNull(parameter) { "Missing move." }

    val move = Move(parameter)
    db.postMove(chess.name!!, move)

    return Result.success(
        chess.copy(
            state = SessionState.WAITING_FOR_OPPONENT,
            board = chess.board?.makeMove(parameter),
            army = chess.army?.other(),
            moves = chess.moves + move
        )
    )
}


/**
 * Updates the state of the game in MongoDB
 * @param db database where the moves are stored
 */
private fun refresh(chess: Session, parameter: String?, db: GameState): Result<Session> {
    require(chess.state != SessionState.LOGGING) { "Can't refresh without a game: try open or join commands." }

    val moves = db.getAllMoves(chess.name!!)
    var newBoard = Board()
    moves.forEach { move -> newBoard = newBoard.makeMove(move.toString()) }

    return Result.success(chess.copy(
        name = db.getGame(chess.name),
        state = if (moves.count() % 2 == 0 && chess.army == Color.BLACK) SessionState.WAITING_FOR_OPPONENT else SessionState.PLAYING,
        army = chess.army,
        board = newBoard,
        moves = moves.toList()
    ))
}


/**
 * Gets all moves made since the beginning of the game
 * @param db database where the moves are stored
 */
private fun moves(chess: Session, parameter: String?, db: GameState): Result<Session> {
    require(chess.state != SessionState.LOGGING) { "No game, no moves." }

    val moves = db.getAllMoves(chess.name!!)

    return Result.success(chess.copy(
        moves = moves.toList()
    ))
}


/**
 * Terminates the application by saving the actual state of the game
 */
private fun exit(chess: Session, parameter: String?, db: GameState): Result<Session> {
    return Result.failure(Throwable("Exiting Game."))
}

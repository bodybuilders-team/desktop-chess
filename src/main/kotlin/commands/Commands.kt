package commands

import Board
import Session
import pieces.Color


/**
 * Representation of command
 */
typealias Command = (Session, String?) -> Result<Session>


/**
 * Returns a map of commands
 * @return map of commands
 */
fun buildCommands(): Map<String, Command> {
    return mapOf(
        "open"    to ::open,
        "join"    to ::join,
        "play"    to ::play,
        "refresh" to ::refresh,
        "moves"   to ::moves,
        "exit"    to ::exit
    )
}


/**
 * Opens a new game with a new name unless a game with that name already exists
 */
private fun open(chess: Session, parameter: String?): Result<Session> {
    requireNotNull(parameter) { "Missing game name." }

    return Result.success(Session(name = parameter, state = GameState.PLAYING, army = Color.WHITE, board = Board()))
}


/**
 * Used by a second user to open the game with the name
 * @param parameter the game´s name
 */
private fun join(chess: Session, parameter: String?): Result<Session> {
    requireNotNull(parameter) { "Missing game name." }

    /*if (parameter.isUnknown()) {
        println("Unknown game.")
        return
    }*/
    return Result.success(chess)
}


/**
 * Executes a move command if it corresponds to the rules
 * @param parameter the play to be made
 */
private fun play(chess: Session, parameter: String?): Result<Session> {
    require(chess.state != GameState.LOGGING) { "Can't play without a game: try open or join commands." }
    require(chess.state != GameState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
    requireNotNull(parameter) { "Missing move." }

    return Result.success(chess.copy(
        state = GameState.WAITING_FOR_OPPONENT,
        board = chess.board?.makeMove(parameter),
        army = chess.army?.other()
    ))
}


/**
 * Updates the state of the game in MongoDB
 */
private fun refresh(chess: Session, parameter: String?): Result<Session> {
    require(chess.state != GameState.LOGGING) { "Can't refresh without a game: try open or join commands." }

    return Result.success(chess)
}


/**
 * Lists all moves made since the beginning of the game
 */
private fun moves(chess: Session, parameter: String?): Result<Session> {
    require(chess.state != GameState.LOGGING) { "No game, no moves." }

    // TODO(Get all plays made)

    return Result.success(chess)
}


/**
 * Terminates the application by saving the actual state of the game
 */
private fun exit(chess: Session, parameter: String?): Result<Session> {
    return Result.failure(Throwable("Exiting Game."))
}

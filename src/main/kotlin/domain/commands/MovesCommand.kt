package domain.commands

import Session
import storage.GameState


/**
 * Gets all moves made since the beginning of the game
 * @param chess current chess game
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game has not been opened yet
 */
class MovesCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(chess.state != SessionState.LOGGING) { "No game, no moves." }

        return Result.success(chess.copy(moves = db.getAllMoves(chess.name!!)))
    }
}

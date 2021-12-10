package domain.commands

import domain.*
import storage.GameState


/**
 * Gets all moves made since the beginning of the game
 * @param session current session
 * @param db database where the moves are stored
 * @throws CommandException if game has not been opened yet
 */
class MovesCommand(private val db: GameState, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(!session.isLogging()) { "No game, no moves." }

        return Result.success(session)
    }
}

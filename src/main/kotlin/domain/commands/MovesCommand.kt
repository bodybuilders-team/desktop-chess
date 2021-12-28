package domain.commands

import domain.*
import storage.GameStorage


/**
 * Gets all moves made since the beginning of the game
 * @param session current session
 * @param db database where the moves are stored
 * @throws CommandException if game has not been opened yet
 */
class MovesCommand(private val db: GameStorage, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(session.isNotLogging()) { "No game, no moves: try open or join commands." }

        return Result.success(session)
    }
}

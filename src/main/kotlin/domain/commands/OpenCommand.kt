package domain.commands

import domain.*
import domain.pieces.Army
import storage.GameStorage


/**
 * Opens a game with the received name or creates a new game if it doesn't exist
 * @param db database where the moves are stored
 * @throws CommandException if game name not specified
 */
class OpenCommand(private val db: GameStorage) : Command {

    override suspend fun execute(parameter: String?): Result<Session> {
        cmdRequireNotNull(parameter) { "Missing game name." }

        val moves =
            if (db.gameExists(parameter)) db.getAllMoves(parameter)
            else {
                db.createGame(parameter)
                emptyList()
            }

        return Result.success(getOpeningBoardSession(gameName = parameter, moves, Army.WHITE))
    }
}

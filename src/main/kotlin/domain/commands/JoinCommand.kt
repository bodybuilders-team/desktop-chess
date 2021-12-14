package domain.commands

import domain.*
import domain.pieces.Army
import storage.GameState


/**
 * Used by a second user to open the game with the received name.
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game name not specified
 * @throws IllegalArgumentException if game name not recognized
 */
class JoinCommand(private val db: GameState) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequireNotNull(parameter) { "Missing game name." }
        cmdRequire(db.gameExists(parameter)) { "A game with the name \"$parameter\" does not exist: try open command." }

        val moves = db.getAllMoves(parameter)
        
        return Result.success(getOpeningBoardSession(gameName = parameter, moves, Army.BLACK))
    }
}

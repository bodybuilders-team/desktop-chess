package domain.commands

import domain.*
import domain.board.*
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
        val game = gameFromMoves(*moves.map { it.toString() }.toTypedArray())
        val state = getCurrentState(game.board, moves, Army.BLACK)

        return Result.success(
            Session(
                name = parameter,
                state = state,
                army = Army.BLACK,
                game = game,
                currentCheck =
                if (state == SessionState.YOUR_TURN && game.board.isKingInCheck(Army.BLACK)) Check.CHECK
                else Check.NO_CHECK
            )
        )
    }
}

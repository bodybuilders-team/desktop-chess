package domain.commands

import domain.*
import domain.board.*
import domain.pieces.Army
import storage.GameState


/**
 * Opens a game with the received name or creates a new game if it doesn't exist
 * @param db database where the moves are stored
 * @throws CommandException if game name not specified
 */
class OpenCommand(private val db: GameState) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequireNotNull(parameter) { "Missing game name." }

        val moves =
            if (db.gameExists(parameter)) db.getAllMoves(parameter)
            else {
                db.createGame(parameter)
                emptyList()
            }

        val game = gameFromMoves(*moves.map { it.toString() }.toTypedArray())
        val state = getCurrentState(game.board, moves, Army.WHITE)

        return Result.success(
            Session(
                name = parameter,
                state = state,
                army = Army.WHITE,
                game = game,
                currentCheck =
                    if (state == SessionState.YOUR_TURN && game.board.isKingInCheck(Army.WHITE)) Check.CHECK
                    else Check.NO_CHECK
            )
        )
    }
}

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
        cmdRequire(db.gameExists(parameter)) { "Unknown game." }

        val moves = db.getAllMoves(parameter)
        val board = boardWithMoves(moves)
        val state = getCurrentState(board, moves, Army.BLACK)

        return Result.success(
            Session(
                name = parameter,
                state = state,
                army = Army.BLACK,
                board = board,
                moves = moves,
                currentCheck =
                if (state == SessionState.YOUR_TURN && board.isKingInCheck(Army.BLACK)) Check.CHECK
                else Check.NO_CHECK
            )
        )
    }
}

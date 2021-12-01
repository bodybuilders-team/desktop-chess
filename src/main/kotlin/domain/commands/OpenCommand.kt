package domain.commands

import domain.*
import domain.pieces.Army
import storage.GameState


/**
 * Opens a game with the received name or creates a new game if it doesn't exist
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game name not specified
 */
class OpenCommand(private val db: GameState) : Command {

    override fun execute(parameter: String?): Result<Session> {
        requireNotNull(parameter) { "Missing game name." }

        val moves =
            if (db.gameExists(parameter)) db.getAllMoves(parameter)
            else {
                db.createGame(parameter)
                emptyList()
            }

        val board = boardWithMoves(moves)

        val inMate = board.isKingInMate(Army.WHITE) || board.isKingInMate(Army.BLACK)

        val state = when {
            inMate -> SessionState.ENDED
            currentTurnArmy(moves) == Army.WHITE -> SessionState.YOUR_TURN
            else -> SessionState.WAITING_FOR_OPPONENT
        }

        return Result.success(
            Session(
                name = parameter,
                state = state,
                army = Army.WHITE,
                board = board,
                moves = moves,
                currentCheck =
                if (state == SessionState.YOUR_TURN && board.isKingInCheck(Army.WHITE)) Check.CHECK
                else Check.NO_CHECK
            )
        )
    }
}

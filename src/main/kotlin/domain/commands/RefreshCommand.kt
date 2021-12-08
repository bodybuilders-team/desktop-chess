package domain.commands

import domain.*
import domain.board.*
import domain.pieces.Army
import storage.GameState


/**
 * Updates the state of the game.
 * @param chess current chess game
 * @param db database where the moves are stored
 * @throws CommandException if game has not been initialized yet
 * @throws CommandException if it is not the user's turn
 * @throws CommandException if the game ended
 */
class RefreshCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(!chess.isLogging()) { "Can't refresh without a game: try open or join commands." }
        cmdRequire(chess.state != SessionState.YOUR_TURN) { "It's your turn: try play." }
        cmdRequire(chess.state != SessionState.ENDED) { "Game ended. There aren't any new moves." }

        val moves = db.getAllMoves(chess.name)
        val board = boardWithMoves(moves)

        val inMate = board.isKingInCheckMate(chess.army) || board.isKingInStaleMate(chess.army)
        
        val state = when {
            inMate -> SessionState.ENDED
            currentTurnArmy(moves) == chess.army -> SessionState.YOUR_TURN
            else -> SessionState.WAITING_FOR_OPPONENT
        }

        return Result.success(
            chess.copy(
                state = state,
                board = board,
                moves = moves,
                currentCheck =
                if (state == SessionState.YOUR_TURN && board.isKingInCheck(chess.army)) Check.CHECK
                else Check.NO_CHECK
            )
        )
    }
}

package domain.commands

import domain.*
import domain.board.*
import domain.move.*
import storage.GameState


/**
 * Updates the state of the game.
 * @param session current chess game
 * @param db database where the moves are stored
 * @throws CommandException if game has not been initialized yet
 * @throws CommandException if it is not the user's turn
 * @throws CommandException if the game ended
 */
class RefreshCommand(private val db: GameState, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(!session.isLogging()) { "Can't refresh without a game: try open or join commands." }
        cmdRequire(session.state != SessionState.YOUR_TURN) { "It's your turn: try play." }
        cmdRequire(session.state != SessionState.ENDED) { "Game ended. There aren't any new moves." }

        val moves = db.getAllMoves(session.name)
        val board = boardWithMoves(moves)

        val inMate = board.isKingInCheckMate(session.army) || board.isKingInStaleMate(session.army, moves)
        
        val state = when {
            inMate                               -> SessionState.ENDED
            currentTurnArmy(moves) == session.army -> SessionState.YOUR_TURN
            else                                 -> SessionState.WAITING_FOR_OPPONENT
        }

        return Result.success(
            session.copy(
                state = state,
                game =  Game(board, moves),
                currentCheck =
                if (state == SessionState.YOUR_TURN && board.isKingInCheck(session.army)) Check.CHECK
                else Check.NO_CHECK
            )
        )
    }
}

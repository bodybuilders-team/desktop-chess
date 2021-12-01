package domain.commands

import domain.*
import storage.GameState


/**
 * Updates the state of the game.
 * @param chess current chess game
 * @param db database where the moves are stored
 * @throws IllegalArgumentException if game has not been opened yet
 */
class RefreshCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(!chess.isLogging()) { "Can't refresh without a game: try open or join commands." }
        require(chess.state != SessionState.YOUR_TURN) { "It's your turn: try play." }
        require(chess.state != SessionState.ENDED) { "Game ended. There aren't any new moves." }

        val moves = db.getAllMoves(chess.name)
        val board = boardWithMoves(moves)

        val inMate = board.isKingInMate(chess.army)
        
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

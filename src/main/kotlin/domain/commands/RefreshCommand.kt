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
        require(chess.state != SessionState.YOUR_TURN) { "It's your turn: try play." }
        require(!chess.isLogging()) { "Can't refresh without a game: try open or join commands." }

        val moves = db.getAllMoves(chess.name)

        return Result.success(
            chess.copy(
                state = if (currentTurnArmy(moves) == chess.army) SessionState.YOUR_TURN else SessionState.WAITING_FOR_OPPONENT,
                board = boardWithMoves(moves),
                moves = moves
            )
        )
    }
}

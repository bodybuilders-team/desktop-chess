package domain.commands

import Session
import domain.pieces.Army
import storage.GameState


/**
 * Used by a second user to open the game with the name.
 * @param db database where the moves are stored
 * @param chess current chess game
 * @throws IllegalArgumentException if game name not specified
 * @throws IllegalArgumentException if game name not recognized
 */
class JoinCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        requireNotNull(parameter) { "Missing game name." }
        requireNotNull(db.getGame(parameter)) { "Unknown game." }

        val moves = db.getAllMoves(parameter)

        return Result.success(
            chess.copy(
                name = db.getGame(parameter),
                state = if (isWhiteTurn(moves)) SessionState.WAITING_FOR_OPPONENT else SessionState.YOUR_TURN,
                army = Army.BLACK,
                board = boardWithMoves(moves),
                moves = moves
            )
        )
    }
}

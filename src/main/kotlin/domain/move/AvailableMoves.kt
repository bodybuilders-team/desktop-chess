package domain.move

import domain.Game
import domain.board.*
import domain.pieces.Army
import domain.board.Board.Position


/**
 * Gets a given position's available moves.
 * @param position position to get the available moves
 * @return list of available moves
 */
fun Game.getAvailableMoves(position: Position) =
    board.getPiece(position)?.getAvailableMoves(this, position) ?: emptyList()


/**
 * Checks if a given army has any available valid moves to make.
 * @param army army to make the move
 * @return true if the army has available moves
 */
fun Game.hasAvailableMoves(army: Army): Boolean {
    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val pos = Position(col, row)

            val piece = board.getPiece(pos) ?: continue
            if (piece.army != army) continue

            if (getAvailableMoves(pos).isNotEmpty()) return true
        }
    }

    return false
}

/**
 * Returns the army playing in the current turn
 * @param moves all game moves
 * @return the army playing in the current turn
 */
fun currentTurnArmy(moves: List<Move>) = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Returns true if it's the white army turn
 * @param moves all game moves
 * @return true if it's the white army turn
 */
fun isWhiteTurn(moves: List<Move>) = currentTurnArmy(moves) == Army.WHITE

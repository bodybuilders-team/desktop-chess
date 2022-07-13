package domain.game

import domain.board.Board.Position
import domain.board.COLS_RANGE
import domain.board.ROWS_RANGE
import domain.pieces.Army

/**
 * Gets a given position's available moves in the game.
 * @param position position to get the available moves
 * @return list of available moves from a position in the game
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

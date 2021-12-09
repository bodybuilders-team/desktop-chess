package domain.move

import domain.board.*
import domain.pieces.Army
import domain.board.Board.Position


//TODO("Test")
/**
 * Gets a given position's available moves.
 * @param position position to get the available moves
 * @param previousMoves previous moves made
 * @return list of available moves
 */
fun Board.getAvailableMoves(position: Position, previousMoves: List<Move>) =
    getPiece(position)?.getAvailableMoves(this, position, previousMoves) ?: emptyList()


//TODO("Test")
/**
 * Checks if a given army has any available valid moves to make.
 * @param army army to make the move
 * @param previousMoves previous moves made
 * @return true if the army has available moves
 */
fun Board.hasAvailableMoves(army: Army, previousMoves: List<Move>): Boolean {
    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val pos = Position(col, row)

            val piece = getPiece(pos) ?: continue
            if (piece.army != army) continue

            if (getAvailableMoves(pos, previousMoves).isNotEmpty()) return true
        }
    }

    return false
}

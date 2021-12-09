package domain.pieces

import domain.board.*
import domain.move.*


/**
 * Rook piece with [type] 'R'.
 *
 * Moves horizontally or vertically
 * @param army piece army
 */
class Rook(override val army: Army) : Piece {

    override val type = PieceType.ROOK

    override fun isValidMove(board: Board, move: Move) = isValidStraightMove(board, move)
}

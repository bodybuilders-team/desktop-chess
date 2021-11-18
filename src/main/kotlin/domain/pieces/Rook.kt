package domain.pieces

import domain.*


/**
 * Rook piece with [type] 'R'.
 *
 * Moves horizontally or vertically
 * @param army piece army
 */
class Rook(override val army: Army) : Piece {

    override val type = PieceType.ROOK

    override fun isValidMove(board: Board, move: Move): Boolean {
        return isValidStraightMove(board, move)
    }
}

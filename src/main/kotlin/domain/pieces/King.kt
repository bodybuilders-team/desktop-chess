package domain.pieces

import domain.*


/**
 * King piece with [type] 'K'.
 *
 * Moves one and only one position in all directions.
 * @param army piece army
 */
class King(override val army: Army) : Piece {

    override val type = PieceType.KING

    override fun isValidMove(board: Board, move: Move): Boolean {
        return (move.rowsAbsoluteDistance() in NO_MOVE..ONE_MOVE)
                && (move.colsAbsoluteDistance() in NO_MOVE..ONE_MOVE)
                && !(move.colsAbsoluteDistance() == NO_MOVE && move.rowsAbsoluteDistance() == NO_MOVE)
    }
}

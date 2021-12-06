package domain.pieces

import domain.board.Board
import domain.move.Move


/**
 * Knight piece with [type] 'N'.
 *
 * Moves only one slot horizontally/vertically and then two slots vertically/horizontally
 * @param army piece army
 */
class Knight(override val army: Army) : Piece {

    override val type = PieceType.KNIGHT

    override fun isValidMove(board: Board, move: Move): Boolean {
        return move.rowsAbsoluteDistance() == ONE_MOVE && move.colsAbsoluteDistance() == DOUBLE_MOVE ||
                move.colsAbsoluteDistance() == ONE_MOVE && move.rowsAbsoluteDistance() == DOUBLE_MOVE
    }
}

package domain.pieces

import domain.board.Board
import domain.move.Move
import domain.move.isValidDiagonalMove
import domain.move.isValidStraightMove


/**
 * Queen piece with [type] 'Q'.
 *
 * Moves horizontally, vertically or diagonally
 * @param army piece army
 */
class Queen(override val army: Army) : Piece {

    override val type = PieceType.QUEEN

    override fun isValidMove(board: Board, move: Move): Boolean {
        return isValidStraightMove(board, move) || isValidDiagonalMove(board, move)
    }
}

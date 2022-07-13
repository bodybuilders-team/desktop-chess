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
data class Queen(override val army: Army) : Piece {

    override val type = PieceType.QUEEN

    override fun toString() = "$army $type"

    override fun isValidMove(board: Board, move: Move) =
        isValidStraightMove(board, move) || isValidDiagonalMove(board, move)
}

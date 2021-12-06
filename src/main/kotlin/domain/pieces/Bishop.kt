package domain.pieces

import domain.*
import domain.board.Board
import domain.move.Move


/**
 * Bishop piece with [type] 'B'.
 *
 * Moves diagonally.
 * @param army piece army
 */
class Bishop(override val army: Army) : Piece {

    override val type = PieceType.BISHOP

    override fun isValidMove(board: Board, move: Move): Boolean {
        return isValidDiagonalMove(board, move)
    }
}

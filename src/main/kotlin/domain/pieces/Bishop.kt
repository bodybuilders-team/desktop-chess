package domain.pieces

import domain.board.Board
import domain.move.*


/**
 * Bishop piece with [type] 'B'.
 *
 * Moves diagonally.
 * @param army piece army
 */
data class Bishop(override val army: Army) : Piece {

    override val type = PieceType.BISHOP

    override fun toString() = "$army $type"

    override fun isValidMove(board: Board, move: Move) = isValidDiagonalMove(board, move)
}

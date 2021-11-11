package pieces

import Board
import Move


class Rook(override val army: Army) : Piece {

    override val symbol = 'R'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Rook moves horizontally or vertically
        return (move.isHorizontal() || move.isVertical()) && !isNonDiagonalOccupied(board, move)
    }
}

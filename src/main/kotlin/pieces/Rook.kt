package pieces

import Board
import Move


class Rook(override val color: Color) : Piece {

    override val symbol = 'R'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Queen moves horizontally, vertically or diagonally
        return (move.isHorizontal() || move.isVertical()) && !isNonDiagonalOccupied(board, move)
    }
}

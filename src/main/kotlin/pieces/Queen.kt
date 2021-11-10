package pieces

import Board
import Move


class Queen(override val color: Color) : Piece {

    override val symbol = 'Q'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Queen moves horizontally, vertically or diagonally
        if (!move.isHorizontal() && !move.isVertical() && !move.isDiagonal()) return false
        if ((move.isHorizontal() || move.isVertical()) && isNonDiagonalOccupied(board, move)) return false
        if (move.isDiagonal() && isDiagonalOccupied(board, move)) return false
        return true
    }
}

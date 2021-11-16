package domain.pieces

import domain.Board
import domain.Move


class Queen(override val army: Army) : Piece {

    override val symbol = 'Q'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Queen moves horizontally, vertically or diagonally
        if (!move.isHorizontal() && !move.isVertical() && !move.isDiagonal()) return false
        if ((move.isHorizontal() || move.isVertical()) && isStraightPathOccupied(board, move)) return false
        if (move.isDiagonal() && isDiagonalPathOccupied(board, move)) return false
        return true
    }
}

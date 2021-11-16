package domain.pieces

import domain.Board
import domain.Move


class Rook(override val army: Army) : Piece {

    override val symbol = 'R'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Rook moves horizontally or vertically
        return (move.isHorizontal() || move.isVertical()) && !isStraightPathOccupied(board, move)
    }
}

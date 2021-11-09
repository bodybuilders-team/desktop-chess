package pieces

import Board
import Move

class Queen(override val color: Color) : Piece {

    override val symbol = 'Q'

    companion object {
        fun checkMove(board: Board, move: Move): Boolean {
            // Queen moves horizontally, vertically or diagonally
            if (!move.isHorizontal() && !move.isVertical() && !move.isDiagonal()) return false
            if ((move.isHorizontal() || move.isVertical()) && checkPiecesInBetweenNonDiagonal(board, move)) return false
            if (move.isDiagonal() && checkPiecesInBetweenDiagonal(board, move)) return false
            return true
        }
    }
}

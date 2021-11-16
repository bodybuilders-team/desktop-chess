package domain.pieces

import domain.Board
import domain.Move


class Bishop(override val army: Army) : Piece {

    override val symbol = 'B'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Bishop moves diagonally
        return move.isDiagonal() && !isDiagonalPathOccupied(board, move)
    }
}

package pieces

import Board
import Move


class Bishop(override val army: Color) : Piece {

    override val symbol = 'B'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Bishop moves diagonally
        return move.isDiagonal() && !isDiagonalOccupied(board, move)
    }
}

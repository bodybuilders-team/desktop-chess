package pieces

import Board
import Move


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    companion object {
        fun checkMove(board: Board, move: Move): Boolean {
            // Bishop moves diagonally
            return move.isDiagonal() && !checkPiecesInBetweenDiagonal(board, move)
        }
    }
}

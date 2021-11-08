package pieces

import Board
import Move


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    override fun checkMove(board: Board, move: Move): Boolean {
        return move.isDiagonal()
    }
}

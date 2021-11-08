package pieces

import Board
import Move


class Queen(override val color: Color) : Piece {

    override val symbol = 'Q'

    companion object {
        fun checkMove(board: Board, move: Move): Boolean {
            return move.isDiagonal() || move.isHorizontal() || move.isVertical()
        }
    }
}

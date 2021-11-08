package pieces

import Board
import Move


class Knight(override val color: Color) : Piece {

    override val symbol = 'N'

    override fun checkMove(board: Board, move: Move): Boolean {
        return move.rowsDistance() == ONE_MOVE && move.colsDistance() == DOUBLE_MOVE ||
                move.colsDistance() == ONE_MOVE && move.rowsDistance() == DOUBLE_MOVE
    }
}

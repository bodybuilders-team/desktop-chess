package pieces

import Board
import Move


class King(override val color: Color) : Piece {

    override val symbol = 'K'

    override fun checkMove(board: Board, move: Move): Boolean {
        return (move.rowsDistance() in NO_MOVE .. ONE_MOVE)
                && (move.colsDistance() in NO_MOVE .. ONE_MOVE)
                && !(move.colsDistance() == NO_MOVE && move.rowsDistance() == NO_MOVE)

    }
}

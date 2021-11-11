package pieces

import Board
import Move


class Knight(override val army: Army) : Piece {

    override val symbol = 'N'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // Knight moves one slot horizontally/vertically and then two slots vertically/horizontally
        return move.rowsAbsoluteDistance() == ONE_MOVE && move.colsAbsoluteDistance() == DOUBLE_MOVE ||
                move.colsAbsoluteDistance() == ONE_MOVE && move.rowsAbsoluteDistance() == DOUBLE_MOVE
    }
}

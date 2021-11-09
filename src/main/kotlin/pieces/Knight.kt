package pieces

import Move


class Knight(override val color: Color) : Piece {

    override val symbol = 'N'

    companion object {
        fun checkMove(move: Move): Boolean {
            // Knight moves one slot horizontally/vertically and then two slots vertically/horizontally
            return move.rowsAbsoluteDistance() == ONE_MOVE && move.colsAbsoluteDistance() == DOUBLE_MOVE ||
                    move.colsAbsoluteDistance() == ONE_MOVE && move.rowsAbsoluteDistance() == DOUBLE_MOVE
        }
    }
}

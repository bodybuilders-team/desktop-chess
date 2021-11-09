package pieces

import Move


class Knight(override val color: Color) : Piece {

    override val symbol = 'N'

    companion object{
        fun checkMove(move: Move): Boolean {
            return move.rowsAbsoluteDistance() == ONE_MOVE && move.colsAbsoluteDistance() == DOUBLE_MOVE ||
                    move.colsAbsoluteDistance() == ONE_MOVE && move.rowsAbsoluteDistance() == DOUBLE_MOVE
        }
    }
}

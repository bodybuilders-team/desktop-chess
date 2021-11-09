package pieces

import Move


class King(override val color: Color) : Piece {

    override val symbol = 'K'

    companion object{
        fun checkMove(move: Move): Boolean {
            return (move.rowsAbsoluteDistance() in NO_MOVE .. ONE_MOVE)
                    && (move.colsAbsoluteDistance() in NO_MOVE .. ONE_MOVE)
                    && !(move.colsAbsoluteDistance() == NO_MOVE && move.rowsAbsoluteDistance() == NO_MOVE)

        }
    }
}

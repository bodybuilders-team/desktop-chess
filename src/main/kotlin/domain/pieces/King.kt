package domain.pieces

import domain.Board
import domain.Move


class King(override val army: Army) : Piece {

    override val symbol = 'K'

    override fun isValidMove(board: Board, move: Move): Boolean {
        // King moves only one position
        return (move.rowsAbsoluteDistance() in NO_MOVE..ONE_MOVE)
                && (move.colsAbsoluteDistance() in NO_MOVE..ONE_MOVE)
                && !(move.colsAbsoluteDistance() == NO_MOVE && move.rowsAbsoluteDistance() == NO_MOVE)
    }
}

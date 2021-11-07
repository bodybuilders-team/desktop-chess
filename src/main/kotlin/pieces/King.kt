package pieces

import Board
import Move
import kotlin.math.abs


class King(override val color: Color) : Piece {

    override val symbol = 'K'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is King) return false
        return (abs(move.from.row - move.to.row) == ONE_MOVE || abs(move.from.row - move.to.row) == NO_MOVE)
                && (abs(move.from.col - move.to.col) == ONE_MOVE || abs(move.from.col - move.to.col) == NO_MOVE)
                && !(abs(move.from.col - move.to.col) == NO_MOVE && abs(move.from.row - move.to.row) == NO_MOVE)

    }
}

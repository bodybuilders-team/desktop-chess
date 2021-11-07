package pieces

import Board
import Move
import kotlin.math.abs


class Knight(override val color: Color) : Piece {

    override val symbol = 'N'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Knight) return false
        return abs(move.from.row - move.to.row) == ONE_MOVE && abs(move.from.col - move.to.col) == DOUBLE_MOVE ||
                abs(move.from.col - move.to.col) == ONE_MOVE && abs(move.from.row - move.to.row) == DOUBLE_MOVE
    }
}

package pieces

import Board
import Move


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Bishop) return false
        return move.isDiagonal()
    }
}

package pieces

import Board
import Move


class Queen(override val color: Color) : Piece {

    override val symbol = 'Q'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Queen) return false
        return move.isDiagonal() || move.isHorizontal() || move.isVertical()
    }
}

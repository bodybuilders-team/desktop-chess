package pieces

import Board
import Move
import kotlin.math.abs


class Rook(override val color: Color) : Piece {

    override val symbol = 'R'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Rook) return false

        //Move has to be horizontal or vertical
        if (!move.isHorizontal() && !move.isVertical()) return false

        var distance = (if (move.isHorizontal()) move.to.col - move.from.col else move.to.row - move.from.row) - 1

        while (abs(distance) > 0) {
            if (move.isHorizontal() && board.positionIsOccupied(move.from.copy(col = move.from.col + distance))
                || move.isVertical() && board.positionIsOccupied(move.from.copy(row = move.from.row + distance))
            )
                return false

            if (distance > 0) distance-- else distance++
        }

        return true
    }
}
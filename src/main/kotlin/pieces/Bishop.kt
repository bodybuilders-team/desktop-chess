package pieces

import Board
import Move
import kotlin.math.abs


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    companion object{
        fun checkMove(board: Board, move: Move): Boolean {
            if (!move.isDiagonal()) return false

            // Distance can be calculated with rows or cols because it's diagonal
            var distance = move.rowsDistance() + if (move.rowsDistance() > 0) -1 else 1

            while (abs(distance) > 0) {
                if (board.positionIsOccupied(move.from.copy(col = move.from.col - distance, row = move.from.row + distance)))
                    return false
    
                if (distance > 0) distance-- else distance++
            }
            return true
        }
    }
}

package pieces

import Board
import Move
import kotlin.math.abs


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    companion object{
        fun checkMove(board: Board, move: Move): Boolean {
            if (!move.isDiagonal()) return false
            if (checkPiecesInBetweenDiagonal(board,move)) return false
            return true
        }
    }
}



fun checkPiecesInBetweenDiagonal(board: Board,move: Move): Boolean {
    var rowsDistance = move.rowsDistance() + if (move.rowsDistance() > 0) -1 else 1
    var colsDistance = move.colsDistance()+ if (move.colsDistance() > 0) -1 else 1
    // Number of steps can be calculated with rows Distance or cols Distance
    var numberOfSteps = abs(move.rowsDistance()) - 1

    while (numberOfSteps > 0) {
        if (board.positionIsOccupied(move.from.copy(col = move.from.col + colsDistance, row = move.from.row + rowsDistance))) return true
        if (colsDistance > 0) colsDistance-- else colsDistance++
        if (rowsDistance > 0) rowsDistance-- else rowsDistance++
        numberOfSteps--
    }
    return false
}


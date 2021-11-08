package pieces

import Board
import Move


class Pawn(override val color: Color) : Piece {

    override val symbol = 'P'

    // Pawn -> Only vertically up or diagonally if capturing
    override fun checkMove(board: Board, move: Move): Boolean {
        // Vertical
        if (move.isVertical()) return checkMoveVertical(board, move)

        // Diagonal (only capture)
        if ((move.capture || board.positionIsOccupied(move.to)) && move.colsDistance() == ONE_MOVE)
            return move.rowsDistance() == ONE_MOVE && board.positionIsOccupied(move.to)

        return false
    }

    /**
     * Check if the vertical pawn move is possible.
     * First pawn move can be 2 or 1.
     * @param board board where the move will happen
     * @param move move to test
     */
    private fun checkMoveVertical(board: Board, move: Move): Boolean {
        val defaultMove = move.rowsDistance() == ONE_MOVE
        val isInInitialRow = move.from.row == if (isWhite()) WHITE_PAWN_INITIAL_ROW else BLACK_PAWN_INITIAL_ROW
        val doubleMove = isInInitialRow && move.rowsDistance() == DOUBLE_MOVE

        return (defaultMove || doubleMove) && !board.positionIsOccupied(move.to)
    }
}

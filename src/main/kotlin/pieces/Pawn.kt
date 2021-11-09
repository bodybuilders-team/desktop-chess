package pieces

import Board
import Move


class Pawn(override val color: Color) : Piece {

    override val symbol = 'P'

    companion object {
        fun checkMove(board: Board, move: Move, color: Color): Boolean {
            // Pawn moves one or two slots vertically or diagonally (when capturing)

            // Vertical
            if (move.isVertical()) return checkMoveVertical(board, move, color)

            // Diagonal (only capture)
            if ((move.capture || board.positionIsOccupied(move.to)) && move.colsAbsoluteDistance() == ONE_MOVE)
                return (move.rowsDistance() == if (color == Color.WHITE) ONE_MOVE else -ONE_MOVE)
                        && board.positionIsOccupied(move.to)

            return false
        }

        /**
         * Check if the vertical pawn move is possible.
         * First pawn move can be 2 or 1.
         * @param board board where the move will happen
         * @param move move to test
         */
        private fun checkMoveVertical(board: Board, move: Move, color: Color): Boolean {
            val isWhite = color == Color.WHITE

            val defaultMove = move.rowsDistance() == if (isWhite) ONE_MOVE else -ONE_MOVE
            val isInInitialRow = move.from.row == if (isWhite) WHITE_PAWN_INITIAL_ROW else BLACK_PAWN_INITIAL_ROW
            val doubleMove = isInInitialRow && move.rowsDistance() == if (isWhite) DOUBLE_MOVE else -DOUBLE_MOVE

            return (defaultMove || doubleMove) && !board.positionIsOccupied(move.to)
        }
    }
}

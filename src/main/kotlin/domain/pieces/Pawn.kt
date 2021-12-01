package domain.pieces

import domain.*


/**
 * Pawn piece with [type] 'P'.
 *
 * Moves one or two slots vertically or one diagonally (when capturing)
 * @param army piece army
 */
class Pawn(override val army: Army) : Piece {

    override val type = PieceType.PAWN

    override fun isValidMove(board: Board, move: Move) =
        move.isVertical() && isValidPawnVerticalMove(board, move) ||
                move.colsAbsoluteDistance() == ONE_MOVE && isValidPawnDiagonalMove(board, move)


    /**
     * Checks if the vertical pawn move is possible.
     * First pawn move can be 2 or 1.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the vertical move is valid
     */
    private fun isValidPawnVerticalMove(board: Board, move: Move): Boolean {
        val defaultMove = move.rowsDistance() == if (isWhite()) ONE_MOVE else -ONE_MOVE
        val isInInitialRow = move.from.row == if (isWhite()) WHITE_PAWN_INITIAL_ROW else BLACK_PAWN_INITIAL_ROW
        val doubleMove = isInInitialRow && move.rowsDistance() == if (isWhite()) DOUBLE_MOVE else -DOUBLE_MOVE

        return (defaultMove || doubleMove) && !board.isPositionOccupied(move.to)
    }


    /**
     * Checks if the diagonal pawn move is possible.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the diagonal move is valid
     */
    private fun isValidPawnDiagonalMove(board: Board, move: Move) =
        (move.rowsDistance() == if (isWhite()) ONE_MOVE else -ONE_MOVE) && board.isPositionOccupied(move.to)


    /**
     * Checks if the en passant move is valid.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the en passant move is valid
     */
    fun isValidEnPassant(board: Board, move: Move): Boolean {
        if (isValidPawnDiagonalMove(board, move) && (isWhite() && move.from.row == 5 || !isWhite() && move.from.row == 4)) {
            val capturedPiecePos = getEnPassantCapturedPawnPosition(move.to, this)
            val capturedPiece = board.getPiece(capturedPiecePos)
            return board.isPositionOccupied(capturedPiecePos) && capturedPiece is Pawn && capturedPiece.army != army
        }

        return false
    }
}

package domain.pieces

import domain.board.*
import domain.move.isStraightPathOccupied
import domain.board.Board.*
import domain.move.*


/**
 * King piece with [type] 'K'.
 *
 * Moves one and only one position in all directions.
 * @param army piece army
 */
class King(override val army: Army) : Piece {

    override val type = PieceType.KING

    override fun isValidMove(board: Board, move: Move): Boolean {
        return (move.rowsAbsoluteDistance() in NO_MOVE..ONE_MOVE)
                && (move.colsAbsoluteDistance() in NO_MOVE..ONE_MOVE)
                && !(move.colsAbsoluteDistance() == NO_MOVE && move.rowsAbsoluteDistance() == NO_MOVE)
    }


    /**
     * Checks if the castle move is valid.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the castle move is valid
     */
    fun isValidCastle(board: Board, move: Move): Boolean {
        val validKingPositionForCastle =
            Position(INITIAL_KING_COL, if (isWhite()) WHITE_FIRST_ROW else BLACK_FIRST_ROW)

        val validKingToColsForCastle = listOf(LONG_CASTLE_KING_COL, SHORT_CASTLE_KING_COL)
        
        return move.isHorizontal() && move.from == validKingPositionForCastle &&
                move.to.col in validKingToColsForCastle &&
                !isStraightPathOccupied(board, move.copy(to = Castle.getRookPosition(move.to)))
    }
}

package domain.pieces

import domain.board.*
import domain.isStraightPathOccupied
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
        return move.isHorizontal() &&
                !isStraightPathOccupied(board, move.copy(to = Castle.getRookPosition(move.to))) &&
                move.from == Position(INITIAL_KING_COL, if (isWhite()) FIRST_ROW else LAST_ROW)
    }
}

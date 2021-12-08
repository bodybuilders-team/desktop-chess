package domain.pieces

import domain.board.*
import domain.board.Board.*
import domain.move.*


/**
 * Rook piece with [type] 'R'.
 *
 * Moves horizontally or vertically
 * @param army piece army
 */
class Rook(override val army: Army) : Piece {

    override val type = PieceType.ROOK

    override fun isValidMove(board: Board, move: Move): Boolean {
        return isValidStraightMove(board, move)
    }

    /**
     * Checks if the castle move is valid.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the castle move is valid
     */
    fun isValidCastle(board: Board, move: Move): Boolean {
        val firstRow = if (isWhite()) WHITE_FIRST_ROW else BLACK_FIRST_ROW
        
        val validLongCastlePositions =
            move.from == Position(INITIAL_ROOK_COL_FURTHER_FROM_KING, firstRow) && move.to.col == LONG_CASTLE_ROOK_COL
        
        val validShortCastlePositions =
            move.from == Position(INITIAL_ROOK_COL_CLOSER_TO_KING, firstRow) && move.to.col == SHORT_CASTLE_ROOK_COL


        return move.isHorizontal() && (validLongCastlePositions || validShortCastlePositions) &&
                !isStraightPathOccupied(board, move.copy(to = Castle.getKingPosition(move.to)))
    }
}

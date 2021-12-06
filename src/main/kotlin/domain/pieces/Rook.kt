package domain.pieces

import domain.*
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
        return move.isHorizontal() &&
                !isStraightPathOccupied(board, move.copy(to = Castle.getKingPosition(move.to))) &&
                move.from in listOf(
                    Position(FIRST_COL, if (isWhite()) FIRST_ROW else LAST_ROW),
                    Position(LAST_COL, if (isWhite()) FIRST_ROW else LAST_ROW)
                )
    }
}

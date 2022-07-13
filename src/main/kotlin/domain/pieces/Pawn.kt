package domain.pieces

import domain.board.Board
import domain.game.Game
import domain.game.searchMoves
import domain.move.Move
import domain.move.extractMoveInfo
import domain.move.getEnPassantCapturedPawnPosition
import domain.move.isStraightPathOccupied

/**
 * Pawn piece with [type] 'P'.
 *
 * Moves one or two slots vertically or one diagonally (when capturing)
 * @param army piece army
 */
data class Pawn(override val army: Army) : Piece {

    override val type = PieceType.PAWN

    override fun toString() = "$army $type"

    override fun isValidMove(board: Board, move: Move) =
        move.isVertical() && isValidPawnVerticalMove(board, move) ||
            move.isDiagonal() && isValidPawnDiagonalMove(board, move)

    override fun getAvailableMoves(game: Game, position: Board.Position): List<Move> =
        super.getAvailableMoves(game, position) +
            game.searchMoves(
                Move.extractMoveInfo("${type.symbol}${position}$DEFAULT_TO_POSITION=Q"),
                optionalToPos = true
            )

    /**
     * Checks if the vertical pawn move is possible.
     * First pawn move can be 2 or 1.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the vertical move is valid
     */
    private fun isValidPawnVerticalMove(board: Board, move: Move): Boolean {
        val defaultMove = move.rowDistanceIsOne()
        val isInInitialRow = move.from.row == if (isWhite()) WHITE_PAWN_INITIAL_ROW else BLACK_PAWN_INITIAL_ROW
        val doubleMove = isInInitialRow && move.rowsDistance() == (if (isWhite()) DOUBLE_MOVE else -DOUBLE_MOVE) &&
            !isStraightPathOccupied(board, move)

        return (defaultMove || doubleMove) && !board.isPositionOccupied(move.to)
    }

    /**
     * Checks if the diagonal pawn move is possible.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the diagonal move is valid
     */
    private fun isValidPawnDiagonalMove(board: Board, move: Move) =
        move.rowDistanceIsOne() && board.isPositionOccupied(move.to)

    /**
     * Checks if the en passant move is valid.
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the en passant move is valid
     */
    fun isValidEnPassant(board: Board, move: Move): Boolean {
        if (move.isDiagonal() && move.rowDistanceIsOne() &&
            (isWhite() && move.from.row == 5 || !isWhite() && move.from.row == 4)
        ) {
            val capturedPiecePos = getEnPassantCapturedPawnPosition(move.to, this)
            val capturedPiece = board.getPiece(capturedPiecePos)
            return board.isPositionOccupied(capturedPiecePos) && capturedPiece is Pawn && capturedPiece.army != army
        }

        return false
    }

    /**
     * Checks if the row distance is [ONE_MOVE]].
     * @return true if the distance between the rows is [ONE_MOVE]
     */
    private fun Move.rowDistanceIsOne() = rowsDistance() == if (isWhite()) ONE_MOVE else -ONE_MOVE
}

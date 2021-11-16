package domain

import domain.Board.*
import domain.pieces.*


/**
 * Checks if a move [moveInString] is valid.
 * Returns false when an [IllegalMoveException] is thrown.
 * @param moveInString piece move in string
 * @return true if the move is valid
 */
fun Board.isValidMove(moveInString: String): Boolean {
    try {
        Move(moveInString, this)
    }
    catch (err: IllegalMoveException){
        return false
    }
    return true
}

/**
 * Checks if the capture in [move] is valid.
 * @param move move with the capture
 * @return true if the capture is valid
 */
fun Board.isValidCapture(piece: Piece, move: Move): Boolean {
    if (move.capture || isPositionOccupied(move.to)) {
        val captured = getPiece(move.to) ?: return false

        return captured.army != piece.army
    }
    return true
}


/**
 * Checks if the promotion is valid and, if it is returns the new promoted piece.
 * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
 *
 * If no promote piece is specified, promote to queen by default.
 * @param piece piece to promote
 * @param toPos new piece position
 * @param promotion new piece type to promote
 * @param move Move String representation
 * @return promoted piece
 * @throws Throwable if the promotion is invalid
 */
fun doPromotion(piece: Piece, toPos: Position, promotion: Char?, move: String): Piece {
    if (piece is Pawn &&
        (piece.army == Army.WHITE && toPos.row == BLACK_FIRST_ROW ||
                piece.army == Army.BLACK && toPos.row == WHITE_FIRST_ROW)
    )
        return getPieceFromSymbol(promotion ?: 'Q', piece.army)
    else
        throw IllegalMoveException(move, "You cannot get promoted.")
}

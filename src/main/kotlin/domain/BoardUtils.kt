package domain

import domain.Board.*
import domain.pieces.*


/**
 * Checks if a move [stringMove] is valid.
 * Returns false when an [IllegalMoveException] is thrown.
 * @return true if the move is valid
 */
fun Board.isValidMove(stringMove: String): Boolean {
    try {
        Move(stringMove, this)
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
 * Verifies if the kings are in check after the move.
 * @param army army of the piece that was move
 */
fun Board.areKingsInCheck(army: Army) {
    /*// After doing the move, if the same army's king is in check, the move is invalid
    if (isKingInCheck(army) >= CHECK_BY_ONE)
        throw domain.IllegalMoveException("Invalid move, your army's king becomes in check.")

    // See if the opponent army's king is in check mate
    val kingInCheck = isKingInCheck(army.other())
    val kingCannotProtect = false //TODO()
    if (kingInCheck >= CHECK_BY_TWO || (kingInCheck == CHECK_BY_ONE && kingCannotProtect)) {
        //TODO(CHECK MATE)
    }*/
}


/**
 * Verifies if the king of given color is in check, returning how many pieces are attacking it.
 * @param army color of the king
 * @return number of pieces attacking the king
 */
fun Board.isKingInCheck(army: Army): Int {
    var checkCount = NOT_IN_CHECK

    var kingPosition: Position? = null

    // Find king
    for (pairPiecePosition in this) {
        val piece = pairPiecePosition.first
        val position = pairPiecePosition.second

        if (piece != null && piece.army == army && piece.symbol == 'K')
            kingPosition = position
    }

    require(kingPosition != null) { "King wasn't found!" }

    // For each piece, if its army is different from the king, check if it can capture the king
    for (pairPiecePosition in this) {
        val piece = pairPiecePosition.first
        val position = pairPiecePosition.second

        if (piece != null && piece.army == army.other()) {
            //if (isValidMove(Move(piece.symbol, position, capture = true, kingPosition, promotion = null)))
                checkCount++
        }
    }

    return checkCount
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

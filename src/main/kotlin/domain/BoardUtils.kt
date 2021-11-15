package domain

import domain.Board.*
import domain.pieces.*


/**
 * Checks if a move is valid.
 * @param move move to test
 * @return true if the move is valid
 * @throws IllegalMoveException if the initial position is invalid, if the capture is invalid or
 * if the move symbol is invalid
 */
fun Board.isValidMove(move: Move): Boolean {
    val piece =
        getPiece(move.from) ?: throw IllegalMoveException(move.toString(), "No piece in the specified position.")
    return isValidInitialPiece(piece, move) &&
            isValidCapture(piece, move) &&
            piece.isValidMove(this, move)
}


/**
 * Checks if the initial position of the move is valid,
 * by checking if piece is of the right type.
 * @param move move to test
 * @return if the the initial position is valid
 */
fun Board.isValidInitialPiece(piece: Piece, move: Move) =
    piece.symbol == move.symbol


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
            if (isValidMove(Move(piece.symbol, position, capture = true, kingPosition, promotion = null)))
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
 * @return promoted piece
 * @throws Throwable if the promotion is invalid
 */
fun doPromotion(piece: Piece, toPos: Position, promotion: Char?): Piece {
    if (piece is Pawn &&
        (piece.army == Army.WHITE && toPos.row == BLACK_FIRST_ROW ||
                piece.army == Army.BLACK && toPos.row == WHITE_FIRST_ROW)
    )
        return getPieceFromSymbol(promotion ?: 'Q', piece.army)
    else
        throw Throwable("You cannot get promoted.")
}

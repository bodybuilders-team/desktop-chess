import pieces.*
import Board.*


/**
 * Checks if a move is valid.
 * @param move move to test
 * @return true if the move is valid
 * @throws IllegalArgumentException if the initial position is invalid, if the capture is invalid or
 * if the move symbol is invalid
 */
fun Board.isValidMove(move: Move): Boolean {
    require(isValidInitialPiece(move))
    require(isValidCapture(move))

    return getPiece(move.from)!!.isValidMove(this, move) //TODO - Remove Double Bang (!!)
}


/**
 * Checks if the initial position of the move is valid,
 * by checking if the position is occupied and the piece is of the right type.
 * @param move move to test
 * @return if the the initial position is valid
 *
 * TODO(Use of positionIsOccupied prevents smart cast (piece != null). Currently using !! (bang operator).)
 */
fun Board.isValidInitialPiece(move: Move) =
    isPositionOccupied(move.from) && getPiece(move.from)!!.symbol == move.symbol //TODO - Remove Double Bang (!!)


/**
 * Checks if the capture in [move] is valid.
 * @param move move with the capture
 * @return true if the capture is valid
 */
fun Board.isValidCapture(move: Move): Boolean {
    if (move.capture || isPositionOccupied(move.to)) {
        val captured = getPiece(move.to) ?: return false

        return captured.army != getPiece(move.from)!!.army //TODO - Remove Double Bang (!!)
    }
    return true
}


/**
 * Verifies if the king of given color is in check, returning how many pieces are attacking it.
 * @param color color of the king
 * @return number of pieces attacking the king
 */
fun Board.isKingInCheck(color: Color): Int {
    var checkCount = NOT_IN_CHECK

    var kingPosition: Position? = null

    //Find king
    for (rowIdx in matrix.indices) {
        for (colIdx in matrix[rowIdx].indices) {
            val piece = matrix[rowIdx][colIdx]

            val actualRow = BOARD_SIDE_LENGTH - rowIdx
            if (piece != null && piece.army == color && piece.symbol == 'K')
                kingPosition = Position(FIRST_COL + colIdx, actualRow)
        }
    }

    require(kingPosition != null) { "King wasn't found!" }

    //For each piece, if its color is different from the king, check if it can capture the king
    for (rowIdx in matrix.indices) {
        for (colIdx in matrix[rowIdx].indices) {
            val piece = matrix[rowIdx][colIdx]

            val actualRow = BOARD_SIDE_LENGTH - rowIdx

            if (piece != null && piece.army == color.other()) {
                if (isValidMove(
                        Move(
                            piece.symbol,
                            Position(FIRST_COL + colIdx, actualRow),
                            true,
                            kingPosition,
                            null
                        )
                    )
                )
                    checkCount++
            }
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
        (piece.army == Color.WHITE && toPos.row == BLACK_FIRST_ROW ||
                piece.army == Color.BLACK && toPos.row == WHITE_FIRST_ROW)
    )
        return getPieceFromSymbol(promotion ?: 'Q', piece.army)
    else
        throw Throwable("You cannot get promoted.")
}

package domain.board

import domain.board.Board.*
import domain.move.*
import domain.pieces.*


// Board properties constants
const val BOARD_SIDE_LENGTH = 8
const val BOARD_SIZE = 64
const val FIRST_COL = 'a'
const val LAST_COL = 'h'
const val FIRST_ROW = 1
const val LAST_ROW = 8
const val BLACK_FIRST_ROW = 8
const val WHITE_FIRST_ROW = 1

val COLS_RANGE = FIRST_COL..LAST_COL
val ROWS_RANGE = FIRST_ROW..LAST_ROW

const val STRING_DEFAULT_BOARD =
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"


/**
 * 2D Matrix made with an array of arrays.
 */
typealias Matrix2D<T> = Array<Array<T>>


/**
 * Checks if a move [moveInString] is valid.
 * Returns false when an [IllegalMoveException] is thrown.
 * @param moveInString piece move in string
 * @return true if the move is valid
 */
fun Board.isValidMove(moveInString: String): Boolean {
    try {
        Move(moveInString, this, emptyList())
    } catch (err: IllegalMoveException) {
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
    val capturedPiece = getPiece(move.to) ?: return true

    return piece.army != capturedPiece.army
}


//TODO("Test")
/**
 * Checks if the promotion is valid and, if it is returns the new promoted piece.
 *
 * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
 * @param piece piece to promote
 * @param toPos new piece position
 * @param promotion new piece type to promote
 * @param moveInString Move String representation
 * @return promoted piece
 * @throws IllegalMoveException if the promotion is invalid
 */
fun getPromotedPiece(piece: Piece, toPos: Position, promotion: Char?, moveInString: String): Piece {
    requireNotNull(promotion) { "Invalid promotion." }

    return if (piece is Pawn &&
        (piece.isWhite() && toPos.row == BLACK_FIRST_ROW || !piece.isWhite() && toPos.row == WHITE_FIRST_ROW)
    )
        getPieceFromSymbol(promotion, piece.army)
    else
        throw IllegalMoveException(moveInString, "You cannot get promoted.")
}


/**
 * Places/Removes the other piece from a special move.
 * In a castle move, the other piece can be a king or a rook.
 * In an en passant move, the other piece is the captured pawn.
 * @param move move to make
 * @param piece already moved piece
 * @param board board to place/remove piece
 */
fun Board.placePieceFromSpecialMoves(move: Move, piece: Piece, board: Board) {
    if (move.type == MoveType.CASTLE) {
        val toRemovePos =
            if (piece is King) Castle.getRookPosition(move.to) else Castle.getKingPosition(move.to)
        val toRemove = getPiece(toRemovePos)

        board.removePiece(toRemovePos)
        board.setPiece(
            if (piece is King) Castle.getRookToPosition(move.to) else Castle.getKingToPosition(move.to),
            toRemove
        )
    } else if (move.type == MoveType.EN_PASSANT)
        board.removePiece(getEnPassantCapturedPawnPosition(move.to, piece))
}


/**
 * Returns matrix from received string board.
 * @param stringBoard board in string
 * @return matrix from received string board.
 */
fun getMatrix2DFromString(stringBoard: String): Matrix2D<Piece?> {
    require(stringBoard.length == BOARD_SIZE) { "Board doesn't have the correct size (BOARD_SIZE = $BOARD_SIZE)" }

    val chessBoard = Matrix2D<Piece?>(BOARD_SIDE_LENGTH) { Array(BOARD_SIDE_LENGTH) { null } }

    stringBoard.forEachIndexed { idx, char ->
        val row = idx / BOARD_SIDE_LENGTH
        val col = idx % BOARD_SIDE_LENGTH
        chessBoard[row][col] =
            if (char == ' ') null
            else getPieceFromSymbol(char.uppercaseChar(), if (char.isUpperCase()) Army.WHITE else Army.BLACK)
    }
    return chessBoard
}

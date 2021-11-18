package domain

import domain.Board.*
import domain.pieces.*


// Board properties constants
const val BOARD_SIDE_LENGTH = 8
const val BOARD_SIZE = 64
const val FIRST_COL = 'a'
const val FIRST_ROW = 1
const val BLACK_FIRST_ROW = 8
const val WHITE_FIRST_ROW = 1

val COLS_RANGE = 'a'..'h'
val ROWS_RANGE = 1..8

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
        Move(moveInString, this)
    }
    catch (err: IllegalMoveException) {
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
    if (isPositionOccupied(move.to)) {
        val captured = getPiece(move.to) ?: return false
        return captured.army != piece.army
    }
    return true
}


/**
 * Checks if the promotion is valid and, if it is returns the new promoted piece.
 *
 * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
 * @param piece piece to promote
 * @param toPos new piece position
 * @param promotion new piece type to promote
 * @param move Move String representation
 * @return promoted piece
 * @throws IllegalMoveException if the promotion is invalid
 */
fun getPromotedPiece(piece: Piece, toPos: Position, promotion: Char?, move: String): Piece {
    requireNotNull(promotion) { "Invalid promotion." }

    return if (piece is Pawn &&
        (piece.isWhite() && toPos.row == BLACK_FIRST_ROW || !piece.isWhite() && toPos.row == WHITE_FIRST_ROW)
    )
        getPieceFromSymbol(promotion, piece.army)
    else
        throw IllegalMoveException(move, "You cannot get promoted.")
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

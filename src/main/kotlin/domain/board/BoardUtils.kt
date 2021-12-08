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
const val BLACK_FIRST_ROW = LAST_ROW
const val WHITE_FIRST_ROW = FIRST_ROW

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
 * Places/Removes the other piece from a special move.
 *
 * In a castle move where the king is moved, the other piece is a rook, and vice-versa.
 * In an en passant move, the other piece is the captured pawn.
 * @param move move to make
 * @param piece already moved piece
 */
fun Board.placePieceFromSpecialMoves(move: Move, piece: Piece) {
    if (move.type == MoveType.CASTLE) {
        val toRemovePos =
            if (piece is King) Castle.getRookPosition(move.to) else Castle.getKingPosition(move.to)
        val toRemove = getPiece(toRemovePos)
        requireNotNull(toRemove) { "No piece in the position. Expected rook/king." }

        removePiece(toRemovePos)
        setPiece(
            if (piece is King) Castle.getRookToPosition(move.to) else Castle.getKingToPosition(move.to),
            toRemove
        )
    } else if (move.type == MoveType.EN_PASSANT)
        removePiece(getEnPassantCapturedPawnPosition(move.to, piece))
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

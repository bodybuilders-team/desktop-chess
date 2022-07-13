package domain.board

import domain.move.Castle
import domain.move.IllegalMoveException
import domain.move.Move
import domain.move.MoveType
import domain.move.getEnPassantCapturedPawnPosition
import domain.pieces.Army
import domain.pieces.Piece
import domain.pieces.getPieceFromSymbol

// Board properties constants
const val BOARD_SIDE_LENGTH = 8
const val BOARD_SIZE = BOARD_SIDE_LENGTH * BOARD_SIDE_LENGTH
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
 * Returns list of pieces from received string board.
 * @param stringBoard board in string
 * @return list of pieces from received string board.
 * @throws IllegalArgumentException if the board doesn't have the correct size [BOARD_SIZE]
 */
fun getPiecesFromString(stringBoard: String): List<Piece?> {
    require(stringBoard.length == BOARD_SIZE) { "Board doesn't have the correct size (BOARD_SIZE = $BOARD_SIZE)" }

    return stringBoard.map { char ->
        if (char == ' ') null
        else getPieceFromSymbol(char.uppercaseChar(), if (char.isUpperCase()) Army.WHITE else Army.BLACK)
    }
}

/**
 * Places/Removes the other piece from a special move.
 *
 * In a castle move, the other piece is a rook.
 * In an en passant move, the other piece is the captured pawn.
 * @param move move to make
 * @param piece already moved piece
 * @throws IllegalMoveException if there's no piece to place
 * @return new board with the pieces placed/removed
 */
fun Board.placePieceFromSpecialMoves(move: Move, piece: Piece) =
    when (move.type) {
        MoveType.CASTLE -> {
            val toRemovePos = Castle.getRookPosition(move.to)
            val toRemove = getPiece(toRemovePos)
            requireNotNull(toRemove) { "No piece in the position. Expected rook." }

            removePiece(toRemovePos).placePiece(Castle.getRookToPosition(move.to), toRemove)
        }
        MoveType.EN_PASSANT -> removePiece(getEnPassantCapturedPawnPosition(move.to, piece))
        MoveType.NORMAL -> this
    }

/**
 * Returns a new list with element at index [at] replaced by [new].
 * @param at index to replace at
 * @param new element to replace previous
 */
fun <T> List<T>.replace(at: Int, new: T) =
    mapIndexed { idx, elem -> if (idx == at) new else elem }

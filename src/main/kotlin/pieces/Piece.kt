package pieces

import Board
import Move
import kotlin.math.abs


// Constants.
const val WHITE_PAWN_INITIAL_ROW = 2
const val BLACK_PAWN_INITIAL_ROW = 7
const val DOUBLE_MOVE = 2
const val ONE_MOVE = 1
const val NO_MOVE = 0


/**
 * Piece color.
 */
enum class Color { WHITE, BLACK }


/**
 * Returns a Piece from its representative [symbol]
 * @param symbol char that represents the Piece type
 * @param color color of the piece
 * @return piece from its representative
 * @throws IllegalArgumentException if [symbol] is invalid
 */
fun getPieceFromSymbol(symbol: Char, color: Color): Piece {
    return when (symbol) {
        'R' -> Rook(color)
        'P' -> Pawn(color)
        'K' -> King(color)
        'Q' -> Queen(color)
        'B' -> Bishop(color)
        'N' -> Knight(color)
        else -> throw IllegalArgumentException("Invalid piece symbol.")
    }
}


/**
 * Chess piece.
 * @property symbol char that represents the piece type
 * @property color piece color (White or Black)
 */
interface Piece {
    val color: Color
    val symbol: Char
}


/**
 * Checks if the piece color is White.
 * @return true if the piece is white
 */
fun Piece.isWhite() = color == Color.WHITE


/**
 * Returns true if there are pieces between diagonal made in [move].
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if there are pieces between diagonal
 */
fun checkPiecesInBetweenDiagonal(board: Board, move: Move): Boolean {
    var rowsDistance = move.rowsDistance() + if (move.rowsDistance() > 0) -1 else 1
    var colsDistance = move.colsDistance() + if (move.colsDistance() > 0) -1 else 1
    // Number of steps can be calculated with rows Distance or cols Distance
    var numberOfSteps = abs(move.rowsDistance()) - 1

    while (numberOfSteps > 0) {
        if (board.positionIsOccupied(
                move.from.copy(
                    col = move.from.col + colsDistance,
                    row = move.from.row + rowsDistance
                )
            )
        ) return true
        if (colsDistance > 0) colsDistance-- else colsDistance++
        if (rowsDistance > 0) rowsDistance-- else rowsDistance++
        numberOfSteps--
    }
    return false
}


/**
 * Returns true if there are pieces between non-diagonal made in [move].
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if there are pieces between non-diagonal
 */
fun checkPiecesInBetweenNonDiagonal(board: Board, move: Move): Boolean {
    var distance = (if (move.isHorizontal()) move.to.col - move.from.col else move.to.row - move.from.row) +
            if (move.colsDistance() > 0 || move.rowsDistance() > 0) -1 else 1


    while (abs(distance) > 0) {
        if (move.isHorizontal() && board.positionIsOccupied(move.from.copy(col = move.from.col + distance))
            || move.isVertical() && board.positionIsOccupied(move.from.copy(row = move.from.row + distance))
        )
            return true

        if (distance > 0) distance-- else distance++
    }
    return false
}

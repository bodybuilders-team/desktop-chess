package domain.pieces

import domain.Board
import domain.Move
import kotlin.math.abs


// Constants.
const val WHITE_PAWN_INITIAL_ROW = 2
const val BLACK_PAWN_INITIAL_ROW = 7
const val DOUBLE_MOVE = 2
const val ONE_MOVE = 1
const val NO_MOVE = 0


/**
 * Piece army.
 */
enum class Army {
    WHITE, BLACK;

    /**
     * Returns the other army.
     * @return other army
     */
    fun other() = if (this == WHITE) BLACK else WHITE
}


/**
 * Returns a Piece from its representative [symbol]
 * @param symbol char that represents the Piece type
 * @param army color of the piece
 * @return piece from its representative
 * @throws IllegalArgumentException if [symbol] is invalid
 */
fun getPieceFromSymbol(symbol: Char, army: Army): Piece {
    return when (symbol) {
        'R' -> Rook(army)
        'P' -> Pawn(army)
        'K' -> King(army)
        'Q' -> Queen(army)
        'B' -> Bishop(army)
        'N' -> Knight(army)
        else -> throw IllegalArgumentException("Invalid piece symbol.")
    }
}


/**
 * Chess piece.
 * @property symbol char that represents the piece type
 * @property army piece army (White or Black)
 */
interface Piece {
    val army: Army
    val symbol: Char

    /**
     * Returns character representation of the piece as seen in game
     * @return character representation of the piece
     */
    fun toChar(): Char =
        if (army == Army.BLACK) symbol.lowercaseChar() else symbol

    /**
     * Checks if a move is possible regarding this specific piece type
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the move is possible
     */
    fun isValidMove(board: Board, move: Move): Boolean
}


/**
 * Returns true if there are pieces in the diagonal path between fromPos and toPos of [move].
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if there are pieces between diagonal
 */
fun isDiagonalPathOccupied(board: Board, move: Move): Boolean {
    var rowsDistance = distanceWithoutToPosition(move.rowsDistance())
    var colsDistance = distanceWithoutToPosition(move.colsDistance())

    for (step in abs(move.rowsDistance()) - 1 downTo 1) {
        if (board.isPositionOccupied(move.from.copy(
                    col = move.from.col + colsDistance,
                    row = move.from.row + rowsDistance
            ))
        ) return true

        colsDistance = updatedDistance(colsDistance)
        rowsDistance = updatedDistance(rowsDistance)
    }

    return false
}


/**
 * Returns true if there are pieces in the straight path between fromPos and toPos of [move].
 * 
 * The straight path might be horizontal or vertical.
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if there are pieces between straight path
 */
fun isStraightPathOccupied(board: Board, move: Move): Boolean {
    var distance = distanceWithoutToPosition(if (move.isHorizontal()) move.colsDistance() else move.rowsDistance())

    while (abs(distance) > 0) {
        if (move.isHorizontal() && board.isPositionOccupied(move.from.copy(col = move.from.col + distance))
            || move.isVertical() && board.isPositionOccupied(move.from.copy(row = move.from.row + distance))
        )
            return true

        distance = updatedDistance(distance)
    }
    return false
}


/**
 * Returns the distance received incremented if it's negative or decremented if it's positive
 * @param distance distance to increment/decrement
 * @return new distance incremented/decremented
 */
private fun updatedDistance(distance: Int): Int = if (distance > 0) distance - ONE_MOVE else distance + ONE_MOVE


/**
 * Receives a distance represented by an Int and returns that distance without the to position.
 * @param distance distance to remove to position
 * @return distance without the to position.
 */
private fun distanceWithoutToPosition(distance: Int) = distance + if (distance > 0) -ONE_MOVE else ONE_MOVE

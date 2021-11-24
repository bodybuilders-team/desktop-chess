package domain

import domain.pieces.ONE_MOVE
import domain.Board.*
import kotlin.math.abs


/**
 * Apply [testPos] to all positions in the straight path between fromPos and toPos of [move].
 *
 * The straight path might be horizontal or vertical.
 * @param move move with the positions to check
 * @param includeFromPos if the fromPos is to be included
 * @param testPos function that tests the position and returns boolean
 * @return true if [testPos] returns true
 */
fun applyToPositionsInStraightPath(move: Move, includeFromPos: Boolean, testPos: (Position) -> Boolean): Boolean {
    var distance = distanceWithoutToPosition(if (move.isHorizontal()) move.colsDistance() else move.rowsDistance())

    while (abs(distance) >= if (includeFromPos) 0 else 1) {
        if (move.isHorizontal() && testPos(move.from.copy(col = move.from.col + distance))
            || move.isVertical() && testPos(move.from.copy(row = move.from.row + distance))
        )
            return true

        if (distance == 0) break

        distance = updatedDistance(distance)

    }
    return false
}


/**
 * Apply [testPos] to all positions in the diagonal path between fromPos and toPos of [move].
 * @param move move with the positions to check
 * @param includeFromPos if the fromPos is to be included
 * @param testPos function that tests the position and returns boolean
 * @return true if [testPos] returns true
 */
fun applyToPositionsInDiagonalPath(move: Move, includeFromPos: Boolean, testPos: (Position) -> Boolean): Boolean {
    var rowsDistance = distanceWithoutToPosition(move.rowsDistance())
    var colsDistance = distanceWithoutToPosition(move.colsDistance())

    for (step in abs(move.rowsDistance()) - ONE_MOVE downTo if (includeFromPos) 0 else 1) {
        if (testPos(move.from.copy(col = move.from.col + colsDistance, row = move.from.row + rowsDistance)))
            return true

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
fun isStraightPathOccupied(board: Board, move: Move) =
    applyToPositionsInStraightPath(move, includeFromPos = false) { pos ->
        board.isPositionOccupied(pos)
    }


/**
 * Returns true if there are pieces in the diagonal path between fromPos and toPos of [move].
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if there are pieces between diagonal
 */
fun isDiagonalPathOccupied(board: Board, move: Move) =
    applyToPositionsInDiagonalPath(move, includeFromPos = false) { pos ->
        board.isPositionOccupied(pos)
    }


/**
 * Checks if the straight move is valid.
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if the straight move is valid
 */
fun isValidStraightMove(board: Board, move: Move) = move.isStraight() && !isStraightPathOccupied(board, move)


/**
 * Checks if the diagonal move is valid.
 * @param board board where [move] will happen
 * @param move move with the positions to check
 * @return true if the diagonal move is valid
 */
fun isValidDiagonalMove(board: Board, move: Move) = move.isDiagonal() && !isDiagonalPathOccupied(board, move)


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

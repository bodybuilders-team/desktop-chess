package domain

import domain.pieces.Army
import domain.pieces.King


/**
 * Returns the number of [armyThatAttacks] pieces that attacks the [position].
 * @param position attacked position
 * @param armyThatAttacks army that attacks the position
 * @return the number of [armyThatAttacks] pieces that attacks the [position].
 */
fun Board.positionAttackers(position: Board.Position, armyThatAttacks: Army): Int {
    var checkCount = 0
    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val fromPos = Board.Position(col, row)
            val piece = getPiece(fromPos) ?: continue
            if (piece.army != armyThatAttacks) continue

            if (piece.isValidMove(this, Move(piece.type.symbol, fromPos, capture = true, position, promotion = null)))
                checkCount++
        }
    }

    return checkCount
}


/**
 * Returns the number of [army] king attackers.
 * @param position position of the king
 * @param army army of the king to check
 * @return the number of [army] king attackers.
 * @throws IllegalArgumentException if there's no king in the specified position
 * @throws IllegalArgumentException if the army of the king is wrong
 * @throws IllegalArgumentException if the king is checked by more than two pieces
 */
fun Board.kingAttackers(position: Board.Position, army: Army): Int {
    val piece = getPiece(position)
    require(piece is King) { "Theres no king in the specified position." }
    require(piece.army == army) { "The army of the king is wrong." }

    val kingAttackers = positionAttackers(position, army.other())
    require(kingAttackers <= 2) { "A king cannot be checked by more than two pieces." }

    return kingAttackers
}


/**
 * Checks if the king of the [army] is in check.
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheck(position: Board.Position, army: Army) = kingAttackers(position, army.other()) > 0


/**
 * Returns the position of the king of the [army]
 * @param army army of the king to get position
 * @return the position of the king of the [army]
 * @throws IllegalArgumentException if the [army] has no king
 */
fun Board.getPositionOfKing(army: Army): Board.Position {
    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val position = Board.Position(col, row)
            val piece = this.getPiece(position) ?: continue

            if (piece is King && piece.army == army)
                return position
        }
    }
    throw IllegalArgumentException("King was not found.")
}


/**
 * Checks if the king of the [army] is in check mate.
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheckMate(army: Army): Boolean {
    val kingPos = getPositionOfKing(army)
    val adjacentPositions = listOf(
        kingPos.copy(col = kingPos.col - 1),
        kingPos.copy(col = kingPos.col + 1),
        kingPos.copy(row = kingPos.row - 1),
        kingPos.copy(row = kingPos.row + 1),
        kingPos.copy(col = kingPos.col - 1, row = kingPos.row - 1),
        kingPos.copy(col = kingPos.col - 1, row = kingPos.row + 1),
        kingPos.copy(col = kingPos.col + 1, row = kingPos.row - 1),
        kingPos.copy(col = kingPos.col + 1, row = kingPos.row + 1)
    )

    return isKingInCheckTwice(kingPos, army) ||
            (isKingInCheck(kingPos, army) && !isKingProtectable(kingPos, army) &&
                    adjacentPositions.all { positionAttackers(it, army.other()) > 0 })
}


/**
 * Checks if the king of the [army] can be protected.
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king of the [army] can be protected.
 */
fun Board.isKingProtectable(position: Board.Position, army: Army): Boolean {
    TODO("To be implemented.")
}


/**
 * Checks if the king of the [army] is in check twice.
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king is in check twice
 */
fun Board.isKingInCheckTwice(position: Board.Position, army: Army) = kingAttackers(position, army) == 2

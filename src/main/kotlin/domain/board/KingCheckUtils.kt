package domain.board

import domain.*
import domain.pieces.*
import domain.board.Board.*
import domain.move.*


/**
 * Types of check.
 */
enum class Check {
    NO_CHECK,
    CHECK,
    CHECK_MATE,
    STALE_MATE
}


/**
 * Checks if the king of the [army] is in check.
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheck(position: Position, army: Army) = kingAttackers(position, army).isNotEmpty()

/**
 * Checks if the king of the [army] is in check.
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheck(army: Army) = kingAttackers(getKingPosition(army), army).isNotEmpty()


/**
 * Checks if the king of the [army] is in check mate.
 *
 * The king is in checkmate if all these conditions apply:
 * - The king is in check;
 * - The king is unprotectable, meaning no ally piece can remove the check, protecting the king (by being placed in front or killing the attacker);
 * - The king has nowhere to go. All adjacent positions are either occupied or attacked by an enemy piece.
 * @param army army of the king to check mate
 * @return true if the king is in check mate
 */
fun Board.isKingInCheckMate(army: Army): Boolean {
    val kingPos = getKingPosition(army)

    return isKingInCheck(kingPos, army) && !isKingProtectable(kingPos, army) && !canKingMove(kingPos, army)
}


//TODO("Test")
/**
 * Checks if the king of the [army] is in stalemate.
 * 
 * The king is in stalemate if the king isn't in check but the army has no valid moves.
 * @param army army of the king to stalemate
 * @return true if the king is in stalemate
 */
fun Board.isKingInStaleMate(army: Army): Boolean {
    val kingPos = getKingPosition(army)
    
    return !isKingInCheck(kingPos, army) && false // !hasAvailableMoves(army)
}


//TODO("Test")
/**
 * Checks if the king of the [army] is in mate (in checkmate or stalemate).
 * @param army army of the king to mate
 */
fun Board.isKingInMate(army: Army) = isKingInCheckMate(army) || isKingInStaleMate(army)


// TODO("Test")
/**
 * Returns list of moves attacking [army] king.
 * @param position position of the king
 * @param army army of the king to attack
 * @return list of moves attacking [army] king.
 * @throws IllegalArgumentException if the king is attacked by more than two pieces
 */
fun Board.kingAttackers(position: Position, army: Army): List<Move> {
    val kingAttackers = positionAttackers(position, army.other())
    require(kingAttackers.size <= 2) { "A king cannot be attacked by more than two pieces." }

    return kingAttackers
}


/**
 * Checks if the king of the [army] can be protected.
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king of the [army] can be protected.
 * @throws IllegalArgumentException if the king isn't in check, therefore not needing to be protected
 */
fun Board.isKingProtectable(position: Position, army: Army): Boolean {
    kingAttackers(position, army).forEach { attackingMove ->
        return when {
            attackingMove.isStraight() ->
                applyToPositionsInStraightPath(attackingMove, includeFromPos = true) { pos ->
                    positionAttackers(pos, army).any { it.symbol != PieceType.KING.symbol }
                }

            attackingMove.isDiagonal() ->
                applyToPositionsInDiagonalPath(attackingMove, includeFromPos = true) { pos ->
                    positionAttackers(pos, army).any { it.symbol != PieceType.KING.symbol }
                }

            else -> positionAttackers(attackingMove.from, army).any { it.symbol != PieceType.KING.symbol }
        }
    }

    return false
}


// TODO("Test")
/**
 * Checks if the [army] king can move to an adjacent position.
 * @param position position of the king
 * @param army army of the king to move
 * @return true if the [army] king can move to an adjacent position
 */
fun Board.canKingMove(position: Position, army: Army): Boolean {
    val adjacentPositions = listOf(
        position.copy(col = position.col - 1),
        position.copy(col = position.col + 1),
        position.copy(row = position.row - 1),
        position.copy(row = position.row + 1),
        position.copy(col = position.col - 1, row = position.row - 1),
        position.copy(col = position.col - 1, row = position.row + 1),
        position.copy(col = position.col + 1, row = position.row - 1),
        position.copy(col = position.col + 1, row = position.row + 1)
    )

    val dummyBoard = this.copy()
    dummyBoard.removePiece(position)

    return !adjacentPositions.all {
        dummyBoard.isPositionOccupied(it) ||
                dummyBoard.positionAttackers(it, army.other()).isNotEmpty()
    }
}


/**
 * Returns list of moves attacking the [position].
 * @param position attacked position
 * @param armyThatAttacks army that attacks the position
 * @return list of moves attacking the [position].
 */
fun Board.positionAttackers(position: Position, armyThatAttacks: Army): List<Move> {
    val attackingMoves = mutableListOf<Move>()

    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val fromPos = Position(col, row)
            val piece = getPiece(fromPos) ?: continue
            if (piece.army != armyThatAttacks) continue

            val move = Move(piece.type.symbol, fromPos, capture = true, position, promotion = null,  MoveType.NORMAL)
            if (piece.isValidMove(this, move) && isValidCapture(piece, move))
                attackingMoves += move
        }
    }

    return attackingMoves
}


/**
 * Returns the position of the king of the [army]
 * @param army army of the king to get position
 * @return the position of the king of the [army]
 * @throws IllegalArgumentException if the [army] has no king
 */
fun Board.getKingPosition(army: Army): Position {
    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val position = Position(col, row)
            val piece = this.getPiece(position) ?: continue

            if (piece is King && piece.army == army)
                return position
        }
    }
    throw IllegalArgumentException("King was not found.")
}

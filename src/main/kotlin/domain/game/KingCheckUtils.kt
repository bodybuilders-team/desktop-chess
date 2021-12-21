package domain.game

import domain.board.Board
import domain.pieces.*
import domain.board.Board.*
import domain.board.COLS_RANGE
import domain.board.ROWS_RANGE
import domain.move.*


// King Check Constants
const val DOUBLE_CHECK = 2
const val MAX_KING_ATTACKERS = DOUBLE_CHECK


/**
 * Checks if the king of the [army] is in check, knowing the king's [position].
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheck(position: Position, army: Army) = kingAttackers(position, army).isNotEmpty()


/**
 * Returns list of moves attacking [army] king.
 * @param position position of the king
 * @param army army of the king to attack
 * @return list of moves attacking [army] king.
 * @throws IllegalArgumentException if the king is attacked by more than two pieces
 */
fun Board.kingAttackers(position: Position, army: Army): List<Move> {
    val kingAttackers = positionAttackers(position, army.other())
    require(kingAttackers.size <= MAX_KING_ATTACKERS) { "A king cannot be attacked by more than two pieces." }

    return kingAttackers
}


/**
 * Checks if the king of the [army] can be protected from a check by an ally piece.
 * 
 * The king can't be protected by an ally in a double check (king must move).
 * 
 * The king is protectable when:
 * - an ally piece can be placed between the king and the attacking piece.
 * - an ally piece can capture the attacking piece.
 * @param position position of the king
 * @param army army of the king to protect
 * @return true if the king of the [army] can be protected from a check.
 */
fun Board.isKingProtectable(position: Position, army: Army): Boolean {
    val kingAttackers = kingAttackers(position, army)
    if (kingAttackers.size == DOUBLE_CHECK) return false

    kingAttackers.first().apply {
        return when {
            isStraight() ->
                anyPositionInStraightPath(this, includeFromPos = true) { pos ->
                    positionAttackers(pos, army).any { it.symbol != PieceType.KING.symbol }
                }

            isDiagonal() ->
                anyPositionInDiagonalPath(this, includeFromPos = true) { pos ->
                    positionAttackers(pos, army).any { it.symbol != PieceType.KING.symbol }
                }

            else -> positionAttackers(this.from, army).any { it.symbol != PieceType.KING.symbol }
        }
    }
}


/**
 * Checks if the [army] king can move to one of its adjacent positions.
 * 
 * The king can only move to an adjacent position if it is not occupied by an ally piece and not attacked by an enemy piece.
 * @param position position of the king
 * @param army army of the king to move
 * @return true if the [army] king can move to an adjacent position
 */
fun Board.canKingMove(position: Position, army: Army): Boolean {
    val dummyBoard = this.copy().removePiece(position)

    return getAdjacentPositions(position).any { pos ->
        (!dummyBoard.isPositionOccupied(pos) || dummyBoard.getPiece(pos)?.army == army.other()) &&
                dummyBoard.removePiece(pos).positionAttackers(pos, army.other()).isEmpty()
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

            val move = Move("${piece.type.symbol}$fromPos$position")
            if (move.isValidNormal(piece, this))
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


/**
 * Returns the adjacent positions of [position].
 * @param position position to get adjacent positions from
 * @return list of adjacent positions of [position]
 */
fun getAdjacentPositions(position: Position): List<Position> {
    val adjacentCols = listOf(position.col - 1, position.col + 1).filter { it in COLS_RANGE }
    val adjacentRows = listOf(position.row - 1, position.row + 1).filter { it in ROWS_RANGE }

    val adjacentPositions = mutableListOf<Position>()

    adjacentCols.forEach { col -> adjacentPositions.add(position.copy(col = col)) }
    adjacentRows.forEach { row -> adjacentPositions.add(position.copy(row = row)) }

    adjacentCols.forEach { col ->
        adjacentRows.forEach { row ->
            adjacentPositions.add(Position(col, row))
        }
    }
    
    return adjacentPositions
}

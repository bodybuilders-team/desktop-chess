package domain.board

import domain.Game
import domain.pieces.*
import domain.board.Board.*
import domain.move.*


// King Check Constants
const val DOUBLE_CHECK = 2
const val MAX_KING_ATTACKERS = DOUBLE_CHECK


/**
 * Checks if the king of the [army] is in check.
 * @param position position of the king
 * @param army army of the king to check
 * @return true if the king is in check
 */
private fun Board.isKingInCheck(position: Position, army: Army) = kingAttackers(position, army).isNotEmpty()

/**
 * Checks if the king of the [army] is in check.
 * @param army army of the king to check
 * @return true if the king is in check
 */
fun Board.isKingInCheck(army: Army) = isKingInCheck(getKingPosition(army), army)


/**
 * Checks if the king of the [army] is in check mate.
 *
 * The king is in checkmate if all these conditions apply:
 * - The king is in check;
 * - The king is not protectable, meaning no ally piece can remove the check, protecting the king (by being placed in front or killing the attacker);
 * - The king has nowhere to go. All adjacent positions are either occupied or attacked by an enemy piece.
 * @param army army of the king to check mate
 * @return true if the king is in check mate
 */
fun Board.isKingInCheckMate(army: Army): Boolean {
    val kingPos = getKingPosition(army)
    return isKingInCheck(kingPos, army) && !isKingProtectable(kingPos, army) && !canKingMove(kingPos, army)
}


/**
 * Checks if the king of the [army] is in stalemate.
 *
 * The king is in stalemate if the king isn't in check, but it's the army's turn and the army has no valid moves.
 * @param army army of the king to stalemate
 * @return true if the king is in stalemate
 */
fun Game.isKingInStaleMate(army: Army) =
    !board.isKingInCheck(board.getKingPosition(army), army) &&
            currentTurnArmy(moves) == army && !hasAvailableMoves(army)


/**
 * Checks if any king in the board is in mate (checkmate or stalemate).
 * @return true if any king in the board is in mate
 */
fun Game.isInMate() =
    board.isKingInCheckMate(Army.WHITE) || board.isKingInCheckMate(Army.BLACK) ||
            isKingInStaleMate(Army.WHITE) || isKingInStaleMate(Army.BLACK)


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

            val move = Move(piece.type.symbol, fromPos, capture = false, position, promotion = null, MoveType.NORMAL)
            if (piece.isValidMove(this, move) && move.isValidCapture(piece, this))
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

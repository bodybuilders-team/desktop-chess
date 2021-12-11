package domain.board

import domain.Game
import domain.pieces.*
import domain.board.Board.*
import domain.move.*


// King Check Constants
const val MAX_KING_ATTACKERS = 2


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
private fun Board.isKingInCheck(position: Position, army: Army) = kingAttackers(position, army).isNotEmpty()

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
                anyPositionInStraightPath(attackingMove, includeFromPos = true) { pos ->
                    positionAttackers(pos, army).any { it.symbol != PieceType.KING.symbol }
                }

            attackingMove.isDiagonal() ->
                anyPositionInDiagonalPath(attackingMove, includeFromPos = true) { pos ->
                    positionAttackers(pos, army).any { it.symbol != PieceType.KING.symbol }
                }

            else -> positionAttackers(attackingMove.from, army).any { it.symbol != PieceType.KING.symbol }
        }
    }

    return false
}


/**
 * Checks if the [army] king can move to an adjacent position.
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
            //TODO("Validate in the context of a game - special moves are currently not being counted here")
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

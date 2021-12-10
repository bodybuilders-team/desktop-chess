package domain.move

import domain.board.*
import domain.pieces.Army
import domain.board.Board.Position


/**
 * Gets a given position's available moves.
 * @param position position to get the available moves
 * @param previousMoves previous moves made
 * @return list of available moves
 */
fun Board.getAvailableMoves(position: Position, previousMoves: List<Move>) =
    getPiece(position)?.getAvailableMoves(this, position, previousMoves) ?: emptyList()


/**
 * Checks if a given army has any available valid moves to make.
 * @param army army to make the move
 * @param previousMoves previous moves made
 * @return true if the army has available moves
 */
fun Board.hasAvailableMoves(army: Army, previousMoves: List<Move>): Boolean {
    for (row in ROWS_RANGE) {
        for (col in COLS_RANGE) {
            val pos = Position(col, row)

            val piece = getPiece(pos) ?: continue
            if (piece.army != army) continue

            if (getAvailableMoves(pos, previousMoves).isNotEmpty()) return true
        }
    }

    return false
}

/**
 * Returns the army playing in the current turn
 * @param moves all game moves
 * @return the army playing in the current turn
 */
fun currentTurnArmy(moves: List<Move>) = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Returns true if it's the white army turn
 * @param moves all game moves
 * @return true if it's the white army turn
 */
fun isWhiteTurn(moves: List<Move>) = currentTurnArmy(moves) == Army.WHITE

package domain.move

import domain.board.*
import domain.pieces.Army
import domain.board.Board.Position


//TODO("Test")
/**
 * Gets a given position's available moves.
 * @param position position to get the available moves
 * @return list of available moves
 */
fun Board.getAvailableMoves(position: Position): List<Move> {
    TODO("To be implemented. Every piece class needs a new function" +
            "availableMoves() that calculates the available moves based on the piece type and position.")
}


//TODO("Test")
/**
 * Checks if a given army has any available valid moves to make.
 * @param army army to make the move
 * @return true if the army has available moves
 */
fun Board.hasAvailableMoves(army: Army): Boolean {
    for(row in ROWS_RANGE){
        for(col in COLS_RANGE){
            val pos = Position(col, row)
            val piece = getPiece(pos) ?: continue
            
            if(piece.army != army) continue
            
            if(getAvailableMoves(pos).isNotEmpty()) return true
        }
    }
    
    return false
}

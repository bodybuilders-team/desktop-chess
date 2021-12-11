package domain

import domain.board.*
import domain.move.*


/**
 * A chess game.
 * @property board game board
 * @property moves previously played moves
 */
data class Game(
    val board: Board,
    val moves: List<Move>
)



/**
 * Makes a move in the board.
 * @param move move to make
 * @return new board with the move made
 */
fun Game.makeMove(move: Move) =
    Game(board = board.makeMove(move), moves = moves + move)



/**
 * Searches for valid moves given a move and if the search is in a specific column or row.
 *
 * If [optionalFromCol] is true, all columns are searched. The same applies to [optionalFromRow].
 *
 * @param move move to search for
 * @param optionalFromCol if the search isn't in a specific column
 * @param optionalFromRow if the search isn't in a specific row
 * @param optionalToPos if the search isn't in a specific to position
 * @return all valid moves
 */
fun Game.searchMoves(move: Move, optionalFromCol: Boolean, optionalFromRow: Boolean, optionalToPos: Boolean): List<Move> {
    val foundMoves: MutableList<Move> = mutableListOf()

    val fromColSearchRange = if (optionalFromCol) COLS_RANGE else move.from.col..move.from.col
    val fromRowSearchRange = if (optionalFromRow) ROWS_RANGE else move.from.row..move.from.row

    val toColSearchRange = if (optionalToPos) COLS_RANGE else move.to.col..move.to.col
    val toRowSearchRange = if (optionalToPos) ROWS_RANGE else move.to.row..move.to.row

    for (fromRow in fromRowSearchRange) {
        for (fromCol in fromColSearchRange) {
            for (toRow in toRowSearchRange) {
                for (toCol in toColSearchRange) {
                    val fromPos = Board.Position(fromCol, fromRow)
                    val piece = board.getPiece(fromPos) ?: continue
                    if (piece.type.symbol != move.symbol) continue
                    if (piece.army != currentTurnArmy(moves)) continue

                    val validatedMove = move.copy(
                        from = fromPos,
                        to = Board.Position(toCol, toRow)
                    ).getValidatedMove(piece, this)
                    if (validatedMove != null) foundMoves.add(validatedMove)
                }
            }
        }
    }

    return foundMoves
}



/**
 * Returns a new game with the moves [movesInString] consecutively made and validated in the game.
 * @param movesInString moves to make in string
 * @return new game with the moves [movesInString] consecutively made and validated in the game
 */
fun gameFromMoves(vararg movesInString: String): Game {
    var newGame = Game(Board(), emptyList())

    movesInString.forEach { moveInString ->
        newGame = newGame.makeMove(Move.validated(moveInString, newGame))
    }

    return newGame
}


/**
 * Returns a new game with the moves [moves] consecutively made and validated in the game.
 * @param moves moves to make
 * @return new game with the moves [moves] consecutively made and validated in the game
 */
fun gameFromMoves(moves: List<Move>): Game =
    gameFromMoves(*moves.map { it.toString() }.toTypedArray())

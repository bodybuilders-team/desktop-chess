package domain

import domain.board.*
import domain.move.*
import domain.pieces.Army


/**
 * A chess game.
 * @property board game board
 * @property moves previously played moves
 */
data class Game(
    val board: Board,
    val moves: List<Move>
) {
    override fun toString(): String {
        return board.toString().chunked(BOARD_SIDE_LENGTH).joinToString("\n")
    }
}


/**
 * Different states the game can be in.
 */
enum class GameState {
    NO_CHECK,
    CHECK,
    CHECKMATE,
    STALEMATE,
    TIE
}


/**
 * Makes a move in the board.
 * @param move move to make
 * @return new board with the move made
 */
fun Game.makeMove(move: Move) =
    Game(board = board.makeMove(move), moves = moves + move)


/**
 * Searches for valid moves given an unvalidated (extracted) [move] and if the search is in a specific column or row.
 *
 * If [optionalFromCol] is true, all columns are searched for the from position. The same applies to [optionalFromRow].
 *
 * @param move move to search for
 * @param optionalFromCol if the search isn't in a specific column
 * @param optionalFromRow if the search isn't in a specific row
 * @param optionalToPos if the search isn't in a specific to position
 * @return all valid moves
 */
fun Game.searchMoves(
    move: Move,
    optionalFromCol: Boolean,
    optionalFromRow: Boolean,
    optionalToPos: Boolean
): List<Move> {
    val foundMoves: MutableList<Move> = mutableListOf()

    val searchRange = getSearchRanges(move, optionalFromCol, optionalFromRow, optionalToPos)

    for (fromRow in searchRange.fromRow) {
        for (fromCol in searchRange.fromCol) {
            for (toRow in searchRange.toRow) {
                for (toCol in searchRange.toCol) {
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
 * Search Ranges to be used in [searchMoves].
 */
private data class SearchRanges(
    val fromCol: Iterable<Char>, val fromRow: Iterable<Int>,
    val toCol: Iterable<Char>, val toRow: Iterable<Int>
)

/**
 * Obtains the search ranges to be used in [searchMoves].
 * @param move move to search for
 * @param optionalFromCol if the search isn't in a specific column
 * @param optionalFromRow if the search isn't in a specific row
 * @param optionalToPos if the search isn't in a specific to position
 */
private fun Game.getSearchRanges(
    move: Move,
    optionalFromCol: Boolean,
    optionalFromRow: Boolean,
    optionalToPos: Boolean
): SearchRanges {
    fun getColSearchRange(optional: Boolean, col: Char) = if (optional) COLS_RANGE else listOf(col)
    fun getRowSearchRange(optional: Boolean, row: Int) = when {
        move.type == MoveType.CASTLE -> listOf(if (isWhiteTurn(moves)) WHITE_FIRST_ROW else BLACK_FIRST_ROW)
        optional -> ROWS_RANGE
        else -> listOf(row)
    }

    val fromColSearchRange = getColSearchRange(optionalFromCol, move.from.col)
    val fromRowSearchRange = getRowSearchRange(optionalFromRow, move.from.row)
    val toColSearchRange = getColSearchRange(optionalToPos, move.to.col)
    val toRowSearchRange = getRowSearchRange(optionalToPos, move.to.row)

    return SearchRanges(fromColSearchRange, fromRowSearchRange, toColSearchRange, toRowSearchRange)
}

const val NUMBER_OF_MOVES_TO_DRAW = 100


/**
 * Checks if the game is tied by the 50 move rule:
 *
 * The fifty-move rule in chess states that a player can claim a draw if no capture has been made and no pawn
 * has been moved in the last fifty consecutive moves.
 *
 * It is designed to stop endgames going on forever when one (or both) players have no idea how to end the game.
 *
 * @return true if the game is tied by the 50 move rule
 */
fun Game.isTiedByFiftyMoveRule() =
    moves.size >= NUMBER_OF_MOVES_TO_DRAW && moves.takeLast(NUMBER_OF_MOVES_TO_DRAW).none { move ->
        move.capture || move.symbol == 'P'
    }


/**
 * Gets the current state of the game.
 * @return state of the game
 */
fun Game.getState(): GameState =
    when {
        board.isKingInCheckMate(Army.WHITE) || board.isKingInCheckMate(Army.BLACK)  -> GameState.CHECKMATE
        isKingInStaleMate(Army.WHITE) || isKingInStaleMate(Army.BLACK)              -> GameState.STALEMATE
        isTiedByFiftyMoveRule()                                                     -> GameState.TIE
        board.isKingInCheck(Army.WHITE) || board.isKingInCheck(Army.BLACK)          -> GameState.CHECK
        else -> GameState.NO_CHECK
    }


/**
 * Checks if the game ended.
 *
 * A game ends when:
 * - there's a checkmate
 * - there's a stalemate
 * - the 50 move rule is applied
 *
 * @return true if the game ended
 */
fun Game.ended() =
    getState() in listOf(GameState.CHECKMATE, GameState.STALEMATE, GameState.TIE)


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
 * Returns a new game with the moves [movesInString] consecutively made and validated in the game.
 * @param movesInString moves to make
 * @return new game with the moves [movesInString] consecutively made and validated in the game
 */
fun gameFromMoves(movesInString: List<String>): Game =
    gameFromMoves(*movesInString.toTypedArray())


/**
 * Returns a new game with the moves [moves] consecutively made and validated in the game.
 * @param moves moves to make
 * @return new game with the moves [moves] consecutively made and validated in the game
 */
@JvmName("gameFromMovesListOfMove")
fun gameFromMoves(moves: List<Move>): Game =
    gameFromMoves(moves.map { it.toString() })

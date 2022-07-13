package domain.game

import domain.board.BLACK_FIRST_ROW
import domain.board.Board.Position
import domain.board.COLS_RANGE
import domain.board.ROWS_RANGE
import domain.board.WHITE_FIRST_ROW
import domain.move.Move
import domain.move.MoveExtraction
import domain.move.MoveType
import domain.move.getValidatedMove
import domain.pieces.Army

/**
 * Searches for valid moves given a move extraction and if the search is in a specific to position.
 *
 * During the search, a move is a candidate for validation only if:
 * - There's a piece in the "from" position.
 * - The type of the moving piece is the one specified in the move.
 * - The moving piece's army is the one playing in the current turn.
 *
 * @param moveExtraction extraction of the move to search for (containing information on optional from column and row)
 * @param optionalToPos if the search isn't in a specific to position
 * @return all valid moves
 */
fun Game.searchMoves(moveExtraction: MoveExtraction, optionalToPos: Boolean): List<Move> {
    val foundMoves: MutableList<Move> = mutableListOf()

    val searchRange = getSearchRanges(moveExtraction, optionalToPos)

    for (fromRow in searchRange.fromRow) {
        for (fromCol in searchRange.fromCol) {
            for (toRow in searchRange.toRow) {
                for (toCol in searchRange.toCol) {
                    val move = moveExtraction.move.copy(from = Position(fromCol, fromRow), to = Position(toCol, toRow))
                    val piece = board.getPiece(move.from) ?: continue
                    if (piece.type.symbol != move.symbol) continue
                    if (piece.army != armyToPlay) continue

                    val validatedMove = move.getValidatedMove(piece, this)
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
    val fromCol: Iterable<Char>,
    val fromRow: Iterable<Int>,
    val toCol: Iterable<Char>,
    val toRow: Iterable<Int>
)

/**
 * Obtains the search ranges to be used in [searchMoves].
 *
 * If optionalFromCol or optionalFromRow from [moveExtraction] are true, all columns or rows are searched for (respectively).
 *
 * @param moveExtraction extraction of the move to search for (containing information on optional from column and row)
 * @param optionalToPos if the search isn't in a specific to position
 */
private fun Game.getSearchRanges(moveExtraction: MoveExtraction, optionalToPos: Boolean): SearchRanges {
    moveExtraction.apply {
        fun getColSearchRange(optional: Boolean, col: Char) = if (optional) COLS_RANGE else listOf(col)
        fun getRowSearchRange(optional: Boolean, row: Int) = when {
            move.type == MoveType.CASTLE -> listOf(if (armyToPlay == Army.WHITE) WHITE_FIRST_ROW else BLACK_FIRST_ROW)
            optional -> ROWS_RANGE
            else -> listOf(row)
        }

        val fromColSearchRange = getColSearchRange(optionalFromCol, move.from.col)
        val fromRowSearchRange = getRowSearchRange(optionalFromRow, move.from.row)
        val toColSearchRange = getColSearchRange(optionalToPos, move.to.col)
        val toRowSearchRange = getRowSearchRange(optionalToPos, move.to.row)

        return SearchRanges(fromColSearchRange, fromRowSearchRange, toColSearchRange, toRowSearchRange)
    }
}

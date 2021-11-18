package domain

import kotlin.math.abs
import domain.Board.Position


/**
 * Chess move
 * @property symbol Piece Moving
 * @property from original piece position
 * @property capture true if the piece will capture enemy piece
 * @property to new piece position
 * @property promotion new PieceType of promotion or null
 */
data class Move(
    val symbol: Char,
    val from: Position,
    val capture: Boolean,
    val to: Position,
    val promotion: Char?
) {
    companion object {

        private const val moveRegexFormat = "^[PKQNBR]?[a-h]?[1-8]?x?([a-h][1-8])(=[QNBR])?\$"
        private const val CAPTURE_CHAR = 'x'
        private const val PROMOTION_CHAR = '='
        private const val MIN_STRING_LEN = 1
        private const val TWO_STRING_LEN = 2
        private const val THREE_STRING_LEN = 3


        /**
         * Move constructor that receives a String and a Board.
         *
         * Move properties are extracted from the [string], and searching the [board], it's verified if the move is possible.
         * @param string move in string format
         * @param board board where the move will happen
         * @return the validated move
         * @throws IllegalMoveException if move is not possible in [board]
         */
        operator fun invoke(string: String, board: Board): Move {
            val (move, optionalFromCol, optionalFromRow) = extractMoveInfo(string)

            return searchMove(move, optionalFromCol, optionalFromRow, board) ?: throw IllegalMoveException(
                string, if (optionalFromCol || optionalFromRow) "Try with origin column and row." else "Illegal move."
            )
        }


        /**
         * Searches for a valid move given a move and if the search is in a specific column or row.
         * If [optionalFromCol] is true, all columns are searched. The same applies to [optionalFromRow]
         * @param move move to search for
         * @param optionalFromCol if the search isn't in a specific column
         * @param optionalFromRow if the search isn't in a specific row
         * @return the valid move or null if it wasn't found
         */
        private fun searchMove(move: Move, optionalFromCol: Boolean, optionalFromRow: Boolean, board: Board): Move? {
            var foundMove: Move? = null

            val colSearchRange = if (optionalFromCol) COLS_RANGE else move.from.col..move.from.col
            val rowSearchRange = if (optionalFromRow) ROWS_RANGE else move.from.row..move.from.row

            for (row in rowSearchRange) {
                for (col in colSearchRange) {
                    val pos = Position(col, row)
                    val piece = board.getPiece(pos) ?: continue

                    if (piece.type.symbol != move.symbol) continue

                    val newMove = move.copy(from = pos)
                    if (pos != newMove.to && piece.isValidMove(board, newMove) && board.isValidCapture(piece, newMove))
                    {
                        if (foundMove != null) return null
                        foundMove = newMove.copy(capture = board.isPositionOccupied(newMove.to))
                    }
                }
            }

            return foundMove
        }


        /**
         * Move extraction
         * @param move extracted move
         * @param optionalFromCol if from column is optional
         * @param optionalFromRow if from row is optional
         */
        data class MoveExtraction(val move: Move, val optionalFromCol: Boolean, val optionalFromRow: Boolean)


        /**
         * Extracts move information from a string.
         * This move is still unvalidated in the context of a board.
         * @param string move in string
         * @return MoveExtraction containing the move, and the if the column or row are optional
         * @throws IllegalMoveException if move string is not well formatted
         */
        fun extractMoveInfo(string: String): MoveExtraction {
            if (!isCorrectlyFormatted(string))
                throw IllegalMoveException(
                    string, "Unrecognized Play. Use format: [<piece>][<from>][x][<to>][=<piece>]"
                )

            var str = string

            val capture = CAPTURE_CHAR in str

            val promotion = if (PROMOTION_CHAR in str) str.last() else null
            if (promotion != null) str = str.dropLast(2)

            val toPos = Position(str[str.lastIndex - 1], str.last().digitToInt())
            str = str.dropLast(if (capture) 3 else 2)

            var pieceSymbol = 'P'
            var fromRow: Int? = null
            var fromCol: Char? = null


            when (str.length) {
                MIN_STRING_LEN ->
                    when {
                        str.first().isUpperCase() -> pieceSymbol = str.first()
                        str.first().isDigit()     -> fromRow = str.first().digitToInt()
                        str.first().isLowerCase() -> fromCol = str.first()
                    }

                TWO_STRING_LEN ->
                    when {
                        str.first().isUpperCase() -> {
                            pieceSymbol = str.first()
                            if (str.last().isLowerCase()) fromCol = str.last()
                            else fromRow = str.last().digitToInt()
                        }

                        str.first().isLowerCase() -> {
                            fromCol = str.first()
                            fromRow = str.last().digitToInt()
                        }
                    }

                THREE_STRING_LEN -> {
                    pieceSymbol = str.first()
                    fromCol = str[1]
                    fromRow = str[2].digitToInt()
                }
            }

            return MoveExtraction(
                Move(pieceSymbol, Position(fromCol ?: FIRST_COL, fromRow ?: FIRST_ROW), capture, toPos, promotion),
                optionalFromCol = fromCol == null,
                optionalFromRow = fromRow == null
            )
        }


        /**
         * Checks if a move in String is correctly formatted.
         * @param moveInString piece move
         * @return true if the move in String is correctly formatted
         */
        fun isCorrectlyFormatted(moveInString: String): Boolean {
            return moveRegexFormat.toRegex().containsMatchIn(moveInString)
        }
    }


    /**
     * Return true if the movement is vertical
     * @return true if the movement is vertical
     */
    fun isVertical() = from.col == to.col

    /**
     * Return true if the movement is horizontal
     * @return true if the movement is horizontal
     */
    fun isHorizontal() = from.row == to.row

    /**
     * Return true if the movement is diagonal
     * @return true if the movement is diagonal
     */
    fun isDiagonal() = rowsAbsoluteDistance() == colsAbsoluteDistance()


    /**
     * Calculates the distance between the rows from the move
     * @return distance between the rows
     */
    fun rowsDistance(): Int = to.row - from.row

    /**
     * Calculates the absolute distance between the rows from the move
     * @return distance between the rows
     */
    fun rowsAbsoluteDistance(): Int = abs(rowsDistance())

    /**
     * Calculates the distance between the columns from the move
     * @return distance between the columns
     */
    fun colsDistance(): Int = to.col - from.col

    /**
     * Calculates the absolute distance between the columns from the move
     * @return distance between the columns
     */
    fun colsAbsoluteDistance(): Int = abs(colsDistance())


    override fun toString(): String {
        return "$symbol$from${if (capture) "x" else ""}$to${promotion ?: ""}" // ( ͡° ͜ʖ ͡°)
    }
}

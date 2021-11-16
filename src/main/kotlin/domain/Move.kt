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
         * @return the move
         * @throws IllegalMoveException if move string is not well formatted or if the move is not possible in [board]
         */
        operator fun invoke(string: String, board: Board): Move {
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


            /**
             * Searches for a valid move given [pieceSymbol], [row] and [col].
             * If [row] is null, all rows are searched. The same applies to [col]
             * @param pieceSymbol piece symbol of the move
             * @param row row to search
             * @param col col to search
             * @return the valid move or null if it wasn't found
             */
            fun searchMove(pieceSymbol: Char, row: Int?, col: Char?): Move? {
                for (pairPiecePosition in board) {
                    val (piece, pos) = pairPiecePosition
                    piece ?: continue
                    if (piece.symbol != pieceSymbol || (row != null && row != pos.row) || (col != null && col != pos.col))
                        continue

                    val move = Move(pieceSymbol, pos, capture, toPos, promotion)
                    if (pos != toPos && piece.isValidMove(board, move) && board.isValidCapture(piece, move))
                        return move.copy(capture = board.isPositionOccupied(toPos))
                }
                return null
            }


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

            return searchMove(pieceSymbol, fromRow, fromCol) ?: throw IllegalMoveException(string, "")
        }


        /**
         * Checks if a move in String is correctly formatted.
         * @param moveInString piece move
         * @return true if the move in String is correctly formatted
         */
        fun isCorrectlyFormatted(moveInString: String): Boolean{
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
        return "$symbol$from${if (capture) "x" else ""}$to${promotion ?: ""}"
    }
}

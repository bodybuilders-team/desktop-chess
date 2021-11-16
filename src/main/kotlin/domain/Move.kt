package domain

import kotlin.math.abs
import domain.Board.Position

const val CAPTURE_CHAR = 'x'
const val PROMOTION_CHAR = '='


// Move Regex Format
const val moveRegexFormat = "^[PKQNBR]?[a-h]?[1-8]?x?([a-h][1-8])(=[QNBR])?\$"


/**
 * Chess move
 * @property symbol Piece Moving
 * @property from original piece position
 * @property capture true if the piece will capture enemy piece
 * @property to new piece position
 * @property promotion new PieceType of promotion or null
 * @throws IllegalArgumentException if move string is not well formatted
 */
data class Move(
    val symbol: Char,
    val from: Position,
    val capture: Boolean,
    val to: Position,
    val promotion: Char?
) {
    companion object {
        operator fun invoke(string: String, board: Board): Move {
            require(isCorrectlyFormatted(string)) {
                "Unrecognized Play. Use format: [<piece>][<from>][x][<to>][=<piece>]"
            }

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
                    if (piece.symbol != pieceSymbol || (row != null && row != pos.row) || (col != null && col != pos.col)) continue

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
                1 ->
                    when {
                        str.first().isUpperCase() -> pieceSymbol = str.first()
                        str.first().isDigit()     -> fromRow = str.first().digitToInt()
                        str.first().isLowerCase() -> fromCol = str.first()
                    }

                2 ->
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

                3 -> {
                    pieceSymbol = str.first()
                    fromCol = str[1]
                    fromRow = str[2].digitToInt()
                }
            }

            return searchMove(pieceSymbol, fromRow, fromCol) ?: throw IllegalMoveException(string, "")
        }


        fun isCorrectlyFormatted(stringMove: String): Boolean{
            return moveRegexFormat.toRegex().containsMatchIn(stringMove)
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

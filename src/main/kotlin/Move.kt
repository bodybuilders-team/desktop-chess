import kotlin.math.abs
import Board.Position

// Move Regex Format
const val moveRegexFormat = "^[PKQNBR][a-h][1-8]x?[a-h][1-8](=[QNBR])?\$"

/**
 * Chess move
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
        operator fun invoke(string: String): Move {
            // Tests if the move is well formatted by using a regular expression (regex)
            require(moveRegexFormat.toRegex().containsMatchIn(string)) {
                "Use format: [<piece>][<from>][x][<to>][=<piece>]"
            }

            val pieceSymbol = string[PIECE_SYMBOL_IDX]

            val fromPos = Position(string[FROM_COL_IDX], string[FROM_ROW_IDX].digitToInt())

            val capture = CAPTURE_CHAR in string

            val captureOffset = if (capture) CAPTURE_OFFSET else NO_OFFSET

            val toPos = Position(
                string[TO_COL_IDX + captureOffset],
                string[TO_ROW_IDX + captureOffset].digitToInt()
            )

            return Move(
                symbol = pieceSymbol,
                from = fromPos,
                capture = capture,
                to = toPos,
                promotion = if (string.length > PROMOTION_IDX + captureOffset)
                    string[PROMOTION_PIECE_TYPE_IDX + captureOffset]
                else null
            )
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
}

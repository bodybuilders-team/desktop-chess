package domain.move

import kotlin.math.abs
import domain.board.Board.Position
import domain.game.*


/**
 * Type of the move.
 */
enum class MoveType {
    NORMAL,
    CASTLE,
    EN_PASSANT
}


/**
 * Chess move.
 * @property symbol Piece Moving
 * @property from original piece position
 * @property capture true if the piece will capture enemy piece
 * @property to new piece position
 * @property promotion new PieceType of promotion or null
 * @property type move type
 */
data class Move(
    val symbol: Char,
    val from: Position,
    val capture: Boolean,
    val to: Position,
    val promotion: Char?,
    val type: MoveType
) {
    companion object {

        private val normalMoveRegex = "[PKQNBR]?[a-h]?[1-8]?x?([a-h][1-8])(=[QNBR])?".toRegex().toString()
        private val castleMoveRegex = "O-O(-O)?".toRegex().toString()
        private val moveRegex = Regex("^($normalMoveRegex|$castleMoveRegex)\$")


        /**
         * Constructs a move from a String.
         * @param moveInString move in string format
         * @return the move extracted from the string, unvalidated in the context of a game
         */
        operator fun invoke(moveInString: String): Move =
            extractMoveInfo(moveInString).move


        /**
         * Returns a move already validated in the context of a game.
         *
         * Move properties are extracted from [moveInString], it's verified if the move is possible by searching the [game] board.
         * @param moveInString move in string format
         * @param game game where the move will happen
         * @return the validated move
         * @throws IllegalMoveException if move is not possible in [game] or multiple possible moves were found
         */
        fun validated(moveInString: String, game: Game): Move {
            val (move, optionalFromCol, optionalFromRow) = extractMoveInfo(moveInString)
            val validMoves = game.searchMoves(move, optionalFromCol, optionalFromRow, optionalToPos = false)

            if (validMoves.size != 1) throw IllegalMoveException(
                move.toString(optionalFromCol, optionalFromRow),
                if (optionalFromCol || optionalFromRow)
                    "Try with origin column and row." else "Illegal move."
            )

            return validMoves.first()
        }


        /**
         * Checks if a move in String is correctly formatted.
         * @param moveInString piece move
         * @return true if the move in String is correctly formatted
         */
        fun isCorrectlyFormatted(moveInString: String) = moveRegex.containsMatchIn(moveInString)
    }


    /**
     * Return true if the movement is vertical
     * @return true if the movement is vertical
     */
    fun isVertical() = from.col == to.col && rowsAbsoluteDistance() != 0

    /**
     * Return true if the movement is horizontal
     * @return true if the movement is horizontal
     */
    fun isHorizontal() = from.row == to.row && colsAbsoluteDistance() != 0

    /**
     * Return true if the movement is straight (horizontal or vertical)
     * @return true if the movement is straight (horizontal or vertical)
     */
    fun isStraight() = isHorizontal() xor isVertical()

    /**
     * Return true if the movement is diagonal
     * @return true if the movement is diagonal
     */
    fun isDiagonal() = rowsAbsoluteDistance() == colsAbsoluteDistance() && rowsDistance() != 0


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


    override fun toString() =
        if (type != MoveType.CASTLE)
            "$symbol$from" +
                    (if (capture) CAPTURE_CHAR else "") +
                    "$to" +
                    (if (promotion != null) "$PROMOTION_CHAR$promotion" else "") // ( ͡° ͜ʖ ͡°)
        else if (to.col == SHORT_CASTLE_KING_COL) SHORT_CASTLE_STRING else LONG_CASTLE_STRING

    /**
     * Returns a string representation of the move, with the possibility to omit fromCol and fromRow.
     * @param optionalFromCol if fromCol is to be omitted
     * @param optionalFromRow if romRow is to be omitted
     * @return string representation of the move
     */
    fun toString(optionalFromCol: Boolean, optionalFromRow: Boolean) =
        if (type != MoveType.CASTLE)
            toString().replaceRange(
                1..2,
                "${if (!optionalFromCol) from.col else ""}${if (!optionalFromRow) from.row else ""}"
            )
        else toString()

}


/**
 * Returns a list of moves from moves [movesInString].
 * @return list of moves from moves [movesInString]
 */
fun listOfMoves(vararg movesInString: String) = movesInString.map { Move(it) }

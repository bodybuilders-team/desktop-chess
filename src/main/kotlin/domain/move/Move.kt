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
        private val normalMoveRegexRequiredFromPosition =
            "[PKQNBR]?([a-h][1-8])x?([a-h][1-8])(=[QNBR])?".toRegex().toString()


        /**
         * Constructs a move, extracting it from a String.
         * 
         * This is the recommended way to create a move object since it saves the hassle of inserting the properties individually.
         *
         * This move is unvalidated in the context of a game. For that reason, it doesn't accept optional from position.
         * Only use when certain that it is a valid move.
         * @param moveInString move in string format
         * @return the move extracted from the string
         */
        operator fun invoke(moveInString: String): Move {
            require(
                Regex("^($normalMoveRegexRequiredFromPosition|$castleMoveRegex)\$")
                    .containsMatchIn(moveInString)
            ) {
                "Move.invoke only accepts a moveInString that includes from position."
            }
            return extractMoveInfo(moveInString).move
        }


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
            val extractedMove = extractMoveInfo(moveInString)
            val validMoves = game.searchMoves(extractedMove, optionalToPos = false)

            extractedMove.apply {
                if (validMoves.size != 1) throw IllegalMoveException(
                    toString(),
                    if (optionalFromCol || optionalFromRow)
                        "Try with origin column and row." else "Illegal move."
                )
            }

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
    fun isStraight() = isHorizontal() || isVertical()

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
}

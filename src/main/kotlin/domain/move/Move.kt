package domain.move

import domain.board.*
import kotlin.math.abs
import domain.board.Board.Position
import domain.pieces.*


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
    val promotion: Char?,
    val type: MoveType
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
         * Move properties are extracted from the [moveInString], and searching the [board], it's verified if the move is possible.
         * @param moveInString move in string format
         * @param board board where the move will happen
         * @return the validated move
         * @throws IllegalMoveException if move is not possible in [board]
         */
        operator fun invoke(moveInString: String, board: Board, previousMoves: List<Move>): Move {
            val (move, optionalFromCol, optionalFromRow) = extractMoveInfo(moveInString)

            return searchMove(move, optionalFromCol, optionalFromRow, board, previousMoves)
                ?: throw IllegalMoveException(
                    move.toString(optionalFromCol, optionalFromRow), //.removeRange((if(optionalFromCol) 1 else 2) .. (if(optionalFromRow) 2 else 1))
                    if (optionalFromCol || optionalFromRow)
                        "Try with origin column and row." else "Illegal move."
                )
        }


        /**
         * Searches for a valid move given a move and if the search is in a specific column or row.
         *
         * If [optionalFromCol] is true, all columns are searched. The same applies to [optionalFromRow].
         *
         * Only one move should exist, otherwise null is returned.
         *
         * @param move move to search for
         * @param optionalFromCol if the search isn't in a specific column
         * @param optionalFromRow if the search isn't in a specific row
         * @param board board to search for the move in
         * @param previousMoves previous moves made
         * @return the valid move or null if it wasn't found or multiple were found
         */
        private fun searchMove(
            move: Move, optionalFromCol: Boolean, optionalFromRow: Boolean, board: Board, previousMoves: List<Move>
        ): Move? {
            var foundMove: Move? = null

            val colSearchRange = if (optionalFromCol) COLS_RANGE else move.from.col..move.from.col
            val rowSearchRange = if (optionalFromRow) ROWS_RANGE else move.from.row..move.from.row

            for (row in rowSearchRange) {
                for (col in colSearchRange) {
                    val fromPos = Position(col, row)
                    val piece = board.getPiece(fromPos) ?: continue

                    if (piece.type.symbol != move.symbol) continue

                    val validatedMove = move.copy(from = fromPos).getValidatedMove(piece, board, previousMoves)

                    if (validatedMove != null) {
                        if (foundMove != null) return null
                        foundMove = validatedMove
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
         * @param moveInString move in string
         * @return MoveExtraction containing the move, and the if the column or row are optional
         * @throws IllegalMoveException if move string is not well formatted
         */
        private fun extractMoveInfo(moveInString: String): MoveExtraction {
            if (!isCorrectlyFormatted(moveInString))
                throw IllegalMoveException(
                    moveInString, "Unrecognized Play. Use format: [<piece>][<from>][x][<to>][=<piece>]"
                )

            var str = moveInString

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
                        str.first().isDigit() -> fromRow = str.first().digitToInt()
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
                Move(
                    pieceSymbol,
                    Position(fromCol ?: FIRST_COL, fromRow ?: FIRST_ROW),
                    capture,
                    toPos,
                    promotion,
                    MoveType.NORMAL
                ),
                optionalFromCol = fromCol == null,
                optionalFromRow = fromRow == null
            )
        }

        /**
         * Returns an unvalidated move in the context of the board.
         * @param moveInString piece move
         * @return true if the move in String is correctly formatted
         */
        fun getUnvalidatedMove(moveInString: String) = extractMoveInfo(moveInString).move


        /**
         * Checks if a move in String is correctly formatted.
         * @param moveInString piece move
         * @return true if the move in String is correctly formatted
         */
        fun isCorrectlyFormatted(moveInString: String) = moveRegexFormat.toRegex().containsMatchIn(moveInString)
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


    override fun toString(): String {
        return "$symbol$from${if (capture) "x" else ""}$to${if (promotion != null) "=$promotion" else ""}" // ( ͡° ͜ʖ ͡°)
    }

    /**
     * Returns a string representation of the move, with the possibility to omit fromCol and fromRow.
     * @param optionalFromCol if fromCol is to be omitted
     * @param optionalFromRow if romRow is to be omitted
     * @return string representation of the move
     */
    fun toString(optionalFromCol: Boolean, optionalFromRow: Boolean): String {
        return "$symbol" +
                "${if (!optionalFromCol) from.col else ""}${if (!optionalFromRow) from.row else ""}" +
                (if (capture) "x" else "") +
                "$to" +
                if (promotion != null) "=$promotion" else ""
    }
}


/**
 * Checks if the capture in the move is valid.
 *
 * Also checking, if the capture is a promotion, if it's a valid promotion.
 * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
 * @param piece move with the capture
 * @param board board where the move happens
 * @return true if the capture is valid
 */
fun Move.isValidCapture(piece: Piece, board: Board): Boolean {
    val isValidPromotion =
        if (this.promotion != null) piece is Pawn && (piece.isWhite() && to.row == BLACK_FIRST_ROW ||
                !piece.isWhite() && to.row == WHITE_FIRST_ROW)
        else true
    
    val capturedPiece = board.getPiece(this.to) ?: return !capture && isValidPromotion

    return piece.army != capturedPiece.army && isValidPromotion
}


/**
 * Gets a validated move, with information of its move type and capture, or null if the move isn't valid.
 * @param piece piece of the move's from position
 * @param board board where the move will happen
 * @param previousMoves previous moves made
 * @return validated move, with information of its move type and capture, or null if the move isn't valid.
 */
fun Move.getValidatedMove(piece: Piece, board: Board, previousMoves: List<Move>): Move? {
    val validMove = when {
        isValidEnPassant(piece, board, previousMoves)                       -> copy(type = MoveType.EN_PASSANT)
        isValidCastle(piece, board, previousMoves)                          -> copy(type = MoveType.CASTLE)
        piece.isValidMove(board, this) && isValidCapture(piece, board)      -> copy(type = MoveType.NORMAL)
        else -> return null
    }

    return validMove.copy(capture = board.isPositionOccupied(validMove.to))
}


/**
 * Type of the move.
 */
enum class MoveType {
    NORMAL,
    CASTLE,
    EN_PASSANT
}

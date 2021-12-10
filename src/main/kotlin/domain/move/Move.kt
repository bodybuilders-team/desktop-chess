package domain.move

import domain.board.*
import kotlin.math.abs
import domain.board.Board.Position
import domain.pieces.*


/**
 * Chess move.
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
         * Move constructor that receives a String.
         * @param moveInString move in string format
         * @return the move extracted from the string, unvalidated in the context of the board
         */
        operator fun invoke(moveInString: String): Move {
            return extractMoveInfo(moveInString).move
        }


        /**
         * Returns a move already validated in the context of a board/chess game.
         *
         * Move properties are extracted from the [moveInString], and searching the [board], it's verified if the move is possible.
         * @param moveInString move in string format
         * @param board board where the move will happen
         * @param previousMoves the previous moves in board
         * @return the validated move
         * @throws IllegalMoveException if move is not possible in [board] or multiple possible moves were found
         */
        fun validated(moveInString: String, board: Board, previousMoves: List<Move>): Move {
            val (move, optionalFromCol, optionalFromRow) = extractMoveInfo(moveInString)
            val validMoves =
                searchMoves(move, optionalFromCol, optionalFromRow, optionalToPos = false, board, previousMoves)

            if (validMoves.size != 1) throw IllegalMoveException(
                move.toString(optionalFromCol, optionalFromRow),
                if (optionalFromCol || optionalFromRow)
                    "Try with origin column and row." else "Illegal move."
            )

            return validMoves.first()
        }


        /**
         * Searches for valid moves given a move and if the search is in a specific column or row.
         *
         * If [optionalFromCol] is true, all columns are searched. The same applies to [optionalFromRow].
         *
         * @param move move to search for
         * @param optionalFromCol if the search isn't in a specific column
         * @param optionalFromRow if the search isn't in a specific row
         * @param optionalToPos if the search isn't in a specific to position
         * @param board board to search for the move in
         * @param previousMoves previous moves made
         * @return all valid moves
         */
        fun searchMoves(
            move: Move, optionalFromCol: Boolean, optionalFromRow: Boolean, optionalToPos: Boolean,
            board: Board, previousMoves: List<Move>
        ): List<Move> {
            val foundMoves: MutableList<Move> = mutableListOf()

            val fromColSearchRange = if (optionalFromCol) COLS_RANGE else move.from.col..move.from.col
            val fromRowSearchRange = if (optionalFromRow) ROWS_RANGE else move.from.row..move.from.row

            val toColSearchRange = if (optionalToPos) COLS_RANGE else move.to.col..move.to.col
            val toRowSearchRange = if (optionalToPos) ROWS_RANGE else move.to.row..move.to.row

            for (fromRow in fromRowSearchRange) {
                for (fromCol in fromColSearchRange) {
                    for (toRow in toRowSearchRange) {
                        for (toCol in toColSearchRange) {
                            val fromPos = Position(fromCol, fromRow)
                            val piece = board.getPiece(fromPos) ?: continue
                            if (piece.type.symbol != move.symbol) continue

                            val validatedMove = move.copy(
                                from = fromPos,
                                to = Position(toCol, toRow)
                            ).getValidatedMove(piece, board, previousMoves)
                            if (validatedMove != null) foundMoves.add(validatedMove)
                        }
                    }
                }
            }

            return foundMoves
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
         * 
         * The returned move will have [FIRST_COL] as the fromCol, and [FIRST_ROW] as the fromRow in case of optional column and/or row, respectively.
         * 
         * @param moveInString move in string
         * @return MoveExtraction containing the unvalidated move, and information on whether the column and/or row is optional
         * @throws IllegalMoveException if move string is not well formatted
         */
        fun extractMoveInfo(moveInString: String): MoveExtraction {
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


    override fun toString() =
        "$symbol$from${if (capture) "x" else ""}$to${if (promotion != null) "=$promotion" else ""}" // ( ͡° ͜ʖ ͡°)

    /**
     * Returns a string representation of the move, with the possibility to omit fromCol and fromRow.
     * @param optionalFromCol if fromCol is to be omitted
     * @param optionalFromRow if romRow is to be omitted
     * @return string representation of the move
     */
    fun toString(optionalFromCol: Boolean, optionalFromRow: Boolean) =
        "$symbol" +
                "${if (!optionalFromCol) from.col else ""}${if (!optionalFromRow) from.row else ""}" +
                (if (capture) "x" else "") +
                "$to" +
                if (promotion != null) "=$promotion" else ""
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
    val isPromotion = piece is Pawn && (piece.isWhite() && to.row == BLACK_FIRST_ROW ||
            !piece.isWhite() && to.row == WHITE_FIRST_ROW)
    
    val isValidPromotion = isPromotion == (promotion != null)

    val capturedPiece = board.getPiece(to) ?: return !capture && isValidPromotion

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
        isValidEnPassant(piece, board, previousMoves) -> copy(type = MoveType.EN_PASSANT)
        isValidCastle(piece, board, previousMoves) -> copy(type = MoveType.CASTLE)
        piece.isValidMove(board, this) && isValidCapture(piece, board) -> copy(type = MoveType.NORMAL)
        else -> return null
    }

    if (board.makeMove(validMove).isKingInCheck(piece.army)) return null

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

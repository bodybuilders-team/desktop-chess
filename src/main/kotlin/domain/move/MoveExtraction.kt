package domain.move

import domain.board.Board
import domain.board.FIRST_COL
import domain.board.FIRST_ROW


private const val CAPTURE_CHAR = 'x'
private const val PROMOTION_CHAR = '='
private const val MIN_STRING_LEN = 1
private const val TWO_STRING_LEN = 2
private const val THREE_STRING_LEN = 3


/**
 * Move extraction
 * @param move unvalidated extracted move
 * @param optionalFromCol if from column is optional
 * @param optionalFromRow if from row is optional
 */
data class MoveExtraction(val move: Move, val optionalFromCol: Boolean, val optionalFromRow: Boolean)


/**
 * Extracts move information from a string.
 * This move is still unvalidated in the context of a game.
 *
 * In case of optional column and/or row, the returned move will have [FIRST_COL] as the fromCol, and [FIRST_ROW] as the fromRow, respectively.
 *
 * @param moveInString move in string
 * @return MoveExtraction containing the unvalidated move, and information on whether the column and/or row is optional
 * @throws IllegalMoveException if move string is not well formatted
 */
fun Move.Companion.extractMoveInfo(moveInString: String): MoveExtraction {
    if (!isCorrectlyFormatted(moveInString))
        throw IllegalMoveException(
            moveInString,
            "Unrecognized Play. Use format: [<piece>][<from>][x][<to>][=<piece>], [O-O] or [O-O-O]"
        )

    if (moveInString in listOf("O-O", "O-O-O")) {
        return MoveExtraction(
            Move(
                'K',
                Board.Position(INITIAL_KING_COL, 1),
                capture = false,
                Board.Position(if (moveInString == "O-O") SHORT_CASTLE_KING_COL else LONG_CASTLE_KING_COL, 1),
                promotion = null,
                MoveType.CASTLE
            ),
            optionalFromCol = false,
            optionalFromRow = false
        )
    }

    var str = moveInString

    val capture = CAPTURE_CHAR in str

    val promotion = if (PROMOTION_CHAR in str) str.last() else null
    if (promotion != null) str = str.dropLast(2)

    val toPos = Board.Position(str[str.lastIndex - 1], str.last().digitToInt())
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
            Board.Position(fromCol ?: FIRST_COL, fromRow ?: FIRST_ROW),
            capture,
            toPos,
            promotion,
            MoveType.NORMAL
        ),
        optionalFromCol = fromCol == null,
        optionalFromRow = fromRow == null
    )
}

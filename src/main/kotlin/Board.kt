// Constants
const val BOARD_SIZE = 8

const val PIECE_IDX = 0
const val FROM_COL_IDX = 1
const val FROM_ROW_IDX = 2
const val CAPTURE_OR_TO_COL_IDX = 3
const val TO_COL_OR_ROW_IDX = 4
const val TO_ROW_IDX = 5
const val PROMOTION_IDX = 6

const val STRING_BOARD =
    "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"


/**
 * Represents the game board with the pieces.
 */
class Board {

    /**
     * Chess piece.
     * @property type piece type
     * @property position piece position in the board
     * @property color piece color (White or Black)
     */
    data class Piece(val type: PieceType, val position: Position, val color: Color)

    /**
     * Position of each board tile
     * @property column char in range ['a', 'h']
     * @property row int in range [0..7]
     */
    data class Position(val column: Char, val row: Int)

    /**
     * Piece color.
     */
    enum class Color { WHITE, BLACK }

    /**
     * Piece type.
     * @property initial char that represents the initial of each piece type.
     */
    enum class PieceType(val initial: Char) {
        ROOK('R'),
        KING('K'),
        QUEEN('Q'),
        KNIGHT('N'),
        BISHOP('B'),
        PAWN('P');

        override fun toString(): String = this.initial.toString()

        companion object {
            /**
             * Gets the PieceType by its initial.
             * @param initial PieceType initial to search
             * @return PieceType found by its initial
             * @throws IllegalArgumentException if there's no type with the initial [initial]
             */
            operator fun get(initial: Char) =
                requireNotNull(values().find { it.initial == initial }) { "No PieceType with initial $initial" }
        }
    }


    /**
     * 8x8 Matrix that represents the chess board.
     */
    private val chessBoard: Array<Array<Piece?>> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { null } }

    init {
        initializeMatrix()
    }

    /**
     * Initialize the [chessBoard].
     */
    private fun initializeMatrix() {
        STRING_BOARD.forEachIndexed { idx, char ->
            val row = idx / BOARD_SIZE
            val col = idx % BOARD_SIZE
            chessBoard[row][col] =
                if (char == ' ') null
                else Piece(
                    PieceType[char.uppercaseChar()],
                    Position('a' + col, row),
                    if (char.isUpperCase()) Color.WHITE else Color.BLACK
                )
        }
    }

    /**
     * Chess move
     * @property piece piece in the board
     * @property from original piece position
     * @property capture true if the piece will capture enemy piece
     * @property to new piece position
     * @property promotion new PieceType of promotion or null
     */
    data class Move(
        val piece: Piece?,
        val from: Position,
        val capture: Boolean,
        val to: Position,
        val promotion: PieceType?
    )

    /**
     * Convert a string to a Move.
     * @return move
     */
    private fun String.toMove(): Move {
        val fromPos = Position(this[FROM_COL_IDX], this[FROM_ROW_IDX].digitToInt())
        val capture = 'x' in this
        val toPos = Position(
            this[if (capture) TO_COL_OR_ROW_IDX else CAPTURE_OR_TO_COL_IDX],
            this[if (capture) TO_ROW_IDX else TO_COL_OR_ROW_IDX].digitToInt()
        )

        return Move(
            piece = chessBoard[fromPos.row - 1][fromPos.column - 'a' - 1],
            from = fromPos,
            capture = capture,
            to = toPos,
            promotion = if (this.length > PROMOTION_IDX) PieceType[this[PROMOTION_IDX]] else null
        )
    }

    /**
     * Move a piece in the board.
     * @param move piece move
     * @return new board with piece moved
     */
    fun makeMove(move: String): Board {
        val realMove = move.toMove()

        try {
            if (realMove.piece == null)
                throw Throwable("No piece in the specified position")

        } catch (err: Throwable) {
            println(err)
            return this
        }

        return this
    }


    /**
     * String representation of the game board.
     * @return string representation of the chess board
     */
    override fun toString(): String {
        return chessBoard.joinToString("") { row ->
            row.map { piece ->
                val initial = piece?.type?.initial ?: ' '
                if (initial != ' ' && piece?.color == Color.BLACK) initial.lowercaseChar() else initial
            }.joinToString("")
        }
    }
}

fun main() {
    val board = Board()

    board.makeMove("Pe9e9")
}

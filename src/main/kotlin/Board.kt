// Constants
const val BOARD_SIZE = 8
const val FIRST_COL = 'a'

// Move arguments index
const val FROM_COL_IDX = 1
const val FROM_ROW_IDX = 2
const val CAPTURE_OR_TO_COL_IDX = 3
const val TO_COL_OR_ROW_IDX = 4
const val TO_ROW_IDX = 5
const val PROMOTION_IDX = 6
const val CAPTURE_CHAR = 'x'
const val CAPTURE_OFFSET = 1
const val NO_OFFSET = 0

const val STRING_BOARD =
    "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"


typealias Matrix2D<T> = Array<Array<T>>


/**
 * Represents the game board with the pieces.
 */
class Board {

    /**
     * Chess piece.
     * @property type piece type
     * @property color piece color (White or Black)
     */
    data class Piece(val type: PieceType, val color: Color)

    /**
     * Position of each board tile
     * @property col char in range ['a', 'h']
     * @property row int in range [0..7]
     */
    data class Position(val col: Char, val row: Int)

    /**
     * Position of each matrix value.
     * @property col
     * @property row
     */
    data class Matrix2DPosition(val col: Int, val row: Int)

    /**
     * Converts a position into a matrix position.
     * @return Matrix position
     */
    private fun Position.toMatrixPosition() = Matrix2DPosition(col = col - FIRST_COL, row = BOARD_SIZE - row)

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
    private val chessBoard: Matrix2D<Piece?> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { null } }

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
                    if (char.isUpperCase()) Color.WHITE else Color.BLACK
                )
        }
    }

    /**
     * Chess move
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
     * @param stringMove piece move
     * @return new board with piece moved
     */
    fun makeMove(stringMove: String): Board {
        try {
            // Get move arguments
            val move = Move(stringMove)
            val fromPos = move.from.toMatrixPosition()
            val toPos = move.to.toMatrixPosition()
            val piece = chessBoard[fromPos.row][fromPos.col] ?: throw Throwable("No piece in the specified position.")

            // Board to return if it's a valid move
            val newBoard = this

            // If it's a capture move, check if it's a valid capture
            if (move.capture) checkCapture(toPos, piece)

            // Remove the piece from the original position
            newBoard.chessBoard[fromPos.row][fromPos.col] = null

            // If it's a promotion move, do promotion, else just put the piece in the to position
            newBoard.chessBoard[toPos.row][toPos.col] =
                if (move.promotion == null) piece
                else doPromotion(piece, toPos, move.promotion)

            return newBoard
        } catch (err: Throwable) {
            println(err)
            return this
        }
    }

    /**
     * Checks if the capture is valid.
     * @param toPos position to capture
     * @param piece piece to do the capture
     * @throws Throwable if the capture is invalid
     */
    private fun checkCapture(toPos: Matrix2DPosition, piece: Piece) {
        val captured = chessBoard[toPos.row][toPos.col]
            ?: throw Throwable("No enemy piece in the specified position. You cannot capture.")
        if (captured.color == piece.color)
            throw Throwable("You cannot capture your color pieces.")
    }

    /**
     * Checks if the promotion is valid and, if it is returns the new promoted piece.
     * @param piece piece to promote
     * @param toPos new piece position
     * @param promotion new piece type to promote
     * @return promoted piece
     * @throws Throwable if the promotion is invalid
     */
    private fun doPromotion(piece: Piece, toPos: Matrix2DPosition, promotion: PieceType): Piece {
        if (piece.color == Color.WHITE && toPos.row == 7 || piece.color == Color.BLACK && toPos.row == 0)
            return piece.copy(type = promotion)
        else
            throw Throwable("You cannot get promoted.")
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

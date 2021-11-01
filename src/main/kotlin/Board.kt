// Constants
const val BOARD_SIZE = 8
const val FIRST_COL = 'a'
const val BLACK_FIRST_ROW_IDX = 0
const val WHITE_FIRST_ROW_IDX = 7

const val moveRegexFormat = "^[PKQNBR][a-h][1-8]x?[a-h][1-8](=[KQNBR])?\$"

// Move arguments index
const val FROM_COL_IDX = 1
const val FROM_ROW_IDX = 2
const val TO_COL_IDX = 3
const val TO_ROW_IDX = 4
const val PROMOTION_IDX = 5
const val PROMOTION_PIECE_TYPE_IDX = 6
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
class Board(private val chessBoard: Matrix2D<Piece?> = getInitialBoard()) {

    /**
     * Chess piece.
     * @property type piece type
     * @property color piece color (White or Black)
     */
    data class Piece(val type: PieceType, val color: Color)

    /**
     * Returns the piece in [pos]
     * @param pos position to get piece
     * @return piece in [pos]
     */
    private fun getPiece(pos: Position) = chessBoard[BOARD_SIZE - pos.row][pos.col - FIRST_COL]

    /**
     * Sets [newPiece] in [pos]
     * @param pos position to set
     * @param newPiece piece to put in [pos]
     */
    private fun setPiece(pos: Position, newPiece: Piece?) {
        chessBoard[BOARD_SIZE - pos.row][pos.col - FIRST_COL] = newPiece
    }

    /**
     * Position of each board tile
     * @property col char in range ['a', 'h']
     * @property row int in range [0..7]
     */
    data class Position(val col: Char, val row: Int)

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
     * Chess move
     * @property from original piece position
     * @property capture true if the piece will capture enemy piece
     * @property to new piece position
     * @property promotion new PieceType of promotion or null
     */
    data class Move(
        val from: Position,
        val capture: Boolean,
        val to: Position,
        val promotion: PieceType?
    ) {
        companion object {
            operator fun invoke(string: String): Move {
                //Tests if the move is well formatted by using a regular expression (regex)
                require(moveRegexFormat.toRegex().containsMatchIn(string)) {
                    "Use format: [<piece>][<from>][x][<to>][=<piece>]"
                }

                val fromPos = Position(string[FROM_COL_IDX], string[FROM_ROW_IDX].digitToInt())
                val capture = CAPTURE_CHAR in string

                val captureOffset = if (capture) CAPTURE_OFFSET else NO_OFFSET

                val toPos = Position(
                    string[TO_COL_IDX + captureOffset],
                    string[TO_ROW_IDX + captureOffset].digitToInt()
                )

                return Move(
                    from = fromPos,
                    capture = capture,
                    to = toPos,
                    promotion = if (string.length > PROMOTION_IDX + captureOffset)
                        PieceType[string[PROMOTION_PIECE_TYPE_IDX + captureOffset]] else null
                )
            }
        }
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
            val fromPos = move.from
            val toPos = move.to
            val piece = getPiece(fromPos) ?: throw Throwable("No piece in the specified position.")

            // Board to return if it's a valid move
            val newBoard = this.copy()

            // If it's a capture move, check if it's a valid capture
            if (move.capture) checkCapture(toPos, piece)

            //TODO("Prevent pieces from doing whatever they want. Each piece has different moves.")

            // Remove the piece from the original position
            newBoard.setPiece(fromPos, null)

            // If it's a promotion move, do promotion, else just put the piece in the to position
            newBoard.setPiece(
                toPos,
                if (move.promotion == null) piece
                else doPromotion(piece, toPos, move.promotion)
            )

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
    private fun checkCapture(toPos: Position, piece: Piece) {
        val captured = getPiece(toPos)
            ?: throw Throwable("No enemy piece in the specified position. You cannot capture.")
        if (captured.color == piece.color)
            throw Throwable("You cannot capture your color pieces.")
    }


    /**
     * Checks if the promotion is valid and, if it is returns the new promoted piece.
     * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
     * @param piece piece to promote
     * @param toPos new piece position
     * @param promotion new piece type to promote
     * @return promoted piece
     * @throws Throwable if the promotion is invalid
     */
    private fun doPromotion(piece: Piece, toPos: Position, promotion: PieceType): Piece {
        //TODO("If no promote piece is specified, promote to queen by default or do nothing and require a choice?")
        if (piece.type == PieceType.PAWN &&
            (piece.color == Color.WHITE && toPos.row == BLACK_FIRST_ROW_IDX ||
                    piece.color == Color.BLACK && toPos.row == WHITE_FIRST_ROW_IDX)
        )
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


    /**
     * Returns a copy of this Board with the [chessBoard]
     * @param chessBoard chessboard of the returned board
     * @return copy of this board
     */
    fun copy(chessBoard: Matrix2D<Piece?> = this.chessBoard) = Board(chessBoard.copyOf())

}


/**
 * Returns initial chess board.
 * @return initial chess board
 */
private fun getInitialBoard(): Matrix2D<Board.Piece?> {
    val newChessBoard: Matrix2D<Board.Piece?> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { null } }
    STRING_BOARD.forEachIndexed { idx, char ->
        val row = idx / BOARD_SIZE
        val col = idx % BOARD_SIZE
        newChessBoard[row][col] =
            if (char == ' ') null
            else Board.Piece(
                Board.PieceType[char.uppercaseChar()],
                if (char.isUpperCase()) Board.Color.WHITE else Board.Color.BLACK
            )
    }
    return newChessBoard
}

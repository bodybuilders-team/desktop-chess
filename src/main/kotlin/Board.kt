// Constants
const val BOARD_SIZE = 8
const val FIRST_COL = 'a'
const val BLACK_FIRST_ROW = 8
const val WHITE_FIRST_ROW = 1


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
 * @property chessBoard 2DMatrix with the pieces
 */
data class Board(val chessBoard: Matrix2D<Piece?> = getInitialBoard()) {

    /**
     * Returns the piece in [pos]
     * @param pos position to get piece
     * @return piece in [pos]
     */
    fun getPiece(pos: Position) = chessBoard[BOARD_SIZE - pos.row][pos.col - FIRST_COL]

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
     * @property row int in range [1..8]
     */
    data class Position(val col: Char, val row: Int) {
        init {
            require(col in 'a'..'h' && row in 1..8) { "Invalid Position." }
        }
    }

    /**
     * Move a piece in the board.
     * @param moveInString piece move
     * @return new board with piece moved
     */
    fun makeMove(moveInString: String): Board {
        try {
            // Get move arguments
            val move = Move(moveInString)
            val fromPos = move.from
            val toPos = move.to
            val piece = getPiece(fromPos) ?: throw Throwable("No piece in the specified position.")

            // Check if it's a valid move
            if (!piece.checkMove(this, move)) throw Throwable("Invalid move.")

            // Check if it's a valid capture
            if (move.capture) {
                val captured = getPiece(toPos)
                    ?: throw Throwable("No enemy piece in the specified position. You cannot capture.")
                if (captured.color == piece.color) throw Throwable("You cannot capture your color pieces.")
            }

            // Board to return if it's a valid move
            val newBoard = this.copy()

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
     * Checks if the promotion is valid and, if it is returns the new promoted piece.
     * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
     * @param piece piece to promote
     * @param toPos new piece position
     * @param promotion new piece type to promote
     * @return promoted piece
     * @throws Throwable if the promotion is invalid
     */
    private fun doPromotion(piece: Piece, toPos: Position, promotion: Char): Piece {
        //TODO("If no promote piece is specified, promote to queen by default or do nothing and require a choice?")
        if (piece is Pawn &&
            (piece.color == Color.WHITE && toPos.row == BLACK_FIRST_ROW ||
                    piece.color == Color.BLACK && toPos.row == WHITE_FIRST_ROW)
        )
            return getPieceFromSymbol(promotion, piece.color)
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
                val initial = piece?.symbol ?: ' '
                if (initial != ' ' && piece?.color == Color.BLACK) initial.lowercaseChar() else initial
            }.joinToString("")
        }
    }


    /**
     * Returns a copy board, using the array function copyOf() for each array in the matrix.
     * @return copied board
     */
    private fun copy(): Board {
        val newBoard = Board(this.chessBoard.copyOf())
        repeat(this.chessBoard.size) {
            newBoard.chessBoard[it] = this.chessBoard[it].copyOf()
        }

        return newBoard
    }
}


/**
 * Returns initial chess board.
 * @return initial chess board
 */
fun getInitialBoard(): Matrix2D<Piece?> {
    val chessBoard = Matrix2D<Piece?>(BOARD_SIZE) { Array(BOARD_SIZE) { null } }

    STRING_BOARD.forEachIndexed { idx, char ->
        val row = idx / BOARD_SIZE
        val col = idx % BOARD_SIZE
        chessBoard[row][col] =
            if (char == ' ') null
            else getPieceFromSymbol(char.uppercaseChar(), if (char.isUpperCase()) Color.WHITE else Color.BLACK)
    }
    return chessBoard
}

import pieces.*

// Board properties constants
const val BOARD_SIDE_LENGTH = 8
const val BOARD_SIZE = 64
const val FIRST_COL = 'a'
const val BLACK_FIRST_ROW = 8
const val WHITE_FIRST_ROW = 1
val COLS_RANGE = 'a'..'h'
val ROWS_RANGE = 1..8


// Move arguments index
const val PIECE_SYMBOL_IDX = 0
const val FROM_COL_IDX = 1
const val FROM_ROW_IDX = 2
const val TO_COL_IDX = 3
const val TO_ROW_IDX = 4
const val PROMOTION_IDX = 5
const val PROMOTION_PIECE_TYPE_IDX = 6
const val CAPTURE_CHAR = 'x'
const val CAPTURE_OFFSET = 1
const val NO_OFFSET = 0

//King in check constants
const val NOT_IN_CHECK = 0
const val CHECK_BY_ONE = 1
const val CHECK_BY_TWO = 2

// Initial board in String format
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
 * 2D Matrix made with an array of arrays.
 */
typealias Matrix2D<T> = Array<Array<T>>


/**
 * Represents the game board with the pieces.
 * @property matrix 2DMatrix with the pieces
 */
data class Board(val matrix: Matrix2D<Piece?> = getMatrix2DFromString(STRING_BOARD)) {

    /**
     * Returns the piece in [pos]
     * @param pos position to get piece
     * @return piece in [pos]
     */
    fun getPiece(pos: Position) = matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL]

    /**
     * Returns the piece in [pos] or throws if no piece in the specified position.
     * @param pos position to get piece
     * @return piece in [pos]
     * @throws IllegalMoveException if no piece is in the specified position.
     */
    fun getPieceOrThrows(pos: Position) =
        getPiece(pos) ?: throw IllegalMoveException("No piece in the specified position.")


    /**
     * Sets [piece] in [pos]
     * @param pos position to set
     * @param piece piece to put in [pos]
     */
    private fun setPiece(pos: Position, piece: Piece?) {
        matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL] = piece
    }


    /**
     * Removes piece from [pos]
     * @param pos position to remove the piece
     */
    private fun removePiece(pos: Position) {
        setPiece(pos, null)
    }


    /**
     * Position of each board tile
     * @property col char in range ['a', 'h']
     * @property row int in range [1..8]
     */
    data class Position(val col: Char, val row: Int) {
        init {
            require(col in COLS_RANGE && row in ROWS_RANGE) { "Invalid Position." }
        }

        override fun toString(): String {
            return "$col$row"
        }
    }


    /**
     * Checks if a position is occupied by a piece.
     * @param position position to check
     * @return true if there's a piece in [position]
     */
    fun isPositionOccupied(position: Position) = getPiece(position) != null


    /**
     * Move a piece in the board.
     * @param moveInString piece move
     * @return new board with piece moved
     */
    fun makeMove(moveInString: String): Board {
        val move = Move(moveInString)
        val fromPos = move.from
        val toPos = move.to
        val piece = getPieceOrThrows(fromPos)

        if (!isValidMove(piece, move)) throw IllegalMoveException("Invalid move.")

        val newBoard = this.copy()
        newBoard.removePiece(fromPos)

        newBoard.setPiece(
            toPos,
            if (move.promotion == null) piece
            else doPromotion(piece, toPos, move.promotion)
        )

        //areKingsInCheck(piece.army) //TODO("Kings in check".)

        return newBoard
    }


    /**
     * String representation of the game board.
     * @return string representation of the chess board
     */
    override fun toString(): String {
        return matrix.joinToString("") { row ->
            row.map { piece ->
                val initial = piece?.symbol ?: ' '
                if (initial != ' ' && piece?.army == Color.BLACK) initial.lowercaseChar() else initial
            }.joinToString("")
        }
    }


    /**
     * Returns a copy board, using the array function copyOf() for each array in the matrix.
     * @return copied board
     */
    private fun copy(): Board {
        val newBoard = Board(this.matrix.copyOf())
        repeat(BOARD_SIDE_LENGTH) {
            newBoard.matrix[it] = this.matrix[it].copyOf()
        }

        return newBoard
    }


    /**
     * Iterator of all pieces and respective positions.
     * @return an iterator of a piece-position pair.
     */
    operator fun iterator(): Iterator<Pair<Piece?, Position>> {
        return object : Iterator<Pair<Piece?, Position>> {
            var rowIdx = 0
            var colIdx = 0

            override fun hasNext(): Boolean {
                return rowIdx != BOARD_SIDE_LENGTH - 1 && colIdx != BOARD_SIDE_LENGTH - 1
            }

            override fun next(): Pair<Piece?, Position> {
                val value = Pair(matrix[rowIdx][colIdx], Position(FIRST_COL + colIdx, BOARD_SIDE_LENGTH - rowIdx))
                if (++colIdx % BOARD_SIDE_LENGTH == 0) {
                    rowIdx++
                    colIdx = 0
                }

                return value
            }

        }
    }
}


/**
 * Returns initial chess board.
 * @return initial chess board
 */
fun getMatrix2DFromString(stringBoard: String): Matrix2D<Piece?> {
    require(stringBoard.length == BOARD_SIZE) { "Board doesn't have the correct size (BOARD_SIZE = $BOARD_SIZE)" }

    val chessBoard = Matrix2D<Piece?>(BOARD_SIDE_LENGTH) { Array(BOARD_SIDE_LENGTH) { null } }

    stringBoard.forEachIndexed { idx, char ->
        val row = idx / BOARD_SIDE_LENGTH
        val col = idx % BOARD_SIDE_LENGTH
        chessBoard[row][col] =
            if (char == ' ') null
            else getPieceFromSymbol(char.uppercaseChar(), if (char.isUpperCase()) Color.WHITE else Color.BLACK)
    }
    return chessBoard
}

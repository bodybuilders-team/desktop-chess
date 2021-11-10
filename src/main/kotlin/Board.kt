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
const val CHECK_BY_1 = 1
const val CHECK_BY_2 = 2

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
    private fun getPiece(pos: Position) = matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL]


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
    }

    /**
     * Move a piece in the board.
     * @param moveInString piece move
     * @return new board with piece moved
     */
    fun makeMove(moveInString: String): Board {
        val move = Move(moveInString)
        val fromPos = move.from
        val toPos = move.to
        val piece = getPiece(fromPos) ?: throw Throwable("No piece in the specified position.")

        if (!isValidMove(move)) throw Throwable("Invalid move.")

        val newBoard = this.copy()
        newBoard.removePiece(fromPos)

        newBoard.setPiece(
            toPos,
            if (move.promotion == null) piece
            else doPromotion(piece, toPos, move.promotion)
        )

        //After doing the move, if the same color king is in check, the move is invalid
        if (isKingInCheck(piece.color) >= CHECK_BY_1)
            throw Throwable("Invalid move, your side's king becomes in check.")

        //See if the opponent's king is in check mate (in check by two different pieces)
        val kingInCheck = isKingInCheck(piece.color.other())
        val kingCannotProtect = false //TODO()
        if (kingInCheck >= CHECK_BY_2 || (kingInCheck == CHECK_BY_1 && kingCannotProtect)) {
            //TODO(CHECK MATE)
        }

        return newBoard
    }


    /**
     * Checks if a move is valid.
     * @param move move to test
     * @return true if the move is valid
     * @throws IllegalArgumentException if the initial position is invalid, if the capture is invalid or
     * if the move symbol is invalid
     */
    fun isValidMove(move: Move): Boolean {
        require(isValidInitialPiece(move)) { "Invalid initial position." }
        require(isValidCapture(move)) { "Invalid capture." }

        return getPiece(move.from)!!.isValidMove(this, move) //TODO - Remove Double Bang (!!)
    }


    /**
     * Checks if the initial position of the move is valid,
     * by checking if the position is occupied and the piece is of the right type.
     * @param move move to test
     * @return if the the initial position is valid
     *
     * TODO(Use of positionIsOccupied prevents smart cast (piece != null). Currently using !! (bang operator).)
     */
    private fun isValidInitialPiece(move: Move) =
        isPositionOccupied(move.from) && getPiece(move.from)!!.symbol == move.symbol //TODO - Remove Double Bang (!!)


    /**
     * Checks if the capture in [move] is valid.
     * @param move move with the capture
     * @return true if the capture is valid
     */
    private fun isValidCapture(move: Move): Boolean {
        if (move.capture || isPositionOccupied(move.to)) {
            val captured = getPiece(move.to) ?: return false

            return captured.color != getPiece(move.from)!!.color //TODO - Remove Double Bang (!!)
        }
        return true
    }


    /**
     * Verifies if the king of given color is in check, returning how many pieces are attacking it.
     * @param color color of the king
     * @return number of pieces attacking the king
     */
    fun isKingInCheck(color: Color): Int {
        var checkCount = NOT_IN_CHECK

        var kingPosition: Position? = null

        //Find king
        for (rowIdx in matrix.indices) {
            for (colIdx in matrix[rowIdx].indices) {
                val piece = matrix[rowIdx][colIdx]

                val actualRow = BOARD_SIDE_LENGTH - rowIdx
                if (piece != null && piece.color == color && piece.symbol == 'K')
                    kingPosition = Position(FIRST_COL + colIdx, actualRow)
            }
        }

        require(kingPosition != null) { "King wasn't found!" }

        //For each piece, if its color is different from the king, check if it can capture the king
        for (rowIdx in matrix.indices) {
            for (colIdx in matrix[rowIdx].indices) {
                val piece = matrix[rowIdx][colIdx]

                val actualRow = BOARD_SIDE_LENGTH - rowIdx

                if (piece != null && piece.color == color.other()) {
                    if (isValidMove(
                            Move(
                                piece.symbol,
                                Position(FIRST_COL + colIdx, actualRow),
                                true,
                                kingPosition,
                                null
                            )
                        )
                    )
                        checkCount++
                }
            }
        }

        return checkCount
    }


    /**
     * Checks if a position is occupied by a piece.
     * @param position position to check
     * @return true if there's a piece in [position]
     */
    fun isPositionOccupied(position: Position) = getPiece(position) != null


    /**
     * Checks if the promotion is valid and, if it is returns the new promoted piece.
     * To promote, a piece needs to be a pawn and its next move has to be to the opposite player's first row.
     *
     * If no promote piece is specified, promote to queen by default.
     * @param piece piece to promote
     * @param toPos new piece position
     * @param promotion new piece type to promote
     * @return promoted piece
     * @throws Throwable if the promotion is invalid
     */
    private fun doPromotion(piece: Piece, toPos: Position, promotion: Char?): Piece {
        if (piece is Pawn &&
            (piece.color == Color.WHITE && toPos.row == BLACK_FIRST_ROW ||
                    piece.color == Color.BLACK && toPos.row == WHITE_FIRST_ROW)
        )
            return getPieceFromSymbol(promotion ?: 'Q', piece.color)
        else
            throw Throwable("You cannot get promoted.")
    }


    /**
     * String representation of the game board.
     * @return string representation of the chess board
     */
    override fun toString(): String {
        return matrix.joinToString("") { row ->
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
        val newBoard = Board(this.matrix.copyOf())
        repeat(BOARD_SIDE_LENGTH) {
            newBoard.matrix[it] = this.matrix[it].copyOf()
        }

        return newBoard
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

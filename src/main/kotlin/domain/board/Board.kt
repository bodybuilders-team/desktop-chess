package domain.board

import domain.move.*
import domain.pieces.*


/**
 * Represents the game board with the pieces.
 * @property matrix 2DMatrix with the pieces
 */
class Board(private val matrix: Matrix2D<Piece?> = getMatrix2DFromString(STRING_DEFAULT_BOARD)) {

    /**
     * Position of each board slot
     * @property col char in range ['a', 'h']
     * @property row int in range [1..8]
     */
    data class Position(val col: Char, val row: Int) {
        init {
            require(col in COLS_RANGE) { "Invalid Position: Column $col out of range ('a' .. 'h')." }
            require(row in ROWS_RANGE) { "Invalid Position: Row $row out of range (1 .. 8)." }
        }

        override fun toString(): String {
            return "$col$row"
        }
    }


    /**
     * Returns the piece in [pos]
     * @param pos position to get piece
     * @return piece in [pos]
     */
    fun getPiece(pos: Position) = matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL]


    /**
     * Sets [piece] in [pos]
     * @param pos position to set
     * @param piece piece to put in [pos]
     */
    fun setPiece(pos: Position, piece: Piece?) {
        matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL] = piece
    }


    /**
     * Removes piece from [pos]
     * @param pos position to remove the piece
     */
    fun removePiece(pos: Position) {
        setPiece(pos, null)
    }


    /**
     * Checks if a position is occupied by a piece.
     * @param position position to check
     * @return true if there's a piece in [position]
     */
    fun isPositionOccupied(position: Position) = getPiece(position) != null


    /**
     * Makes a move in the board.
     * @param move move to make
     * @return new board with the move made
     */
    fun makeMove(move: Move): Board {
        val fromPos = move.from
        val toPos = move.to
        val piece = getPiece(fromPos)

        requireNotNull(piece) { "Move.invoke() is not throwing IllegalMoveException in case of invalid from position." }

        val newBoard = this.copy()

        newBoard.removePiece(fromPos)

        newBoard.setPiece(
            toPos,
            if (move.promotion == null) piece
            else getPromotedPiece(piece, toPos, move.promotion, move.toString())
        )


        if (move.type == MoveType.CASTLE) {
            val rookPosition = getRookPosition(toPos)
            val rook = getPiece(rookPosition)

            newBoard.removePiece(rookPosition)
            newBoard.setPiece(getRookToPosition(toPos), rook)
        }
        else if (move.type == MoveType.EN_PASSANT)
            newBoard.removePiece(getEnPassantCapturedPawnPosition(toPos, piece))


        if (isKingInCheck(piece.army))
            throw IllegalMoveException(move.toString(), "Your King is in check! You must protect your King.")

        return newBoard
    }


    /**
     * Makes a move in the board.
     * @param moveInString move to make in string
     * @return new board with the move made
     */
    fun makeMove(moveInString: String): Board {
        return makeMove(Move(moveInString, this, emptyList()))
    }


    /**
     * String representation of the game board.
     * @return string representation of the chess board
     */
    override fun toString(): String {
        return matrix.joinToString("") { row ->
            row.map { piece ->
                piece?.toChar() ?: ' '
            }.joinToString("")
        }
    }


    /**
     * Returns a copy board, using the array function copyOf() for each array in the matrix.
     * @return copied board
     */
    fun copy(): Board {
        val newBoard = Board(this.matrix.copyOf())
        repeat(BOARD_SIDE_LENGTH) {
            newBoard.matrix[it] = this.matrix[it].copyOf()
        }

        return newBoard
    }
}
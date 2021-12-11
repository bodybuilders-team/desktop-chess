package domain.board

import domain.move.*
import domain.pieces.*


/**
 * Represents the game board with the pieces.
 * @property matrix 2DMatrix with the pieces
 */
data class Board(private val matrix: Matrix2D<Piece?> = getMatrix2DFromString(STRING_DEFAULT_BOARD)) {

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

        override fun toString() = "$col$row"
    }


    /**
     * Returns the piece in [pos]
     * @param pos position to get piece
     * @return piece in [pos]
     */
    fun getPiece(pos: Position) = matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL]


    /**
     * Place [piece] in [pos], returning new board
     * @param pos position to place piece in
     * @param piece piece to place in [pos]
     * @return new board with the piece place in the position
     */
    fun placePiece(pos: Position, piece: Piece): Board {
        val newBoard = this.copy()
        newBoard.matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL] = piece
        return newBoard
    }


    /**
     * Removes piece from [pos], returning new board
     * @param pos position to remove the piece
     * @return new board with the removed piece
     */
    fun removePiece(pos: Position): Board {
        val newBoard = this.copy()
        newBoard.matrix[BOARD_SIDE_LENGTH - pos.row][pos.col - FIRST_COL] = null
        return newBoard
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
        val piece = getPiece(move.from)
        requireNotNull(piece) { "Move is not validated! Invalid from position ${move.from}." }

        return removePiece(move.from)
            .placePiece(move.to, if (move.promotion == null) piece else getPieceFromSymbol(move.promotion, piece.army))
            .placePieceFromSpecialMoves(move, piece)
    }


    /**
     * String representation of the game board.
     * @return string representation of the chess board
     */
    override fun toString() =
        matrix.joinToString("") { row ->
            row.map { piece ->
                piece?.toChar() ?: ' '
            }.joinToString("")
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false

        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        return matrix.contentDeepHashCode()
    }
}

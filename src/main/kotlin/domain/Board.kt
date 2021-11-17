package domain

import domain.pieces.*


/**
 * Represents the game board with the pieces.
 * @property matrix 2DMatrix with the pieces
 */
class Board(private val matrix: Matrix2D<Piece?> = getMatrix2DFromString(STRING_BOARD)) {

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
     * Position of each board tile
     * @property col char in range ['a', 'h']
     * @property row int in range [1..8]
     */
    data class Position(val col: Char, val row: Int) {
        private val COLS_RANGE = 'a'..'h'
        private val ROWS_RANGE = 1..8

        init {
            require(col in COLS_RANGE) { "Invalid Position: Column $col out of range ('a' .. 'h')." }
            require(row in ROWS_RANGE) { "Invalid Position: Row $row out of range (1 .. 8)." }
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
     * @param moveInString piece move in string
     * @return new board with piece moved
     * @throws IllegalMoveException if there is no piece in the specified position or if the move is invalid
     */
    fun makeMove(moveInString: String): Board {
        val move = Move(moveInString, this)
        val fromPos = move.from
        val toPos = move.to
        val piece = getPiece(fromPos) ?: throw IllegalMoveException(moveInString, "No piece in the specified position.")

        val newBoard = this.copy()
        newBoard.removePiece(fromPos)

        newBoard.setPiece(
            toPos,
            if (move.promotion == null) piece
            else doPromotion(piece, toPos, move.promotion, move.toString())
        )

        return newBoard
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


    /**
     * Represents a pair between a piece and a position in the board.
     */
    data class Slot(val piece: Piece?, val position: Position)

    /**
     * Iterator of all pieces and respective positions.
     * @return an iterator of a piece-position pair.
     */
    operator fun iterator(): Iterator<Slot> {
        return object : Iterator<Slot> {
            var rowIdx = 0
            var colIdx = 0

            override fun hasNext(): Boolean {
                return rowIdx != BOARD_SIDE_LENGTH - 1 || colIdx != BOARD_SIDE_LENGTH - 1
            }

            override fun next(): Slot {
                val value = Slot(matrix[rowIdx][colIdx], Position(FIRST_COL + colIdx, BOARD_SIDE_LENGTH - rowIdx))
                if (++colIdx == BOARD_SIDE_LENGTH) {
                    rowIdx++
                    colIdx = 0
                }

                return value
            }
        }
    }
}

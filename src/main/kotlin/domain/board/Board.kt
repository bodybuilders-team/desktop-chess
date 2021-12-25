package domain.board

import domain.move.*
import domain.pieces.*


/**
 * Represents the game board with the pieces.
 * @property matrix list of pieces representing the board matrix with the pieces
 */
data class Board(private val matrix: List<Piece?>) {
    
    constructor(stringBoard: String = STRING_DEFAULT_BOARD) : this(getPiecesFromString(stringBoard))

    /**
     * Position of each board slot.
     * @property col char in range [COLS_RANGE]
     * @property row int in range [ROWS_RANGE]
     */
    data class Position(val col: Char, val row: Int) {
        init {
            require(col in COLS_RANGE) { "Invalid Position: Column $col out of range (${COLS_RANGE.first} .. ${COLS_RANGE.last})." }
            require(row in ROWS_RANGE) { "Invalid Position: Row $row out of range (${ROWS_RANGE.first} .. ${ROWS_RANGE.last})." }
        }

        override fun toString() = "$col$row"
    }

    /**
     * Returns the matrix index obtained from the position.
     * @return the matrix index obtained from the position
     */
    private fun Position.toIndex() = (BOARD_SIDE_LENGTH - row) * BOARD_SIDE_LENGTH + (col - FIRST_COL)


    /**
     * Returns the piece in [position].
     * @param position position to get piece of
     * @return piece in [position]
     */
    fun getPiece(position: Position) = matrix[position.toIndex()]


    /**
     * Place [piece] in [position], returning new board.
     * @param position position to place piece in
     * @param piece piece to place in [position]
     * @return new board with the [piece] placed in the [position]
     */
    fun placePiece(position: Position, piece: Piece) =
        Board(matrix.replace(position.toIndex(), piece))


    /**
     * Removes piece from [position], returning new board.
     * @param position position to remove the piece of
     * @return new board with the removed piece
     */
    fun removePiece(position: Position) =
        Board(matrix.replace(position.toIndex(), null))


    /**
     * Checks if a position is occupied by a piece.
     * @param position position to check
     * @return true if there's a piece in [position]
     */
    fun isPositionOccupied(position: Position) = getPiece(position) != null


    /**
     * Makes a move in the board. Expects the move to already be validated.
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
        matrix.map { piece ->
            piece?.toChar() ?: ' '
        }.joinToString("")
}

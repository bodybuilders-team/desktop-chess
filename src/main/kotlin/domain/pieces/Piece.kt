package domain.pieces

import domain.board.Board
import domain.move.IllegalMoveException
import domain.move.Move
import domain.move.MoveType


// Constants.
const val WHITE_PAWN_INITIAL_ROW = 2
const val BLACK_PAWN_INITIAL_ROW = 7
const val DOUBLE_MOVE = 2
const val ONE_MOVE = 1
const val NO_MOVE = 0


/**
 * Chess piece.
 * @property type piece type
 * @property army piece army (White or Black)
 */
interface Piece {
    val army: Army
    val type: PieceType

    /**
     * Returns character representation of the piece as seen in game
     * @return character representation of the piece
     */
    fun toChar(): Char =
        if (!isWhite()) type.symbol.lowercaseChar() else type.symbol

    /**
     * Checks if a move is possible regarding this specific piece type
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the move is possible
     */
    fun isValidMove(board: Board, move: Move): Boolean


    /**
     * Gets all available moves based on the piece type and the position in the board.
     * @param board board where the piece is situated
     * @param position position of the piece
     * @param previousMoves previous moves made
     * @return list of available moves
     */
    fun getAvailableMoves(board: Board, position: Board.Position, previousMoves: List<Move>): List<Move> =
        Move.searchMoves(
            Move(
                symbol = type.symbol,
                from = position,
                capture = false,
                to = Board.Position('a', 1),
                promotion = null,
                type = MoveType.NORMAL
            ),
            optionalFromCol = false,
            optionalFromRow = false,
            optionalToPos = true,
            board = board,
            previousMoves = previousMoves
        ).filter { move ->
            try {
                board.makeMove(move)
            } catch (err: IllegalMoveException) {
                if (err.message == "Your King is in check! You must protect your King.") return@filter false
            }
            true
        }
}


/**
 * Piece army.
 */
enum class Army {
    WHITE, BLACK;

    /**
     * Returns the other army.
     * @return other army
     */
    fun other() = if (this == WHITE) BLACK else WHITE
}


/**
 * Returns a Piece from its representative [symbol]
 * @param symbol char that represents the Piece type
 * @param army color of the piece
 * @return piece from its representative
 */
fun getPieceFromSymbol(symbol: Char, army: Army): Piece {
    return when (PieceType[symbol]) {
        PieceType.PAWN -> Pawn(army)
        PieceType.ROOK -> Rook(army)
        PieceType.KNIGHT -> Knight(army)
        PieceType.BISHOP -> Bishop(army)
        PieceType.KING -> King(army)
        PieceType.QUEEN -> Queen(army)
    }
}


/**
 * All valid piece types.
 * @param symbol char that represents the Piece type
 */
enum class PieceType(val symbol: Char) {
    PAWN('P'),
    ROOK('R'),
    KNIGHT('N'),
    BISHOP('B'),
    KING('K'),
    QUEEN('Q');

    override fun toString(): String = this.symbol.toString()

    companion object {
        /**
         * Gets the PieceType by its symbol.
         * @param symbol PieceType symbol to search
         * @return PieceType found by its symbol
         * @throws IllegalArgumentException if there's no type with the symbol '[symbol]'
         */
        operator fun get(symbol: Char) =
            requireNotNull(values().find { it.symbol == symbol }) { "No PieceType with the symbol \'$symbol\'" }
    }
}


/**
 * Checks if the piece army is White.
 * @return true if the piece army is white
 */
fun Piece.isWhite() = army == Army.WHITE

package pieces

import Board
import Move

// Constants.
const val WHITE_PAWN_INITIAL_ROW = 2
const val BLACK_PAWN_INITIAL_ROW = 7
const val DOUBLE_MOVE = 2
const val ONE_MOVE = 1
const val NO_MOVE = 0


/**
 * Piece color.
 */
enum class Color { WHITE, BLACK }


/**
 * Returns a Piece from its representative [symbol]
 * @param symbol char that represents the Piece type
 * @param color color of the piece
 * @return piece from its representative
 * @throws IllegalArgumentException if [symbol] is invalid
 */
fun getPieceFromSymbol(symbol: Char, color: Color): Piece {
    return when (symbol) {
        'R' -> Rook(color)
        'P' -> Pawn(color)
        'K' -> King(color)
        'Q' -> Queen(color)
        'B' -> Bishop(color)
        'N' -> Knight(color)
        else -> throw IllegalArgumentException("Invalid piece symbol.")
    }
}


/**
 * Chess piece.
 * @property symbol char that represents the piece type
 * @property color piece color (White or Black)
 */
interface Piece {
    val color: Color
    val symbol: Char

    /**
     * Checks if a move is possible
     * @param board board where the move will happen
     * @param move move to test
     * @return true if the move is possible
     */
    fun checkMove(board: Board, move: Move): Boolean

    /**
     * Checks if the initial position of the move is valid,
     * by checking if the position is occupied and the piece is of the right type.
     * @param board board where the move will happen
     * @param move move to test
     * @return if the the initial position is valid
     *
     * TODO(Use of positionIsOccupied prevents smart cast (piece != null). Currently using !! (bang operator).)
     */
    fun validInitialPiece(board: Board, move: Move) =
        board.positionIsOccupied(move.from) && move.symbol == this.symbol &&
                board.getPiece(move.from)!!.symbol == this.symbol
}


/**
 * Checks if the piece color ir White.
 * @return true if the piece is white
 */
fun Piece.isWhite() = color == Color.WHITE

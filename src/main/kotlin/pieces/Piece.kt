package pieces


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
}


/**
 * Checks if the piece color is White.
 * @return true if the piece is white
 */
fun Piece.isWhite() = color == Color.WHITE


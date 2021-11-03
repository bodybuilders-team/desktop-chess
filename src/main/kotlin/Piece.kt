import kotlin.math.abs

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
    fun checkMove(board: Board, move: Board.Move): Boolean
}


class Pawn(override val color: Color) : Piece {

    override val symbol = 'P'

    override fun checkMove(board: Board, move: Board.Move): Boolean {
        if (board.getPiece(move.from) == null) return false
        if (board.getPiece(move.from) !is Pawn) return false

        // Only vertically up or diagonally capturing


        // Vertical
        if (move.from.col == move.to.col) {
            if (move.from.row == if (color == Color.WHITE) 2 else 7) {
                if (move.to.row == move.from.row + if (color == Color.WHITE) 2 else -2) {
                    return board.getPiece(move.to) == null
                }
            }

            if (move.to.row == move.from.row + if (color == Color.WHITE) 1 else -1) {
                return board.getPiece(move.to) == null
            }
            return false
        }

        // Diagonal (only capture)
        if (abs(move.from.col - move.to.col) == 1) {
            if (move.to.row == move.from.row + if (color == Color.WHITE) 1 else -1) {
                return board.getPiece(move.to) != null
            }
            return false
        }

        return false
    }
}


class Rook(override val color: Color) : Piece {

    override val symbol = 'R'

    override fun checkMove(board: Board, move: Board.Move): Boolean {
        return move.from.row == move.to.row || move.from.col == move.to.col
    }
}


class King(override val color: Color) : Piece {

    override val symbol = 'K'

    override fun checkMove(board: Board, move: Board.Move): Boolean {
        return abs(move.from.row - move.to.row) == 1 || abs(move.from.col - move.to.col) == 1
    }
}


class Queen(override val color: Color) : Piece {

    override val symbol = 'Q'

    override fun checkMove(board: Board, move: Board.Move): Boolean {
        TODO("Not yet implemented")
    }
}


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    override fun checkMove(board: Board, move: Board.Move): Boolean {
        return move.from.row != move.to.row && move.from.col != move.to.col
    }
}


class Knight(override val color: Color) : Piece {

    override val symbol = 'N'

    override fun checkMove(board: Board, move: Board.Move): Boolean {
        TODO("Not yet implemented")
    }
}


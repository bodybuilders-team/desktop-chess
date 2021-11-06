import kotlin.math.abs

// Constants.
const val WHITE_PAWN_INITIAL_ROW = 2
const val BLACK_PAWN_INITIAL_ROW = 7
const val MAX_INITIAL_PAWN_MOVE = 2
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
}

/**
 * Checks if the piece color ir White.
 * @return true if the piece is white
 */
fun Piece.isWhite() = color == Color.WHITE

/**
 * Checks if the piece color ir Black.
 * @return true if the piece is black
 */
fun Piece.isBlack() = color == Color.BLACK


class Pawn(override val color: Color) : Piece {

    override val symbol = 'P'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Pawn) return false
        // Pawn -> Only vertically up or diagonally capturing


        // Vertical
        if (move.isVertical()) {
            // First pawn move can be 2 or 1

            val defaultMove = move.to.row == move.from.row + if (isWhite()) 1 else -1
            val isInInitialRow = move.from.row == if (isWhite())
                WHITE_PAWN_INITIAL_ROW else BLACK_PAWN_INITIAL_ROW
            val doubleMove =
                isInInitialRow && move.to.row == move.from.row + if (isWhite()) MAX_INITIAL_PAWN_MOVE else -MAX_INITIAL_PAWN_MOVE

            if (defaultMove || doubleMove) return board.getPiece(move.to) == null

            return false
        }

        // Diagonal (only capture)
        if (abs(move.from.col - move.to.col) == ONE_MOVE) {
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

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Rook) return false
        //return move.isHorizontal() || move.isVertical()
        var distance = if (move.isHorizontal()) move.to.col - move.from.col else move.to.row - move.from.row
        while (abs(distance) > 0) {
            if (move.isHorizontal() && board.getPiece(Board.Position(move.from.col + distance,move.to.row)) != null
                || move.isVertical() && board.getPiece(Board.Position(move.to.col,move.from.row + distance)) != null) {
                return false
            }
            if (distance > 0) distance-- else distance++
        }
        return true
    }
}


class King(override val color: Color) : Piece {

    override val symbol = 'K'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is King) return false
        if ((abs(move.from.row - move.to.row) == ONE_MOVE || abs(move.from.row - move.to.row) == NO_MOVE )
            && (abs(move.from.col - move.to.col) == ONE_MOVE || abs(move.from.col - move.to.col) == NO_MOVE)
            && !(abs(move.from.col - move.to.col) == NO_MOVE && abs(move.from.row - move.to.row) == NO_MOVE )) {
            return true
        }
        return false
    }
}


class Queen(override val color: Color) : Piece {

    override val symbol = 'Q'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Queen) return false
        return move.isDiagonal() || move.isHorizontal() || move.isVertical()
    }
}


class Bishop(override val color: Color) : Piece {

    override val symbol = 'B'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Bishop) return false
        return move.isDiagonal()
    }
}


class Knight(override val color: Color) : Piece {

    override val symbol = 'N'

    override fun checkMove(board: Board, move: Move): Boolean {
        if (board.getPiece(move.from) == null || board.getPiece(move.from) !is Knight) return false
        return abs(move.from.row - move.to.row) == ONE_MOVE && abs(move.from.col - move.to.col) == 2 ||
                abs(move.from.col - move.to.col) == ONE_MOVE && abs(move.from.row - move.to.row) == 2
    }
}


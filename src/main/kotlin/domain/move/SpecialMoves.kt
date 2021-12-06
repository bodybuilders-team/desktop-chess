package domain.move

import domain.board.*
import domain.board.Board.*
import domain.pieces.*

const val INITIAL_KING_COL = 'e'

/**
 * Checks if an en passant move is valid/possible.
 * @param piece piece to check if an en passant move is valid/possible
 * @param board board where the possible en passant move will happen
 * @param previousMoves previous moves made
 * @return true if an en passant move is valid/possible
 */
fun Move.isValidEnPassant(piece: Piece, board: Board, previousMoves: List<Move>) =
    isEnPassantPossible(piece, previousMoves) &&
            piece is Pawn &&
            piece.isValidEnPassant(board, this)


/**
 * Checks if the last move is valid to do en passant move immediately next.
 * @param piece piece that makes en passant
 * @param previousMoves previous game moves
 * @return true if the last move is valid
 */
fun Move.isEnPassantPossible(piece: Piece, previousMoves: List<Move>) =
    previousMoves.isNotEmpty() && previousMoves.last().toString() in listOf(
        "P${from.col - 1}${from.row + 2 * if (piece.isWhite()) 1 else -1}${from.col - 1}${from.row}",
        "P${from.col + 1}${from.row + 2 * if (piece.isWhite()) 1 else -1}${from.col + 1}${from.row}"
    )


/**
 * Checks if a castle move is valid/possible.
 * @param piece piece to check if a castle move is valid/possible
 * @param board board where the possible castle move will happen
 * @param previousMoves previous moves made
 * @return true if a castle move is valid/possible
 */
fun Move.isValidCastle(piece: Piece, board: Board, previousMoves: List<Move>): Boolean {
    return isCastlePossible(piece, previousMoves) &&
            (piece is King && piece.isValidCastle(board, this) || piece is Rook && piece.isValidCastle(board, this))
}


/**
 * Checks if a castle move is possible.
 * @param piece piece that makes castle move
 * @param previousMoves previous game moves
 * @return true if the castle move is possible
 */
fun isCastlePossible(piece: Piece, previousMoves: List<Move>) =
    previousMoves.none { move ->
        piece.isWhite() && move.from !in listOf(Position('a', 1), Position('h', 1), Position('e', 1)) ||
        !piece.isWhite() && move.from !in listOf(Position('a', 8), Position('h', 8), Position('e', 8))
    }


object Castle {
    /**
     * Gets the from position of the rook in a castle move.
     * @param kingToPos king position after the castle move
     * @return position of the rook
     */
    fun getRookPosition(kingToPos: Position) =
        Position(
            col = if (kingToPos.col == 'g') 'h' else 'a',
            row = kingToPos.row
        )

    /**
     * Gets the to position of the rook in a castle move.
     * @param kingToPos king position after the castle move
     * @return position of the rook
     */
    fun getRookToPosition(kingToPos: Position) =
        Position(
            col = if (kingToPos.col == 'g') 'f' else 'd',
            row = kingToPos.row
        )

    /**
     * Gets the from position of the king in a castle move.
     * @param rookToPos rook position after the castle move
     * @return position of the king
     */
    fun getKingPosition(rookToPos: Position) =
        Position(
            col = INITIAL_KING_COL,
            row = rookToPos.row
        )

    /**
     * Gets the to position of the king in a castle move.
     * @param rookToPos rook position after the castle move
     * @return position of the king
     */
    fun getKingToPosition(rookToPos: Position) =
        Position(
            col = if (rookToPos.col == 'f') 'g' else 'c',
            row = rookToPos.row
        )
}


/**
 * Gets the position of the captured pawn from the en passant move.
 * @param attackerToPos position of the attacker after the move
 * @param attackingPiece attacker
 * @return position of the captured pawn
 */
fun getEnPassantCapturedPawnPosition(attackerToPos: Position, attackingPiece: Piece) =
    Position(
        col = attackerToPos.col,
        row = attackerToPos.row + if (attackingPiece.isWhite()) -1 else +1
    )



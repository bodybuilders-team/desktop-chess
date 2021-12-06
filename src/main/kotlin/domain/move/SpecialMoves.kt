package domain.move

import domain.board.Board
import domain.pieces.*


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
 * @return true if the last move is valid.
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
fun Move.isValidCastle(piece: Piece, board: Board, previousMoves: List<Move>): Boolean =
    false
    //TODO("To be implemented")

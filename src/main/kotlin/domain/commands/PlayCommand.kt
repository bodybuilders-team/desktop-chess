package domain.commands

import domain.*
import storage.GameState


/**
 * Executes a move command if it corresponds to the rules
 * @param db database where the moves are stored
 * @param chess current chess game
 * @throws IllegalArgumentException if game has not been initialized yet
 * @throws IllegalArgumentException if it is not the user's turn
 * @throws IllegalArgumentException if move to be made not specified
 */
class PlayCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        require(!chess.isLogging()) { "Can't play without a game: try open or join commands." }
        require(chess.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        require(chess.state != SessionState.ENDED) { "Game ended. Can't play any more moves." }

        requireNotNull(parameter) { "Missing move." }

        val move = Move(parameter, chess.board, chess.moves)

        val piece = chess.board.getPiece(move.from)
        requireNotNull(piece) { "Move.invoke() is not throwing IllegalMoveException in case of invalid from position." }

        if (piece.army == chess.army.other())
            throw IllegalMoveException(move.toString(), "You cannot move an opponent's piece.")

        val newBoard = chess.board.makeMove(move)
        db.postMove(chess.name, move)
        
        val inCheckMate = newBoard.isKingInCheckMate(chess.army.other())
        val inStaleMate = newBoard.isKingInStaleMate(chess.army.other())

        return Result.success(
            chess.copy(
                state = if (inCheckMate || inStaleMate) SessionState.ENDED else SessionState.WAITING_FOR_OPPONENT,
                board = newBoard,
                moves = chess.moves + move,
                currentCheck = when {
                    newBoard.isKingInCheck(chess.army.other()) -> Check.CHECK
                    inCheckMate -> Check.CHECK_MATE
                    inStaleMate -> Check.STALE_MATE
                    else -> Check.NO_CHECK
                }
            )
        )
    }
}

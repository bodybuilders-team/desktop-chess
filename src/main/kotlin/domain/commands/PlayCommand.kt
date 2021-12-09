package domain.commands

import domain.*
import domain.board.*
import domain.move.*
import storage.GameState


/**
 * Executes a move command if it corresponds to the rules
 * @param db database where the moves are stored
 * @param chess current chess game
 * @throws CommandException if game has not been initialized yet
 * @throws CommandException if it is not the user's turn
 * @throws CommandException if the game ended
 * @throws CommandException if move to be made not specified
 * @throws IllegalArgumentException if there's no piece in the specified from position in move
 * @throws IllegalMoveException if the user tried to play with an opponent's piece
 */
class PlayCommand(private val db: GameState, private val chess: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(!chess.isLogging()) { "Can't play without a game: try open or join commands." }
        cmdRequire(chess.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        cmdRequire(chess.state != SessionState.ENDED) { "Game ended. Can't play any more moves." }
        cmdRequireNotNull(parameter) { "Missing move." }

        val move = Move.getValidatedMove(parameter, chess.board, chess.moves)

        val piece = chess.board.getPiece(move.from)
        requireNotNull(piece) { "Move.getValidatedMove() is not throwing IllegalMoveException in case of invalid from position." }

        if (piece.army == chess.army.other())
            throw IllegalMoveException(move.toString(), "You cannot move an opponent's piece.")

        val newBoard = chess.board.makeMove(move)
        db.postMove(chess.name, move)

        val moves = chess.moves + move
        
        val inCheckMate = newBoard.isKingInCheckMate(chess.army.other())
        val inStaleMate = newBoard.isKingInStaleMate(chess.army.other(), moves)

        return Result.success(
            chess.copy(
                state = if (inCheckMate || inStaleMate) SessionState.ENDED else SessionState.WAITING_FOR_OPPONENT,
                board = newBoard,
                moves = moves,
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

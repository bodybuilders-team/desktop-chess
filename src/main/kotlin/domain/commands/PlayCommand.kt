package domain.commands

import domain.*
import domain.board.*
import domain.move.*
import storage.GameState


/**
 * Executes a move command if it corresponds to the rules
 * @param db database where the moves are stored
 * @param session current session
 * @throws CommandException if game has not been initialized yet
 * @throws CommandException if it is not the user's turn
 * @throws CommandException if the game ended
 * @throws CommandException if move to be made not specified
 * @throws IllegalArgumentException if there's no piece in the specified from position in move
 * @throws IllegalMoveException if the user tried to play with an opponent's piece
 */
class PlayCommand(private val db: GameState, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(!session.isLogging()) { "Can't play without a game: try open or join commands." }
        cmdRequire(session.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        cmdRequire(session.state != SessionState.ENDED) { "Game ended. Can't play any more moves." }
        cmdRequireNotNull(parameter) { "Missing move." }

        val move = Move.validated(parameter, session.game.board, session.game.moves)

        val piece = session.game.board.getPiece(move.from)
        requireNotNull(piece) { "Move.getValidatedMove() is not throwing IllegalMoveException in case of invalid from position." }

        if (piece.army == session.army.other())
            throw IllegalMoveException(move.toString(), "You cannot move an opponent's piece.")

        val newBoard = session.game.board.makeMove(move)
        db.postMove(session.name, move)

        val moves = session.game.moves + move
        
        val inCheckMate = newBoard.isKingInCheckMate(session.army.other())
        val inStaleMate = newBoard.isKingInStaleMate(session.army.other(), moves)

        return Result.success(
            session.copy(
                state = if (inCheckMate || inStaleMate) SessionState.ENDED else SessionState.WAITING_FOR_OPPONENT,
                game =  Game(newBoard, moves),
                currentCheck = when {
                    newBoard.isKingInCheck(session.army.other()) -> Check.CHECK
                    inCheckMate -> Check.CHECK_MATE
                    inStaleMate -> Check.STALE_MATE
                    else -> Check.NO_CHECK
                }
            )
        )
    }
}

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
 * @throws CommandException if it is not the player's turn
 * @throws CommandException if the game ended
 * @throws CommandException if move to be made not specified
 * @throws IllegalArgumentException if it's not this army's turn but SessionState is YOUR_TURN (database modified?)
 * @throws IllegalArgumentException if there's no piece in the specified from position in move
 * @throws IllegalMoveException if the player tried to play with an opponent's piece
 */
class PlayCommand(private val db: GameState, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(!session.isLogging()) { "Can't play without a game: try open or join commands." }
        cmdRequire(session.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        cmdRequire(session.state != SessionState.ENDED) { "Game ended. Can't play any more moves." }
        cmdRequireNotNull(parameter) { "Missing move." }
        require(currentTurnArmy(session.game.moves) == session.army) { "It's not this army's turn! Session army is different from the current turn army." }

        val move = Move.validated(parameter, session.game)

        val piece = session.game.board.getPiece(move.from)
        requireNotNull(piece) { "Move.validated() is not throwing IllegalMoveException in case of invalid from position." }

        if (piece.army == session.army.other())
            throw IllegalMoveException(move.toString(), "You cannot move an opponent's piece.")
        
        val game = session.game.makeMove(move)

        db.postMove(session.name, move)

        val inCheckMate = game.board.isKingInCheckMate(session.army.other())
        val inStaleMate = game.isKingInStaleMate(session.army.other())

        return Result.success(
            session.copy(
                state = if (inCheckMate || inStaleMate) SessionState.ENDED else SessionState.WAITING_FOR_OPPONENT,
                game = game,
                currentCheck = when {
                    inCheckMate -> Check.CHECK_MATE
                    inStaleMate -> Check.STALE_MATE
                    game.board.isKingInCheck(session.army.other()) -> Check.CHECK
                    else -> Check.NO_CHECK
                }
            )
        )
    }
}

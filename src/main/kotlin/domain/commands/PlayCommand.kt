package domain.commands

import domain.*
import domain.game.*
import domain.move.*
import storage.GameState

//TODO - Para todos os files do projeto - Ver se os "require" devem ser "check"
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
        require(session.game.currentTurnArmy == session.army) { "It's not this army's turn! Session army is different from the current turn army." }

        val move = Move.validated(parameter, session.game)

        val piece = session.game.board.getPiece(move.from)
        requireNotNull(piece) { "Move.validated() is not throwing IllegalMoveException in case of invalid from position." }
        
        val game = session.game.makeMove(move)

        db.postMove(session.name, move)

        return Result.success(
            session.copy(
                state = if (game.ended()) SessionState.ENDED else SessionState.WAITING_FOR_OPPONENT,
                game = game,
                gameState = game.getState()
            )
        )
    }
}

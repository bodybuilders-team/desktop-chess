package domain.commands

import domain.*
import domain.game.*
import domain.move.*
import storage.GameStorage


/**
 * Executes a move command if it corresponds to the rules
 * @param db database where the moves are stored
 * @param session current session
 * @throws CommandException if game has not been initialized yet
 * @throws CommandException if it is not the player's turn
 * @throws CommandException if the game ended
 * @throws CommandException if move to be made not specified
 * @throws IllegalArgumentException if it's not this army's turn but SessionState is YOUR_TURN (database modified?)
 */
class PlayCommand(private val db: GameStorage, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(session.isNotLogging()) { "Can't play without a game: try open or join commands." }
        cmdRequire(session.state != SessionState.WAITING_FOR_OPPONENT) { "Wait for your turn: try refresh command." }
        cmdRequire(session.state != SessionState.ENDED) { "Game ended. Can't play any more moves." }
        cmdRequireNotNull(parameter) { "Missing move." }

        val move = Move.validated(parameter, session.game)

        val game = session.game.makeMove(move)

        db.postMove(session.name, move)

        return Result.success(
            session.copy(
                state =
                when {
                    game.ended()                                -> SessionState.ENDED
                    session.state == SessionState.SINGLE_PLAYER -> SessionState.SINGLE_PLAYER
                    else                                        -> SessionState.WAITING_FOR_OPPONENT
                },
                game = game
            )
        )
    }
}

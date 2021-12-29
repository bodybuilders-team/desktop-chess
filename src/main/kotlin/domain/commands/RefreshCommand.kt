package domain.commands

import domain.*
import domain.game.*
import storage.GameStorage


/**
 * Updates the state of the game.
 * @param session current chess game
 * @param db database where the moves are stored
 * @throws CommandException if game has not been initialized yet
 * @throws CommandException if it is not the user's turn
 * @throws CommandException if the game ended
 */
class RefreshCommand(private val db: GameStorage, private val session: Session) : Command {

    override fun execute(parameter: String?): Result<Session> {
        cmdRequire(session.isNotLogging()) { "Can't refresh without a game: try open or join commands." }
        cmdRequire(session.state != SessionState.YOUR_TURN) { "It's your turn: try play." }
        cmdRequire(session.state != SessionState.ENDED) { "Game ended. There aren't any new moves." }

        val moves = db.getAllMoves(session.name)
        val yourTurn = session.game.moves.size < moves.size
        
        val game = if (yourTurn) session.game.makeMove(moves.last())
        else session.game

        return Result.success(
            session.copy(
                state =
                when {
                    game.ended()    -> SessionState.ENDED
                    yourTurn        -> SessionState.YOUR_TURN
                    else            -> SessionState.WAITING_FOR_OPPONENT
                },
                game = game
            )
        )
    }
}

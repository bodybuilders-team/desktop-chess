package domain

import domain.game.*
import domain.move.*
import domain.pieces.Army


/**
 * A game session.
 * @property name session name
 * @property state current session state
 * @property army session army
 * @property game game with current board and list of previously played moves
 */
data class Session(
    val name: String,
    val state: SessionState,
    val army: Army,
    val game: Game
)


// Session Constants
const val NO_NAME = ""


/**
 * Different states the session can be in.
 */
enum class SessionState {
    LOGGING,
    YOUR_TURN,
    WAITING_FOR_OPPONENT,
    ENDED
}


/**
 * Checks if a session is in logging state.
 * @return true if a session is in logging state.
 */
fun Session.isLogging() = state == SessionState.LOGGING


/**
 * Returns the state of a session, given its game and army.
 * @param game session game
 * @param army session army
 * @return session state
 */
fun getSessionState(game: Game, army: Army) =
    when {
        game.ended()                    -> SessionState.ENDED
        game.armyToPlay == army         -> SessionState.YOUR_TURN
        else                            -> SessionState.WAITING_FOR_OPPONENT
    }


/**
 * Gets a session of an opening game.
 * @param gameName name of the game
 * @param moves moves of the game
 * @param army session army
 * @return session of the opened game
 */
fun getOpeningBoardSession(gameName: String, moves: List<Move>, army: Army): Session {
    val game = gameFromMoves(moves)

    return Session(
        name = gameName,
        state = getSessionState(game, army),
        army = army,
        game = game
    )
}

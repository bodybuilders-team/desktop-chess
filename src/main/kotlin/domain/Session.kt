package domain

import domain.board.*
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
    val game: Game,
    val currentCheck: Check
)


// Session Constants
const val NO_NAME = ""


/**
 * Game state.
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
 * Gets the current state of the session.
 * @param game game being played
 * @param army session army
 * @return current state of the session
 */
fun getCurrentState(game: Game, army: Army) = when {
    game.isInMate()                       -> SessionState.ENDED
    currentTurnArmy(game.moves) == army   -> SessionState.YOUR_TURN
    else                                  -> SessionState.WAITING_FOR_OPPONENT
}


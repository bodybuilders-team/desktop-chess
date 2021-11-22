package domain

import domain.pieces.Army


/**
 * A game session.
 * @property name session name
 * @property state current session state
 * @property army session army
 * @property board current board
 * @property moves list of previously played moves
 */
data class Session(
    val name: String,
    val state: SessionState,
    val army: Army,
    val board: Board,
    val moves: List<Move>
)


/**
 * Game state.
 */
enum class SessionState {
    LOGGING,
    YOUR_TURN,
    WAITING_FOR_OPPONENT
}


/**
 * Checks if a session is in logging state.
 * @return true if a session is in logging state.
 */
fun Session.isLogging() = state == SessionState.LOGGING
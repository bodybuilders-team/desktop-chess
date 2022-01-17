package domain

import domain.game.*
import domain.move.*
import domain.pieces.Army


/**
 * A game session.
 * @property name session name
 * @property state current session state
 * @property game game with current board and list of previously played moves
 */
data class Session(
    val name: String,
    val state: SessionState,
    val game: Game
)


// Session Constants
const val NO_NAME = ""
val INITIAL_SESSION = Session(
    name = NO_NAME,
    state = SessionState.LOGGING,
    game = INITIAL_GAME
)


/**
 * Different states the session can be in.
 */
enum class SessionState {
    LOGGING,
    YOUR_TURN,
    WAITING_FOR_OPPONENT,
    ENDED,
    SINGLE_PLAYER
}


/**
 * Checks if a session is in logging state.
 * @return true if a session is in logging state.
 */
fun Session.isLogging() = state == SessionState.LOGGING


/**
 * Checks if a session is not in logging state.
 * @return true if a session is not in logging state.
 */
fun Session.isNotLogging() = state != SessionState.LOGGING


/**
 * Checks if a session is playable.
 * @return true if a session is playable.
 */
fun Session.isPlayable() = state in listOf(SessionState.YOUR_TURN, SessionState.SINGLE_PLAYER)


/**
 * Checks if a session state is logging.
 * @return true if a session state is logging.
 */
fun SessionState.isLogging() = this == SessionState.LOGGING


/**
 * Checks if a session state is not logging.
 * @return true if a session state is not logging.
 */
fun SessionState.isNotLogging() = !this.isLogging()


/**
 * Checks if a session state is waiting.
 * @return true if a session state is not waiting.
 */
fun SessionState.isWaiting() = this == SessionState.WAITING_FOR_OPPONENT


/**
 * Returns the state of a session, given its game and army.
 * @param game session game
 * @param army session army
 * @return session state
 */
fun getSessionState(game: Game, army: Army) =
    when {
        game.ended() -> SessionState.ENDED
        game.armyToPlay == army -> SessionState.YOUR_TURN
        else -> SessionState.WAITING_FOR_OPPONENT
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
        game = game
    )
}

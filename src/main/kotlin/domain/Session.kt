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
 * Gets a session of an opening game.
 * @param gameName name of the game
 * @param moves moves of the game
 * @param army session army
 * @return session of the opened game
 */
fun getOpeningBoardSession(gameName: String, moves: List<Move>, army: Army): Session {
    val game = gameFromMoves(moves)

    val inCheckMate = game.board.isKingInCheckMate(Army.WHITE) || game.board.isKingInCheckMate(Army.BLACK)
    val inStaleMate = game.isKingInStaleMate(Army.WHITE) || game.isKingInStaleMate(Army.BLACK)

    val state = when {
        inCheckMate || inStaleMate             -> SessionState.ENDED
        currentTurnArmy(moves) == army         -> SessionState.YOUR_TURN
        else                                   -> SessionState.WAITING_FOR_OPPONENT
    }

    return Session(
        name = gameName,
        state = state,
        army = army,
        game = game,
        currentCheck = when {
            inCheckMate -> Check.CHECKMATE
            inStaleMate -> Check.STALEMATE
            (state == SessionState.YOUR_TURN && game.board.isKingInCheck(army)) -> Check.CHECK
            else -> Check.NO_CHECK
        }
    )
}

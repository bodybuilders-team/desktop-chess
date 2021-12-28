package ui.compose.app

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.NO_NAME
import domain.Session
import domain.SessionState
import domain.commands.RefreshCommand
import domain.game.gameFromMoves
import domain.pieces.Army
import kotlinx.coroutines.delay
import storage.GameStorage
import ui.compose.*
import ui.compose.board.*


// Constants
val WINDOW_PADDING = 32.dp
val WINDOW_WIDTH = BOARD_WIDTH + MOVES_WIDTH + WINDOW_PADDING * 4
val WINDOW_HEIGHT = BOARD_HEIGHT + WINDOW_PADDING * 4 + 60.dp + GAME_INFO_HEIGHT + COLS_VIEW_HEIGHT
val BACKGROUND_COLOR = Color(0xFF2B2B2B)
const val REFRESH_TIME = 10L
const val REFRESH_TIME_DELAY = 100L

val INITIAL_GAME = gameFromMoves()

val INITIAL_SESSION = Session(
    name = NO_NAME,
    state = SessionState.LOGGING,
    army = Army.WHITE,
    game = INITIAL_GAME
)


/**
 * App options
 * @property singlePlayer when true, single player mode is on
 * @property targetsOn when true, the available move targets are on
 * @property closeGame when true, the current game is closed
 * @property exitApp when true, the app is closed
 */
data class Options(
    val singlePlayer: MutableState<Boolean>,
    val targetsOn: MutableState<Boolean>,
    val closeGame: MutableState<Boolean>,
    val exitApp: MutableState<Boolean>
)


/**
 * Timer used to refresh the app session.
 * @param session app session to be refreshed
 * @param dataBase database where the games are stored
 */
@Composable
fun Timer(session: MutableState<Session>, dataBase: GameStorage) {
    if (session.value.state == SessionState.WAITING_FOR_OPPONENT) {
        val timerValue = remember { mutableStateOf(REFRESH_TIME) }

        LaunchedEffect(key1 = timerValue.value, key2 = session.value) {
            if (timerValue.value <= 0) {
                timerValue.value = REFRESH_TIME
                println("refresh")
                session.value = RefreshCommand(dataBase, session.value).execute(null).getOrThrow()
            } else {
                delay(REFRESH_TIME_DELAY)
                println(timerValue.value)
                timerValue.value--
            }
        }
    }
}

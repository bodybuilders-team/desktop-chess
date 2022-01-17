package ui.compose

import androidx.compose.runtime.*
import domain.Session
import domain.SessionState
import domain.commands.RefreshCommand
import kotlinx.coroutines.delay
import storage.GameStorage


// Constants
const val REFRESH_TIME = 10L
const val REFRESH_TIME_DELAY = 100L

// TODO: 17/01/2022 Arranjar/Ver Refresh timer
/**
 * Timer used to refresh the app session.
 * @param session app session to be refreshed
 * @param dataBase database where the games are stored
 */
@Composable
fun RefreshTimer(session: MutableState<Session>, dataBase: GameStorage) {
    if (session.value.state == SessionState.WAITING_FOR_OPPONENT) {
        val timerValue = remember { mutableStateOf(REFRESH_TIME) }

        LaunchedEffect(key1 = timerValue.value, key2 = session.value) {
            if (timerValue.value <= 0) {
                timerValue.value = REFRESH_TIME
                session.value = RefreshCommand(dataBase, session.value).execute(null).getOrThrow()
            } else {
                delay(REFRESH_TIME_DELAY)
                timerValue.value--
            }
        }
    }
}

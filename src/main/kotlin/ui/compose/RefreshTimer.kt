package ui.compose

import androidx.compose.runtime.*
import domain.Session
import domain.commands.RefreshCommand
import domain.isWaiting
import kotlinx.coroutines.delay
import storage.GameStorage


// Constants
private const val REFRESH_TIME = 10L
private const val REFRESH_TIME_DELAY = 100L

// TODO: 17/01/2022 Arranjar/Ver Refresh timer
/**
 * Timer used to refresh the app session.
 * @param session app session to be refreshed
 * @param dataBase database where the games are stored
 */
@Composable
fun RefreshTimer(session: MutableState<Session>, dataBase: GameStorage) {
    if (session.value.isWaiting()) {
        val timer = remember { mutableStateOf(REFRESH_TIME) }

        LaunchedEffect(key1 = timer.value, key2 = session.value) {
            if (timer.value <= 0) {
                timer.value = REFRESH_TIME
                session.value = RefreshCommand(dataBase, session.value).execute(null).getOrThrow()
            } else {
                delay(REFRESH_TIME_DELAY)
                timer.value--
            }
        }
    }
}

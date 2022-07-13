@file:Suppress("FunctionName")

package ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

// Constants
private const val REFRESH_TIME_DELAY = 1_000L

/**
 * Timer used to refresh the app session.
 * @param onRefreshRequest callback to be executed when occurs a refresh game request
 */
@Composable
fun RefreshTimer(onRefreshRequest: () -> Unit) {
    LaunchedEffect(key1 = true) {
        while (true) {
            onRefreshRequest()
            delay(REFRESH_TIME_DELAY)
        }
    }
}

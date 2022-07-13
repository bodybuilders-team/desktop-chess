@file:Suppress("FunctionName")

package ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import domain.Session
import domain.isLogging
import domain.isNotLogging
import domain.isWaiting

/**
 * Window Menu Bar.
 * @param session current session state
 * @param appOptions app options
 * @param onRefreshGameRequest callback to be executed when occurs a refresh game request
 * @param onCloseGameRequest callback to be executed when occurs a close game request
 * @param onShowTargetsChange callback to be executed when occurs a change in the show-targets checkbox
 * @param onSinglePlayerChange callback to be executed when occurs a change in the single-player checkbox
 */
@Composable
fun FrameWindowScope.MenuBar(
    session: Session,
    appOptions: AppOptions,
    onRefreshGameRequest: () -> Unit,
    onCloseGameRequest: () -> Unit,
    onShowTargetsChange: (Boolean) -> Unit,
    onSinglePlayerChange: (Boolean) -> Unit
) {
    MenuBar {
        Menu("File") {
            Item(
                text = "Refresh game",
                onClick = onRefreshGameRequest,
                enabled = session.isWaiting()
            )
            Item(
                text = "Close game",
                onClick = onCloseGameRequest,
                enabled = session.isNotLogging()
            )
        }
        Menu("Options") {
            CheckboxItem(
                text = "Show targets",
                checked = appOptions.targetsOn.value,
                onCheckedChange = onShowTargetsChange
            )

            CheckboxItem(
                text = "Single player",
                checked = appOptions.singlePlayer.value,
                onCheckedChange = onSinglePlayerChange,
                enabled = session.isLogging()
            )
        }
    }
}

@file:Suppress("FunctionName")

package ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import domain.INITIAL_SESSION
import domain.commands.RefreshCommand
import kotlinx.coroutines.launch
import storage.GameStorage

// Constants
private const val MAIN_WINDOW_TITLE = "Desktop Chess by Nyck, Jesus and Santos"
private const val MAIN_WINDOW_ICON = "chess_icon.png"

private val MAIN_WINDOW_WIDTH = APP_WIDTH + APP_PADDING * 2
private val MENU_BAR_HEIGHT = 60.dp
private val MAIN_WINDOW_HEIGHT = APP_HEIGHT + APP_PADDING * 2 + MENU_BAR_HEIGHT

/**
 * Application main window.
 * @param gameStorage where the games are stored
 * @param onCloseRequest callback to be executed when occurs a close window request (through closing the window directly,
 * or through clicking the "Cancel" button on the menu)
 */
@Composable
fun MainWindow(gameStorage: GameStorage, onCloseRequest: () -> Unit) {
    Window(
        title = MAIN_WINDOW_TITLE,
        state = WindowState(
            size = DpSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT),
            position = WindowPosition(Alignment.Center)
        ),
        onCloseRequest = onCloseRequest,
        icon = painterResource(MAIN_WINDOW_ICON),
        resizable = false
    ) {
        val session = remember { mutableStateOf(INITIAL_SESSION) }

        val appOptions = AppOptions(
            singlePlayer = remember { mutableStateOf(true) },
            targetsOn = remember { mutableStateOf(true) }
        )

        val coroutineScope = rememberCoroutineScope()

        MenuBar(
            session.value,
            appOptions,
            onRefreshGameRequest = {
                coroutineScope.launch {
                    session.value = RefreshCommand(gameStorage, session.value).execute(null).getOrThrow()
                }
            },
            onCloseGameRequest = { session.value = INITIAL_SESSION },
            onShowTargetsChange = { appOptions.targetsOn.value = it },
            onSinglePlayerChange = { appOptions.singlePlayer.value = it }
        )

        App(session, gameStorage, appOptions, onCloseRequest)
    }
}

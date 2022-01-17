package ui.compose

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import domain.INITIAL_SESSION
import domain.commands.RefreshCommand
import storage.GameStorage


// Constants
private const val MAIN_WINDOW_TITLE = "Desktop Chess by Nyck, Jesus and Santos"
private const val MAIN_WINDOW_ICON = "chess_icon.png"

private val MAIN_WINDOW_WIDTH = APP_WIDTH + APP_PADDING * 2
private val MENU_BAR_HEIGHT = 60.dp
private val MAIN_WINDOW_HEIGHT = APP_HEIGHT + APP_PADDING * 2 + MENU_BAR_HEIGHT


/**
 * Application main window.
 * @param appOptions application options
 * @param dataBase database where the games are stored
 */
@Composable
fun MainWindow(appOptions: AppOptions, dataBase: GameStorage) {
    Window(
        title = MAIN_WINDOW_TITLE,
        state = WindowState(
            size = DpSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT),
            position = WindowPosition(Alignment.Center)
        ),
        onCloseRequest = { appOptions.exitApp.value = true },
        icon = painterResource(MAIN_WINDOW_ICON),
        resizable = false
    ) {
        val session = remember { mutableStateOf(INITIAL_SESSION) }

        MenuBar(
            session.value.state,
            appOptions,
            onRefreshGameRequest =  { session.value = RefreshCommand(dataBase, session.value).execute(null).getOrThrow() },
            onCloseGameRequest =    { session.value = INITIAL_SESSION },
            onShowTargetsChange =   { appOptions.targetsOn.value = it },
            onSinglePlayerChange =  { appOptions.singlePlayer.value = it }
        )

        App(session, dataBase, appOptions)
    }
}

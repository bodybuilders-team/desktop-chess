@file:Suppress("FunctionName")

package ui.compose.rightPanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.Session
import domain.SessionState
import domain.commands.JoinCommand
import domain.commands.OpenCommand
import domain.isLogging
import domain.isPlayable
import storage.GameStorage
import ui.compose.AppOptions
import ui.compose.WINDOW_SCALE
import kotlin.collections.contains
import kotlin.collections.listOf

// Constants
private val MENU_TITLE_SIZE = 40.sp * WINDOW_SCALE
private val MENU_BUTTON_WIDTH = 200.dp * WINDOW_SCALE
private val MENU_BUTTON_HEIGHT = 60.dp * WINDOW_SCALE
private val MENU_BUTTON_PADDING = 10.dp * WINDOW_SCALE
private val MENU_IMAGE_PADDING = 32.dp * WINDOW_SCALE
private val SPACE_BETWEEN_BUTTONS = 32.dp * WINDOW_SCALE
private val MENU_BUTTON_MODIFIER = Modifier.size(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT).padding(MENU_BUTTON_PADDING)
private val MENU_OPTION_VIEW_WIDTH = 400.dp
private val MENU_OPTION_VIEW_HEIGHT = 200.dp

private const val MENU_IMAGE = "menu_image.png"

/**
 * Composable used to display a column with the menu.
 * @param onCommandSelected function to be executed when the menu command is selected
 */
@Composable
fun MenuView(onCommandSelected: (MenuCommand?) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.size(RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT)
    ) {
        Text(
            text = "Welcome to Desktop Chess",
            fontSize = MENU_TITLE_SIZE,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = { onCommandSelected(MenuCommand.OPEN) },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text(
                text = "Open a game",
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { onCommandSelected(MenuCommand.JOIN) },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text(
                text = "Join a game",
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { onCommandSelected(MenuCommand.EXIT) },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text(
                text = "Exit",
                fontWeight = FontWeight.Bold
            )
        }

        Image(
            painter = painterResource(MENU_IMAGE),
            contentDescription = null,
            modifier = Modifier.padding(MENU_IMAGE_PADDING)
        )
    }
}

/**
 * Represents a menu command.
 */
enum class MenuCommand {
    OPEN,
    JOIN,
    EXIT
}

/**
 * Composable used to display a menu command view.
 * @param selectedCommand command to display the view
 * @param onOpenSessionRequest callback to be executed when the user clicks to open a session
 * @param onCancelRequest callback to be executed when the user clicks the cancel button
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuOptionView(
    selectedCommand: MenuCommand?,
    onOpenSessionRequest: (String) -> Unit,
    onCancelRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("${if (selectedCommand == MenuCommand.OPEN) "Open" else "Join"} a game") },
        text = {
            Text(
                if (selectedCommand == MenuCommand.OPEN) {
                    "Opens or joins a game to play with the White pieces"
                } else {
                    "Joins a game named to play with the Black pieces"
                }
            )
        },
        modifier = Modifier.size(MENU_OPTION_VIEW_WIDTH, MENU_OPTION_VIEW_HEIGHT),
        buttons = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(MENU_OPTION_VIEW_WIDTH)
            ) {
                val textState = remember { mutableStateOf(TextFieldValue()) }
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it }
                )
                Row {
                    Button(
                        onClick = { onOpenSessionRequest(textState.value.text) },
                        modifier = Modifier.padding(end = SPACE_BETWEEN_BUTTONS)
                    ) {
                        Text(if (selectedCommand == MenuCommand.OPEN) "Open" else "Join")
                    }

                    Button(onClick = onCancelRequest) {
                        Text("Cancel")
                    }
                }
            }
        }
    )
}

/**
 * Composable used to display a menu and other composable when the session is in logging state.
 * @param session game session
 * @param onOpenSessionRequest callback to be executed when the user clicks to open a session
 * @param windowOnCloseRequest callback to be executed when the user clicks the exit button
 */
@Composable
fun LoggingView(
    session: Session,
    onOpenSessionRequest: (MenuCommand?, String) -> Unit,
    windowOnCloseRequest: () -> Unit
) {
    require(session.isLogging()) { "The session is not in logging state." }

    val selectedCommand = remember { mutableStateOf<MenuCommand?>(null) }

    MenuView { menuCommand -> selectedCommand.value = menuCommand }

    when (selectedCommand.value) {
        in listOf(MenuCommand.OPEN, MenuCommand.JOIN) ->
            MenuOptionView(
                selectedCommand.value,
                onOpenSessionRequest = { gameName ->
                    onOpenSessionRequest(selectedCommand.value, gameName)
                },
                onCancelRequest = { selectedCommand.value = null }
            )
        MenuCommand.EXIT -> windowOnCloseRequest()
        else -> {}
    }
}

/**
 * Opens a session using OpenCommand or JoinCommand.
 * @param session game session
 * @param gameName name of the session game
 * @param selectedCommand used to check if it's an open or join command
 * @param gameStorage where the games are stored
 * @param appOptions application options
 */
suspend fun openSession(
    gameName: String,
    session: MutableState<Session>,
    selectedCommand: MenuCommand?,
    gameStorage: GameStorage,
    appOptions: AppOptions
) {
    if (gameName.isBlank()) return

    session.value = when (selectedCommand) {
        MenuCommand.OPEN -> {
            val openSession = OpenCommand(gameStorage).execute(gameName).getOrThrow()

            if (appOptions.singlePlayer.value && openSession.isPlayable()) {
                openSession.copy(state = SessionState.SINGLE_PLAYER)
            } else {
                openSession
            }
        }
        MenuCommand.JOIN -> JoinCommand(gameStorage).execute(gameName).getOrThrow()
        else -> session.value
    }
}

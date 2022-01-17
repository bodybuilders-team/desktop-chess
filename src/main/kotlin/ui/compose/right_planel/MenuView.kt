package ui.compose.right_planel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import domain.*
import domain.commands.*
import storage.GameStorage
import ui.compose.AppOptions


// Constants
private val MENU_TITLE_SIZE = 40.sp
private val MENU_BUTTON_WIDTH = 200.dp
private val MENU_BUTTON_HEIGHT = 60.dp
private val MENU_BUTTON_PADDING = 10.dp
private val MENU_IMAGE_PADDING = 32.dp
private val SPACE_BETWEEN_BUTTONS = 32.dp
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
 * Composable used to display a menu command view
 * @param session game session
 * @param dataBase database
 * @param selectedCommand command to display the view
 * @param singlePlayer when true, single player mode is on
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuOptionView(
    session: MutableState<Session>,
    dataBase: GameStorage,
    selectedCommand: MutableState<MenuCommand?>,
    singlePlayer: MutableState<Boolean>
) {
    val command = selectedCommand.value

    AlertDialog(
        onDismissRequest = { },
        title = { Text("${if (command == MenuCommand.OPEN) "Open" else "Join"} a game") },
        text = {
            Text(
                if (command == MenuCommand.OPEN)
                    "Opens or joins a game to play with the White pieces"
                else
                    "Joins a game named to play with the Black pieces"
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
                        onClick = {
                            if (textState.value.text.isNotBlank()) {
                                session.value =
                                    if (command == MenuCommand.OPEN) {
                                        val openSession =
                                            OpenCommand(dataBase).execute(textState.value.text).getOrThrow()
                                        if (singlePlayer.value && openSession.isPlayable())
                                            openSession.copy(state = SessionState.SINGLE_PLAYER)
                                        else
                                            openSession
                                    } else
                                        JoinCommand(dataBase).execute(textState.value.text).getOrThrow()
                            }
                        },
                        modifier = Modifier.padding(end = SPACE_BETWEEN_BUTTONS)
                    ) {
                        Text(if (command == MenuCommand.OPEN) "Open" else "Join")
                    }

                    Button(onClick = { selectedCommand.value = null }) {
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
 * @param dataBase database
 * @param options app options
 */
@Composable
fun LoggingView(session: MutableState<Session>, dataBase: GameStorage, options: AppOptions) {
    require(session.value.isLogging()) { "The session is not in logging state." }

    val selectedCommand = remember { mutableStateOf<MenuCommand?>(null) }

    MenuView { menuCommand -> selectedCommand.value = menuCommand }

    when (selectedCommand.value) {
        in listOf(MenuCommand.OPEN, MenuCommand.JOIN) ->
            MenuOptionView(session, dataBase, selectedCommand, options.singlePlayer)
        MenuCommand.EXIT -> options.exitApp.value = true
        else -> {}
    }
}

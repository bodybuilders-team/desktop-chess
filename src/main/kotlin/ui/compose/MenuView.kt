package ui.compose

import WINDOW_PADDING
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import domain.Session
import domain.commands.JoinCommand
import domain.commands.OpenCommand
import storage.GameStorage


// Constants
val MENU_WIDTH = 260.dp
val MENU_HEIGHT = BOARD_HEIGHT
val MENU_TITLE_SIZE = 40.sp
val MENU_BUTTON_WIDTH = 200.dp
val MENU_BUTTON_HEIGHT = 60.dp
val MENU_BUTTON_MODIFIER = Modifier.size(MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT).padding(PROMOTION_BUTTON_PADDING)
val MENU_OPTION_VIEW_WIDTH = 400.dp
val MENU_OPTION_VIEW_HEIGHT = 200.dp


/**
 * Composable used to display a column with the menu.
 * @param onCommandSelected function to be executed when the menu command is selected
 */
@Composable
fun MenuView(onCommandSelected: (MenuCommand?) -> Unit) {
    Column(
        modifier = Modifier.padding(start = WINDOW_PADDING)
            .width(MENU_WIDTH)
            .height(MENU_HEIGHT)
            .background(Color(WHITE)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Desktop Chess",
            fontSize = MENU_TITLE_SIZE,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { onCommandSelected(MenuCommand.OPEN) },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text("Open a game")
        }
        Button(
            onClick = { onCommandSelected(MenuCommand.JOIN) },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text("Join a game")
        }
        Button(
            onClick = { onCommandSelected(MenuCommand.EXIT) },
            modifier = MENU_BUTTON_MODIFIER
        ) {
            Text("Exit")
        }
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
 * @param db database
 * @param command command to display the view
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuOptionView(session: MutableState<Session>, db: GameStorage, command: MenuCommand) {
    if (command == MenuCommand.EXIT) return

    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text("${if (command == MenuCommand.OPEN) "Open" else "Join"} a game")
            },
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
                                session.value =
                                    if (command == MenuCommand.OPEN)
                                        OpenCommand(db).execute(textState.value.text).getOrThrow()
                                    else
                                        JoinCommand(db).execute(textState.value.text).getOrThrow()
                            },
                            modifier = Modifier.padding(end = WINDOW_PADDING)
                        ) {
                            Text(if (command == MenuCommand.OPEN) "Open" else "Join")
                        }

                        Button(onClick = { openDialog.value = false }) {
                            Text("Cancel")
                        }
                    }
                }
            }
        )
    }
}

package ui.compose.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import domain.Session
import domain.commands.JoinCommand
import storage.GameStorage


// Constants
val JOIN_VIEW_WIDTH = 400.dp
val JOIN_VIEW_HEIGHT = 200.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JoinView(session: MutableState<Session>, db: GameStorage) {
    var openDialog by mutableStateOf(true)

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text("Join a game") },
            text = { Text("Joins a game named to play with the Black pieces") },
            modifier = Modifier.size(JOIN_VIEW_WIDTH, JOIN_VIEW_HEIGHT),
            buttons = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(OPEN_VIEW_WIDTH)) {
                    val textState = remember { mutableStateOf(TextFieldValue()) }
                    TextField(
                        value = textState.value,
                        onValueChange = { textState.value = it }
                    )
                    Button(
                        onClick = {
                            session.value = JoinCommand(db).execute(textState.value.text).getOrThrow()
                        }
                    ) {
                        Text("Join")
                    }
                }
            }
        )
    }
}

package ui.compose.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import domain.Session
import domain.commands.OpenCommand
import storage.GameStorage


// Constants
val OPEN_VIEW_WIDTH = 400.dp
val OPEN_VIEW_HEIGHT = 200.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OpenView(session: MutableState<Session>, db: GameStorage) {
    var openDialog by mutableStateOf(true)

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text("Open a game") },
            text = { Text("Opens or joins a game to play with the White pieces") },
            modifier = Modifier.size(OPEN_VIEW_WIDTH, OPEN_VIEW_HEIGHT),
            buttons = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(OPEN_VIEW_WIDTH)) {
                    val textState = remember { mutableStateOf(TextFieldValue()) }
                    TextField(
                        value = textState.value,
                        onValueChange = { textState.value = it }
                    )
                    Button(
                        onClick = {
                            session.value = OpenCommand(db).execute(textState.value.text).getOrThrow()
                        }
                    ) {
                        Text("Open")
                    }
                }
            }
        )
    }
}

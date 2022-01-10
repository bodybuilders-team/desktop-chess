package ui.compose

import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import domain.*
import ui.compose.app.Options


/**
 * Window Menu Bar.
 * @param options app options
 */
@Composable
fun FrameWindowScope.MenuBar(session: MutableState<Session>, options: Options) {
    MenuBar {
        Menu("File") {
            Item(
                text = "Refresh game",
                onClick = { options.refreshGame.value = true },
                enabled = session.value.state == SessionState.WAITING_FOR_OPPONENT
            )
            Item(
                text = "Close game",
                onClick = { options.closeGame.value = true },
                enabled = session.value.isNotLogging()
            )
        }
        Menu("Options") {
            CheckboxItem(
                text = "Show targets",
                checked = options.targetsOn.value,
                onCheckedChange = { options.targetsOn.value = it }
            )

            CheckboxItem(
                text = "Single player",
                checked = options.singlePlayer.value,
                onCheckedChange = { options.singlePlayer.value = it },
                enabled = session.value.isLogging()
            )

        }
    }
}

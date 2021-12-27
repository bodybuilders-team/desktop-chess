package ui.compose

import androidx.compose.runtime.*
import androidx.compose.ui.window.*


/**
 * Window Menu Bar.
 * @param options app options
 */
@Composable
fun FrameWindowScope.MenuBar(options: Options) {
    MenuBar {
        Menu("Options") {
            CheckboxItem(
                text = "Show targets",
                checked = options.targetsOn.value,
                onCheckedChange = { options.targetsOn.value = it }
            )

            CheckboxItem(
                text = "Single player",
                checked = options.singlePlayer.value,
                onCheckedChange = { options.singlePlayer.value = it }
            )

            Item(
                text = "Close game",
                onClick = { options.closeGame.value = true }
            )
        }
    }
}

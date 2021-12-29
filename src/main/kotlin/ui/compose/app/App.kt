package ui.compose.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import domain.*
import storage.*
import ui.compose.*
import ui.compose.board.*


/**
 * Main Composable used to display the chess app.
 * @param dataBase database where the games are stored
 * @param options app options
 */
@Composable
@Preview
fun App(dataBase: GameStorage, options: Options) {
    val session = remember { mutableStateOf(INITIAL_SESSION) }

    if (options.closeGame.value) {
        session.value = INITIAL_SESSION
        options.closeGame.value = false
    }

    Timer(session, dataBase)

    MaterialTheme {
        Row(
            modifier = Modifier
                .background(BACKGROUND_COLOR)
                .padding(WINDOW_PADDING)
                .size(WINDOW_WIDTH, WINDOW_HEIGHT)
        ) {
            RowsView()
            Column {
                ColumnsView()
                Row {
                    BoardView(session, options.targetsOn, dataBase)

                    if (session.value.isLogging())
                        LoggingView(session, dataBase, options)
                    else
                        MovesView(session.value.game)
                }
                if (session.value.isNotLogging())
                    GameInfoView(session.value)
            }
        }
    }
}

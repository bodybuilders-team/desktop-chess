package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.*
import storage.*
import ui.compose.board.*
import ui.compose.right_planel.*


// Constants
const val WINDOW_SCALE = 1f

val APP_WIDTH = ROWS_IDENTIFIER_WIDTH + BOARD_WIDTH + SPACE_BETWEEN_BOARD_AND_RIGHT_PANEL + RIGHT_PANEL_WIDTH
val APP_HEIGHT = COLUMNS_IDENTIFIER_HEIGHT + BOARD_HEIGHT + SPACE_BETWEEN_BOARD_AND_GAME_INFO + GAME_INFO_HEIGHT
val APP_PADDING = 32.dp
private val BACKGROUND_COLOR = Color(0xFF2B2B2B)

val FONT_FAMILY = FontFamily.Monospace
val FONT_SIZE = 20.sp

/**
 * Main Composable used to display the chess app.
 * @param session app session
 * @param dataBase database where the games are stored
 * @param appOptions app options
 */
@Composable
@Preview
fun App(session: MutableState<Session>, dataBase: GameStorage, appOptions: AppOptions,
        windowOnCloseRequest: () -> Unit) {
    RefreshTimer(session, dataBase)

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(BACKGROUND_COLOR)
                .padding(APP_PADDING)
                .size(APP_WIDTH, APP_HEIGHT)
        ) {
            Row {
                BoardView(
                    session = session,
                    targetsOn = appOptions.targetsOn,
                    useSelectedTile = @Composable { selectedPosition, availableMoves ->
                        UseSelectedPosition(selectedPosition.value!!, dataBase, session, availableMoves)
                    }
                )

                RightPanelView(session, dataBase, appOptions, windowOnCloseRequest)
            }
            if (session.value.isNotLogging()) {
                GameInfoView(session.value)
            }
        }
    }
}


/**
 * App options
 * @property singlePlayer when true, single player mode is on
 * @property targetsOn when true, the available move targets are on
 */
data class AppOptions(
    val singlePlayer: MutableState<Boolean>,
    val targetsOn: MutableState<Boolean>
)

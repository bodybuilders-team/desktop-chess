package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.*
import domain.*
import domain.commands.*
import domain.game.getAvailableMoves
import domain.move.Move
import storage.*
import ui.compose.board.*
import ui.compose.right_planel.*


// Constants
const val WINDOW_SCALE = 0.8f

val APP_WIDTH = ROWS_IDENTIFIER_WIDTH + BOARD_WIDTH + SPACE_BETWEEN_BOARD_AND_RIGHT_PANEL + RIGHT_PANEL_WIDTH
val APP_HEIGHT = COLUMNS_IDENTIFIER_HEIGHT + BOARD_HEIGHT + SPACE_BETWEEN_BOARD_AND_GAME_INFO + GAME_INFO_HEIGHT
val APP_PADDING = 32.dp * WINDOW_SCALE
private val BACKGROUND_COLOR = Color(0xFF2B2B2B)

val FONT_FAMILY = FontFamily.Monospace
val FONT_SIZE = 20.sp * WINDOW_SCALE


/**
 * Main Composable used to display the chess app.
 * @param session app session
 * @param dataBase database where the games are stored
 * @param appOptions app options
 * @param windowOnCloseRequest callback to be executed when the user clicks the exit button
 */
@Composable
@Preview
fun App(
    session: MutableState<Session>,
    dataBase: GameStorage,
    appOptions: AppOptions,
    windowOnCloseRequest: () -> Unit
) {
    if (session.value.isWaiting())
        RefreshTimer { session.value = RefreshCommand(dataBase, session.value).execute(null).getOrThrow() }

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(BACKGROUND_COLOR)
                .padding(APP_PADDING)
                .size(APP_WIDTH, APP_HEIGHT)
        ) {
            Row {
                val move = remember { mutableStateOf<Move?>(null) }
                val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }
                val showPromotionView = remember { mutableStateOf(false) }

                BoardView(
                    session = session.value,
                    targetsOn = appOptions.targetsOn.value,
                    move = move.value,
                    availableMoves = availableMoves.value,
                    onLoggingRequest = { availableMoves.value = emptyList() },
                    onClickedTile = { clickedPosition ->
                        move.value = availableMoves.value.find { it.to == clickedPosition }
                        if (move.value == null)
                            availableMoves.value = session.value.game.getAvailableMoves(clickedPosition)
                    },
                    onMakeMoveRequest = {
                        when {
                            move.value?.promotion != null -> showPromotionView.value = true
                            else -> makeMove(move, availableMoves, session, dataBase)
                        }
                    }
                )

                if (showPromotionView.value) {
                    ShowPromotionView(session, move, availableMoves, dataBase)
                    showPromotionView.value = false
                }

                RightPanelView(
                    session = session.value,
                    onOpenSessionRequest = { selectedCommand, gameName ->
                        openSession(gameName, session, selectedCommand, dataBase, appOptions)
                    },
                    windowOnCloseRequest = windowOnCloseRequest
                )
            }

            if (session.value.isNotLogging())
                GameInfoView(session.value)
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

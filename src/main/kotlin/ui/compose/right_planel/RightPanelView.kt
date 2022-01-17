package ui.compose.right_planel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Session
import domain.isLogging
import storage.GameStorage
import ui.compose.AppOptions
import ui.compose.board.*


// Constants
val RIGHT_PANEL_WIDTH = 320.dp
private val RIGHT_PANEL_HEIGHT = BOARD_HEIGHT
val SPACE_BETWEEN_BOARD_AND_RIGHT_PANEL = 32.dp
private val RIGHT_PANEL_BACKGROUND = LIGHT_TILE_COLOR



/**
 * Composable used to display a box with the menu or the list of moves already made in a chess game.
 * If the current session is in "Logging" state, displays the menu, if not, displays the list of moves.
 *
 * @param session app session
 * @param dataBase where the games are stored
 * @param appOptions application options
 */
@Composable
fun RightPanelView(session: MutableState<Session>, dataBase: GameStorage, appOptions: AppOptions) {
    Box(
        modifier = Modifier
            .padding(start = SPACE_BETWEEN_BOARD_AND_RIGHT_PANEL, top = COLUMNS_IDENTIFIER_HEIGHT)
            .size(RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT)
            .background(RIGHT_PANEL_BACKGROUND)
    ) {
        if (session.value.isLogging())
            LoggingView(session, dataBase, appOptions)
        else
            MovesView(session.value.game)
    }
}

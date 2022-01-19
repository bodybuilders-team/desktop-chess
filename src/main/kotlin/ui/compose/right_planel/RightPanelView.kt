package ui.compose.right_planel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.*
import ui.compose.WINDOW_SCALE
import ui.compose.board.*


// Constants
val RIGHT_PANEL_WIDTH = 320.dp * WINDOW_SCALE
val RIGHT_PANEL_HEIGHT = BOARD_HEIGHT
val SPACE_BETWEEN_BOARD_AND_RIGHT_PANEL = 32.dp * WINDOW_SCALE
private val RIGHT_PANEL_BACKGROUND = LIGHT_TILE_COLOR


/**
 * Composable used to display a box with the menu or the list of moves already made in a chess game.
 * If the current session is in "Logging" state, displays the menu, if not, displays the list of moves.
 *
 * @param session app session
 * @param onOpenSessionRequest callback to be executed when the user clicks to open a session
 * @param windowOnCloseRequest callback to be executed when the user clicks the exit button
 */
@Composable
fun RightPanelView(
    session: Session,
    onOpenSessionRequest: (MenuCommand?, String) -> Unit,
    windowOnCloseRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = SPACE_BETWEEN_BOARD_AND_RIGHT_PANEL, top = COLUMNS_IDENTIFIER_HEIGHT)
            .size(RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT)
            .background(RIGHT_PANEL_BACKGROUND)
    ) {
        if (session.isLogging())
            LoggingView(
                session,
                onOpenSessionRequest = onOpenSessionRequest,
                windowOnCloseRequest = windowOnCloseRequest
            )
        else
            MovesView(session.game)
    }
}

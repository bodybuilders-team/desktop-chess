package ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import domain.NO_NAME
import domain.Session
import domain.board.*
import domain.game.*


//Constants
val GAME_INFO_HEIGHT = 70.dp
val GAME_INFO_PADDING = 10.dp
val GAME_INFO_FONT_SIZE = 20.sp
val GAME_INFO_FONT_COLOR = Color.Black
val ROWS_COLS_FONT_COLOR = Color.White
val COLS_VIEW_HEIGHT = 30.dp
val CHARACTER_SIZE = 24.sp


/**
 * Composable used to display additional information related to the game.
 * @param session game session
 */
@Composable
fun GameInfoView(session: Session) {
    Column(
        modifier = Modifier
            .padding(top = WINDOW_PADDING)
            .size(BOARD_WIDTH, GAME_INFO_HEIGHT)
            .background(Color(WHITE))
    ) {
        Text(
            "Game: ${if (session.name != NO_NAME) session.name.trim() else "???"} | Turn: ${session.game.armyToPlay}",
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE,
            color = GAME_INFO_FONT_COLOR
        )
        Text(
            "Game State: ${session.state}",
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE,
            color = GAME_INFO_FONT_COLOR
        )
        Text(
            "Info: ${session.game.state}",
            color = when (session.game.state) {
                GameState.CHECK     -> Color.Red
                GameState.CHECKMATE -> Color.Red
                else                -> GAME_INFO_FONT_COLOR
            },
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE
        )
    }
}


/**
 * Composable used to display additional information related to columns.
 */
@Composable
fun ColumnsView() {
    Row(
        modifier = Modifier
            .padding(bottom = GAME_INFO_PADDING)
            .height(COLS_VIEW_HEIGHT)
    ) {
        repeat(BOARD_SIDE_LENGTH) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.width(TILE_SIZE)) {
                Text(
                    "${FIRST_COL + it}",
                    fontFamily = FONT_FAMILY,
                    fontSize = CHARACTER_SIZE,
                    color = ROWS_COLS_FONT_COLOR
                )
            }
        }
    }
}


/**
 * Composable used to display additional information related to rows.
 */
@Composable
fun RowsView() {
    Column(modifier = Modifier.padding(top = COLS_VIEW_HEIGHT + GAME_INFO_PADDING, end = GAME_INFO_PADDING)) {
        repeat(BOARD_SIDE_LENGTH) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.height(TILE_SIZE)) {
                Text(
                    "${LAST_ROW - it}",
                    fontFamily = FONT_FAMILY,
                    fontSize = CHARACTER_SIZE,
                    color = ROWS_COLS_FONT_COLOR
                )
            }
        }
    }
}

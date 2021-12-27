package ui.compose.menu

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.Session
import domain.board.BOARD_SIDE_LENGTH
import domain.board.FIRST_COL
import domain.board.LAST_ROW
import domain.game.GameState
import domain.game.state
import ui.compose.FONT_FAMILY
import ui.compose.FONT_SIZE
import ui.compose.TILE_SIZE


//Constants
val GAME_INFO_PADDING = 64.dp
val SPACE_BETWEEN_INFO_PADDING = 8.dp
val FONT_SIZE_OFFSET = 14.dp
val CHARACTER_SIZE = 24.sp

/**
 * Composable used to display additional information related to the game.
 * @param session game session
 */
@Composable
@Preview
fun GameInfoView(session:Session) {
    Column(modifier =  Modifier.padding(SPACE_BETWEEN_INFO_PADDING)) {
        Text("Game: ${if (session.name != "") session.name.trim() else "???"} | Turn: ${session.army}",
            fontFamily = FONT_FAMILY,
            fontSize = FONT_SIZE
        )
        Text("Game State: ${session.state}",
            fontFamily = FONT_FAMILY,
            fontSize = FONT_SIZE
        )
        Text("Info: ${session.game.state}",
            color = when(session.game.state) {
                GameState.CHECK -> Color.Red
                GameState.CHECKMATE -> Color.Red
                else -> Color.Black
            }, fontFamily = FONT_FAMILY,
            fontSize = FONT_SIZE
        )
    }
}


/**
 * Composable used to display additional information related to columns.
 */
@Composable
@Preview
fun ColumnView() {
    Row(modifier = Modifier.absolutePadding(left = 10.dp)) {
        repeat(BOARD_SIDE_LENGTH) {
            Text("${FIRST_COL + it}",
                fontFamily = FONT_FAMILY,
                fontSize = CHARACTER_SIZE,
                color = Color.Black,
                modifier = Modifier.absolutePadding(right = TILE_SIZE - FONT_SIZE_OFFSET)
            )
        }

    }
}


/**
 * Composable used to display additional information related to rows.
 */
@Composable
@Preview
fun RowView() {
    Column {
        repeat(BOARD_SIDE_LENGTH) {
            Text("${LAST_ROW - it}",
                fontFamily = FONT_FAMILY,
                fontSize = CHARACTER_SIZE,
                color = Color.Black,
                modifier = Modifier.absolutePadding(top = TILE_SIZE - FONT_SIZE_OFFSET * 2)
            )
        }
    }
}
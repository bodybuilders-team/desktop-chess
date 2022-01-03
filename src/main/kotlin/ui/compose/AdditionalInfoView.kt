package ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import domain.*
import domain.board.*
import domain.game.*
import ui.compose.app.WINDOW_PADDING
import ui.compose.board.*


//Constants
val GAME_INFO_HEIGHT = 70.dp
val COLS_VIEW_HEIGHT = 30.dp
private val GAME_INFO_PADDING = 10.dp
private val GAME_INFO_FONT_SIZE = 20.sp
private val CHARACTER_SIZE = 24.sp

private val ROWS_COLS_FONT_COLOR = Color.White
private val GAME_INFO_FONT_COLOR = Color.Black
private val GAME_INFO_CHECK_FONT_COLOR = Color(0xFFFF4500)
private val GAME_INFO_CHECKMATE_FONT_COLOR = Color.Red
private val GAME_INFO_TIE_FONT_COLOR = Color.Blue


operator fun Color.plus(other: Color) = Color(this.value + other.value)

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
            "Game: ${if (session.name != NO_NAME) session.name.trim() else "???"}" +
                    "\nTurn: ${session.game.armyToPlay}",
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE,
            color = GAME_INFO_FONT_COLOR
        )
        Text(
            "Info: " +
                    if (session.game.endedInDraw()) "Draw " +
                            when (session.game.state) {
                                GameState.STALEMATE -> "by stalemate"
                                GameState.FIFTY_MOVE_RULE -> "by fifty-move-rule"
                                GameState.THREE_FOLD -> "by repetition"
                                GameState.DEAD_POSITION -> "by insufficient material"
                                else -> ""
                            }
                    else
                        session.game.state.toString().replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercaseChar() },
            color = when (session.game.state) {
                GameState.CHECK -> GAME_INFO_CHECK_FONT_COLOR
                GameState.CHECKMATE -> GAME_INFO_CHECKMATE_FONT_COLOR
                GameState.STALEMATE -> GAME_INFO_TIE_FONT_COLOR
                GameState.FIFTY_MOVE_RULE -> GAME_INFO_TIE_FONT_COLOR
                GameState.THREE_FOLD -> GAME_INFO_TIE_FONT_COLOR
                GameState.DEAD_POSITION -> GAME_INFO_TIE_FONT_COLOR
                else -> GAME_INFO_FONT_COLOR
            },
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE
        )
    }
}


/**
 * Composable used to display chess board column letters.
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
 * Composable used to display chess board row numbers.
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
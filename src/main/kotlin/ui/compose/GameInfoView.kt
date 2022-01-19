package ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import domain.*
import domain.game.*
import ui.compose.board.*


// Constants
val GAME_INFO_HEIGHT = 72.dp * WINDOW_SCALE
val SPACE_BETWEEN_BOARD_AND_GAME_INFO = 32.dp * WINDOW_SCALE
private val GAME_INFO_FONT_SIZE = 20.sp * WINDOW_SCALE

private val GAME_INFO_BACKGROUND = LIGHT_TILE_COLOR
private val GAME_INFO_FONT_COLOR = Color.Black
private val GAME_INFO_CHECK_FONT_COLOR = Color(0xFFFF4500)
private val GAME_INFO_CHECKMATE_FONT_COLOR = Color.Red
private val GAME_INFO_TIE_FONT_COLOR = Color.Blue


/**
 * Composable used to display additional information related to the game.
 * @param session game session
 */
@Composable
fun GameInfoView(session: Session) {
    Column(
        modifier = Modifier
            .padding(start = ROWS_IDENTIFIER_WIDTH, top = SPACE_BETWEEN_BOARD_AND_GAME_INFO)
            .size(BOARD_WIDTH, GAME_INFO_HEIGHT)
            .background(GAME_INFO_BACKGROUND)
    ) {
        Text(
            text = "Game: ${if (session.name != NO_NAME) session.name.trim() else "???"}",
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE,
            color = GAME_INFO_FONT_COLOR
        )
        Text(
            text = "Turn: ${session.game.armyToPlay}",
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE,
            color = GAME_INFO_FONT_COLOR
        )
        Text(
            text = "Info: " +
                    if (session.game.endedInDraw()) {
                        "Draw " +
                                when (session.game.state) {
                                    GameState.STALEMATE         -> "by stalemate"
                                    GameState.FIFTY_MOVE_RULE   -> "by fifty-move-rule"
                                    GameState.THREE_FOLD        -> "by repetition"
                                    GameState.DEAD_POSITION     -> "by insufficient material"
                                    else -> ""
                                }
                    } else {
                        session.game.state.toString()
                            .replace("_", " ")
                            .lowercase()
                            .replaceFirstChar { it.uppercaseChar() }
                    },
            color = when (session.game.state) {
                GameState.CHECK             -> GAME_INFO_CHECK_FONT_COLOR
                GameState.CHECKMATE         -> GAME_INFO_CHECKMATE_FONT_COLOR
                GameState.STALEMATE         -> GAME_INFO_TIE_FONT_COLOR
                GameState.FIFTY_MOVE_RULE   -> GAME_INFO_TIE_FONT_COLOR
                GameState.THREE_FOLD        -> GAME_INFO_TIE_FONT_COLOR
                GameState.DEAD_POSITION     -> GAME_INFO_TIE_FONT_COLOR
                else                        -> GAME_INFO_FONT_COLOR
            },
            fontFamily = FONT_FAMILY,
            fontSize = GAME_INFO_FONT_SIZE
        )
    }
}

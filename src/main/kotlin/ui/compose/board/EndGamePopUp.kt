package ui.compose.board

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import domain.Session
import domain.game.*
import ui.compose.WINDOW_SCALE

// Constants
private val END_GAME_POP_UP_WIDTH = 200.dp * WINDOW_SCALE
private val END_GAME_POP_UP_TEXT_FONT_SIZE = 20.sp * WINDOW_SCALE


/**
 * Composable used to show the endgame pop-up in case of a play that ends the game, i.e. checkmate.
 * @param session game session
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EndGamePopUp(session: Session) {
    val showEnd = remember { mutableStateOf(EndState.ENDED) }
    if (showEnd.value == EndState.ACKNOWLEDGED) return

    AlertDialog(
        onDismissRequest = { },
        title = { Text("Game ended") },
        modifier = Modifier.width(END_GAME_POP_UP_WIDTH),
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(END_GAME_POP_UP_WIDTH)
            ) {
                Text(
                    text = if (session.game.state == GameState.CHECKMATE) "CHECKMATE!" else "DRAW",
                    fontSize = END_GAME_POP_UP_TEXT_FONT_SIZE,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text =
                    when (session.game.state) {
                        GameState.CHECKMATE         -> "${session.game.armyToPlay.other()} won!"
                        GameState.STALEMATE         -> "by stalemate"
                        GameState.FIFTY_MOVE_RULE   -> "by fifty-move-rule"
                        GameState.THREE_FOLD        -> "by repetition"
                        GameState.DEAD_POSITION     -> "by insufficient material"
                        else -> ""
                    },
                    fontSize = END_GAME_POP_UP_TEXT_FONT_SIZE,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        buttons = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(END_GAME_POP_UP_WIDTH)
            ) {
                Button(onClick = { showEnd.value = EndState.ACKNOWLEDGED }) { Text("OK") }
            }
        }
    )
}

/**
 * Represents the EndState, used in end game pop up.
 */
private enum class EndState {
    ENDED,
    ACKNOWLEDGED
}

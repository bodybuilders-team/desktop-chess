package ui.compose.board

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.Session
import domain.SessionState
import domain.game.GameState
import domain.game.armyToPlay
import domain.game.state


private val END_GAME_POP_UP_WIDTH = 200.dp
private val END_GAME_POP_UP_TEXT_FONT_SIZE = 20.sp


enum class EndState {
    NOT_ENDED,
    ENDED,
    ACKNOWLEDGED
}


/**
 * Composable used to show the endgame pop-up in case of a play that ends the game, i.e. checkmate.
 * @param session game session
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EndGamePopUp(session: MutableState<Session>) {

    val showEnd = remember { mutableStateOf(EndState.NOT_ENDED) }

    if (session.value.state == SessionState.ENDED && showEnd.value == EndState.NOT_ENDED)
        showEnd.value = EndState.ENDED

    if (showEnd.value == EndState.ENDED) {
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
                        text = if (session.value.game.state == GameState.CHECKMATE) "CHECKMATE!" else "DRAW",
                        fontSize = END_GAME_POP_UP_TEXT_FONT_SIZE,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =
                        when (session.value.game.state) {
                            GameState.CHECKMATE         -> "${session.value.game.armyToPlay.other()} won!"
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
                    Button(onClick = { showEnd.value = EndState.ACKNOWLEDGED }) {
                        Text("OK")
                    }
                }
            }
        )
    }
}

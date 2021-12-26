import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import domain.board.BOARD_SIDE_LENGTH
import domain.board.Board
import domain.board.FIRST_COL
import domain.game.gameFromMoves
import domain.game.getAvailableMoves
import domain.game.makeMove
import domain.move.Move
import domain.pieces.isWhite
import ui.compose.BoardView
import ui.compose.IMAGE_CENTERING_LEFT_OFFSET
import ui.compose.IMAGE_DIMENSIONS
import ui.compose.TILE_SIZE


// Constants
val BOARD_WINDOW_HEIGHT = TILE_SIZE * BOARD_SIDE_LENGTH
val BOARD_WINDOW_WIDTH = BOARD_WINDOW_HEIGHT
val MOVES_WINDOW_WIDTH = 200.dp
val MOVES_WINDOW_HEIGHT = BOARD_WINDOW_HEIGHT
val WINDOW_PADDING = 32.dp
val WINDOW_WIDTH =  BOARD_WINDOW_WIDTH + MOVES_WINDOW_WIDTH + WINDOW_PADDING * 2
val WINDOW_HEIGHT = BOARD_WINDOW_HEIGHT + WINDOW_PADDING * 2 + 39.dp


/**
 *
 */
@Composable
@Preview
fun App() {
    val game = remember { mutableStateOf(gameFromMoves()) }
    val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }

    MaterialTheme {
            Box(modifier = Modifier.width(WINDOW_WIDTH).height(WINDOW_HEIGHT).background(Color.Gray)) {
                /*Image(
                    painter = painterResource("background.png"),
                    contentDescription = null,
                    modifier = Modifier.width(WINDOW_WIDTH).height(WINDOW_HEIGHT),
                    alignment = Alignment.TopStart
                )*/
                Row(modifier = Modifier.padding(WINDOW_PADDING)) {
                    BoardView(game.value, availableMoves.value) { position ->
                        if (position in availableMoves.value.map { it.to }) {
                            game.value = game.value.makeMove(availableMoves.value.first { it.to == position })
                            availableMoves.value = emptyList()
                        } else {
                            availableMoves.value = game.value.getAvailableMoves(position)
                        }
                    }
                    // MOVES BOX
                }
            }
        }
    }


fun main() = application {
    Window(
        resizable = false,
        state = WindowState(size = DpSize(WINDOW_WIDTH, WINDOW_HEIGHT)),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}

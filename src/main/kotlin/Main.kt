import androidx.compose.desktop.DesktopMaterialTheme

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import domain.board.*
import domain.game.Game
import domain.game.gameFromMoves
import domain.game.getAvailableMoves
import domain.game.makeMove
import domain.pieces.Piece
import domain.pieces.isWhite


val WINDOW_SIZE = 1024.dp
val WINDOW_PADDING = 32.dp

@Composable
@Preview
fun App() {
    val game = remember { mutableStateOf(gameFromMoves()) }

    DesktopMaterialTheme {
        Box(modifier = Modifier.size(WINDOW_SIZE).background(color = Color.DarkGray).padding(WINDOW_PADDING)) {
            Row(modifier = Modifier.background(Color.Black)) {
                repeat(BOARD_SIDE_LENGTH) { columnIdx ->
                    Column(modifier = Modifier.background(Color.White)) {
                        repeat(BOARD_SIDE_LENGTH) { rowIdx ->
                            val position = Board.Position(FIRST_COL + columnIdx, LAST_ROW - rowIdx)
                            val availableMoves = game.value.getAvailableMoves(position)
                            
                            Tile(position, game.value.board.getPiece(position), false) {
                                
                                /*if (availableMoves.isNotEmpty())
                                    game.value = game.value.makeMove(availableMoves.first())*/
                                
                                println(position)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Tile(position: Board.Position, piece: Piece?, isAvailable: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clickable(true, onClick = onClick)
            .background(color = if ((position.col - FIRST_COL + position.row) % 2 == 0) Color.White else Color.Gray)
    ) {
        if (piece != null) {
            val painter = painterResource(
                "${if (piece.isWhite()) "w" else "b"}_${piece.type.toString().lowercase()}.png"
            )

            Image(painter = painter, contentDescription = null, modifier = Modifier.size(256.dp, 256.dp))
        }
        if (isAvailable) {
            androidx.compose.foundation.Canvas(modifier = Modifier.size(20.dp).align(Alignment.Center), onDraw = {
                drawCircle(color = Color.Green)
            })
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
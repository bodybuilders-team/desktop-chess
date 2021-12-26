package ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.board.Board
import domain.board.FIRST_COL
import domain.pieces.Piece
import domain.pieces.isWhite


// Constants
const val RED_TILE = 0xFF794839
const val ORANGE_TILE = 0xFF5D3231
const val LIGHT_TILE = 0xFFEBEBD0
const val DARK_TILE = RED_TILE
val TILE_SIZE = 88.dp
val IMAGE_DIMENSIONS = 256.dp
val IMAGE_CENTERING_LEFT_OFFSET = 2.dp
val GREEN_CIRCLES_SIZE = 20.dp


/**
 * Composable used to display a Chess Tile
 * @param position board position relative to that tile
 * @param piece Piece supposed to be (or not) in that tile
 * @param isAvailable Indicates whether a move is possible to that tile or not
 * @param onClick Event to be made after a tile is selected
 */
@Composable
fun Tile(position: Board.Position, piece: Piece?, isAvailable: Boolean, onClick: (Board.Position) -> Unit) {
    Box(
        modifier = Modifier
            .size(TILE_SIZE)
            .clickable(true) {
                onClick(position)
            }
            .background(
                color = if ((position.col - FIRST_COL + position.row) % 2 == 0) Color(LIGHT_TILE) else Color(DARK_TILE)
            )
    )
    {
        if (piece != null) {
            val painter = painterResource(
                "${if (piece.isWhite()) "w" else "b"}_${piece.type.toString().lowercase()}.png"
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(IMAGE_DIMENSIONS, IMAGE_DIMENSIONS)
                    .absolutePadding(left = IMAGE_CENTERING_LEFT_OFFSET)
            )
        }
        if (isAvailable) {
            Canvas(modifier = Modifier.size(GREEN_CIRCLES_SIZE).align(Alignment.Center), onDraw = {
                drawCircle(color = Color.Green)
            })
        }
    }
}

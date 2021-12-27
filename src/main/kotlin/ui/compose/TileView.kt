package ui.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.board.*
import domain.pieces.*


// Constants
const val RED = 0xFF794839
const val WHITE = 0xFFEBEBD0
val LIGHT_TILE_COLOR = Color(WHITE)
val DARK_TILE_COLOR = Color(RED)
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
fun Tile(
    position: Board.Position,
    piece: Piece?,
    isAvailable: Boolean,
    onClick: (Board.Position) -> Unit,
    targetsOn: MutableState<Boolean>
) {
    Box(
        modifier = Modifier
            .size(TILE_SIZE)
            .clickable(true) { onClick(position) }
            .background(if ((position.col - FIRST_COL + position.row) % 2 == 0) LIGHT_TILE_COLOR else DARK_TILE_COLOR)
    )
    {
        if (piece != null) {
            Image(
                painter = painterResource(
                    "${if (piece.isWhite()) "w" else "b"}_${piece.type.toString().lowercase()}.png"
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(IMAGE_DIMENSIONS, IMAGE_DIMENSIONS)
                    .absolutePadding(left = IMAGE_CENTERING_LEFT_OFFSET)
            )
        }
        if (isAvailable && targetsOn.value)
            Canvas(
                modifier = Modifier
                    .size(GREEN_CIRCLES_SIZE)
                    .align(Alignment.Center),
                onDraw = { drawCircle(color = Color.Green) }
            )
    }
}

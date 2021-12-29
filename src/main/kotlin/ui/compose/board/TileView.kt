package ui.compose.board

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.board.*
import domain.pieces.*


// Constants
const val RED = 0xFF794839
const val WHITE = 0xFFEBEBD0

val TILE_SIZE = 88.dp
private val LIGHT_TILE_COLOR = Color(WHITE)
private val DARK_TILE_COLOR = Color(RED)
private val IMAGE_DIMENSIONS = 256.dp
private val IMAGE_CENTERING_OFFSET = 3.dp
private val GREEN_CIRCLES_SIZE = 20.dp
private val TILE_BORDER_WIDTH = 3.dp
private val TILE_BORDER_COLOR = Color.Green


/**
 * Composable used to display a Chess Tile
 * @param position board position relative to that tile
 * @param piece piece supposed to be (or not) in that tile
 * @param isAvailable indicates whether a move is possible to that tile or not
 * @param isSelected indicates if the tile is selected
 * @param onClick event to be made after a tile is selected
 */
@Composable
fun Tile(
    position: Board.Position,
    piece: Piece?,
    isAvailable: Boolean,
    isSelected: Boolean,
    onClick: (Board.Position) -> Unit
) {
    Box(
        modifier = Modifier
            .size(TILE_SIZE)
            .clickable(true) { onClick(position) }
            .background(if ((position.col - FIRST_COL + position.row) % 2 == 0) LIGHT_TILE_COLOR else DARK_TILE_COLOR)
            .border(if (isSelected) TILE_BORDER_WIDTH else (-1).dp, TILE_BORDER_COLOR)
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
                    .absolutePadding(left = IMAGE_CENTERING_OFFSET, top = IMAGE_CENTERING_OFFSET)
            )
        }
        if (isAvailable)
            Canvas(
                modifier = Modifier
                    .size(GREEN_CIRCLES_SIZE)
                    .align(Alignment.Center),
                onDraw = { drawCircle(color = Color.Green) }
            )
    }
}

package ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import domain.board.*
import domain.game.Game
import domain.move.Move


// Constants
val BOARD_HEIGHT = TILE_SIZE * BOARD_SIDE_LENGTH
val BOARD_WIDTH = BOARD_HEIGHT


/**
 * Composable used to display a Chess Board
 * @param game chess game
 * @param availableMoves all possible moves of the selected position
 * @param onTileSelected Event to be made after a tile is selected
 */
@Composable
fun BoardView(
    game: Game,
    availableMoves: List<Move>,
    targetsOn: MutableState<Boolean>,
    onTileSelected: (Board.Position) -> Unit
) {
    Row {
        repeat(BOARD_SIDE_LENGTH) { columnIdx ->
            Column {
                repeat(BOARD_SIDE_LENGTH) { rowIdx ->
                    val position = Board.Position(FIRST_COL + columnIdx, LAST_ROW - rowIdx)
                    Tile(
                        position,
                        piece = game.board.getPiece(position),
                        isAvailable = position in availableMoves.map { it.to },
                        onClick = onTileSelected,
                        targetsOn = targetsOn
                    )
                }
            }
        }
    }
}

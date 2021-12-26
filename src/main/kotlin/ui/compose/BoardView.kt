package ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnitType
import domain.board.*
import domain.game.Game
import domain.move.Move


/**
 * Composable used to display a Chess Board
 * @param game chess game
 * @param availableMoves all possible moves of the selected piece
 * @param onTileSelected Event to be made after a tile is selected
 */
@Composable
fun BoardView (
    game: Game,
    availableMoves: List<Move>,
    onTileSelected: (Board.Position) -> Unit
) {
    Row {
        repeat(BOARD_SIDE_LENGTH) { columnIdx ->
            Column {
                repeat(BOARD_SIDE_LENGTH) { rowIdx ->
                    val position = Board.Position(FIRST_COL + columnIdx, LAST_ROW - rowIdx)
                    Tile(position,
                         game.board.getPiece(position),
                         isAvailable = position in availableMoves.map { it.to },
                         onClick = onTileSelected
                    )
                }
            }
        }
    }
}

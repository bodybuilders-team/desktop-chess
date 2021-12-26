package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
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
@Preview
fun BoardView(
    game: Game,
    availableMoves: List<Move>,
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
                        onClick = onTileSelected
                    )
                }
            }
        }
    }
}

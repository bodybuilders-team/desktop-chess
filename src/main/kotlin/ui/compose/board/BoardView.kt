@file:Suppress("FunctionName")

package ui.compose.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.Session
import domain.SessionState
import domain.board.BOARD_SIDE_LENGTH
import domain.board.Board.Position
import domain.board.FIRST_COL
import domain.board.LAST_ROW
import domain.isLogging
import domain.isPlayable
import domain.move.Move
import ui.compose.FONT_FAMILY
import ui.compose.WINDOW_SCALE

// Constants
val BOARD_HEIGHT = TILE_SIZE * BOARD_SIDE_LENGTH
val BOARD_WIDTH = BOARD_HEIGHT

val ROWS_IDENTIFIER_WIDTH = 30.dp * WINDOW_SCALE
val COLUMNS_IDENTIFIER_HEIGHT = 34.dp * WINDOW_SCALE

private val CHARACTER_SIZE = 24.sp * WINDOW_SCALE
private val ROWS_COLS_FONT_COLOR = Color.White

/**
 * Composable used to display a Chess Board.
 *
 * @param session game session
 * @param targetsOn when true, the available move targets are on
 * @param moveTakingPlace move to be made or null
 * @param availableMoves list of available moves calculated with the current selected position
 * @param onLoggingRequest callback to be executed when the current session is in logging state
 * @param onClickedTile callback to be executed when a tile is clicked
 * @param onMakeMoveRequest callback to be executed when a piece is to be moved
 */
@Composable
fun BoardView(
    session: Session,
    targetsOn: Boolean,
    moveTakingPlace: Move?,
    availableMoves: List<Move>,
    onLoggingRequest: () -> Unit,
    onClickedTile: (Position) -> Unit,
    onMakeMoveRequest: () -> Unit
) {
    val selectedPosition = remember { mutableStateOf<Position?>(null) }

    if (session.isLogging()) {
        selectedPosition.value = null
        onLoggingRequest()
    }

    Column {
        ColumnsIdentifierView()
        Row {
            RowsIdentifierView()
            repeat(BOARD_SIDE_LENGTH) { columnIdx ->
                Column {
                    repeat(BOARD_SIDE_LENGTH) { rowIdx ->
                        val position = Position(FIRST_COL + columnIdx, LAST_ROW - rowIdx)
                        val piece = session.game.board.getPiece(position)

                        Tile(
                            position = position,
                            piece = piece,
                            isAvailable = targetsOn && position in availableMoves.map { it.to },
                            isSelected = selectedPosition.value == position && piece != null,
                            onClick = { clickedPosition ->
                                if (session.isPlayable() && moveTakingPlace == null) {
                                    selectedPosition.value = clickedPosition
                                    onClickedTile(clickedPosition)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (session.isPlayable() && moveTakingPlace != null) {
        onMakeMoveRequest()
    }

    if (session.state == SessionState.ENDED) {
        EndGamePopUp(session)
    }
}

/**
 * Composable used to display chess board column letters.
 */
@Composable
fun ColumnsIdentifierView() {
    Row(modifier = Modifier.padding(start = ROWS_IDENTIFIER_WIDTH)) {
        repeat(BOARD_SIDE_LENGTH) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(TILE_SIZE, COLUMNS_IDENTIFIER_HEIGHT)
            ) {
                Text(
                    "${FIRST_COL + it}",
                    fontFamily = FONT_FAMILY,
                    fontSize = CHARACTER_SIZE,
                    color = ROWS_COLS_FONT_COLOR
                )
            }
        }
    }
}

/**
 * Composable used to display chess board row numbers.
 */
@Composable
fun RowsIdentifierView() {
    Column {
        repeat(BOARD_SIDE_LENGTH) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(ROWS_IDENTIFIER_WIDTH, TILE_SIZE)
            ) {
                Text(
                    "${LAST_ROW - it}",
                    fontFamily = FONT_FAMILY,
                    fontSize = CHARACTER_SIZE,
                    color = ROWS_COLS_FONT_COLOR
                )
            }
        }
    }
}

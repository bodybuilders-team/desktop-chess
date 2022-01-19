package ui.compose.board

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import domain.Session
import domain.board.*
import domain.board.Board.*
import domain.game.*
import domain.*
import domain.move.Move
import ui.compose.FONT_FAMILY


// Constants
val BOARD_HEIGHT = TILE_SIZE * BOARD_SIDE_LENGTH
val BOARD_WIDTH = BOARD_HEIGHT

val ROWS_IDENTIFIER_WIDTH = 30.dp
val COLUMNS_IDENTIFIER_HEIGHT = 34.dp

private val CHARACTER_SIZE = 24.sp
private val ROWS_COLS_FONT_COLOR = Color.White


/**
 * Composable used to display a Chess Board.
 *
 * @param session game session
 * @param targetsOn when true, the available move targets are on
 * @param onMakeMoveRequest callback to be executed when a piece is to be moved
 */
@Composable
fun BoardView(
    session: Session,
    targetsOn: Boolean,
    onMakeMoveRequest: @Composable (MutableState<Move?>, MutableState<List<Move>>) -> Unit
) {
    val selectedPosition = remember { mutableStateOf<Position?>(null) }
    val move = remember { mutableStateOf<Move?>(null) }
    val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }

    if (session.isLogging()) {
        selectedPosition.value = null
        availableMoves.value = emptyList()
    }

    //println("Composed BoardView")
    
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
                            isAvailable = targetsOn && position in availableMoves.value.map { it.to },
                            isSelected = selectedPosition.value == position && piece != null,
                            onClick = { clickedPosition ->
                                if (session.isPlayable()) {
                                    selectedPosition.value = clickedPosition

                                    move.value = availableMoves.value.find { it.to == clickedPosition }
                                    
                                    if (move.value == null) 
                                        availableMoves.value = session.game.getAvailableMoves(clickedPosition)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (session.isPlayable() && move.value != null) {
        onMakeMoveRequest(move, availableMoves)
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

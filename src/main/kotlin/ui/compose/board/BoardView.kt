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
import domain.commands.PlayCommand
import domain.game.*
import domain.*
import domain.move.Move
import storage.GameStorage
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
 * @param useSelectedTile callback to be executed when a tile is selected
 */
@Composable
fun BoardView(
    session: MutableState<Session>,
    targetsOn: MutableState<Boolean>,
    useSelectedTile: @Composable (MutableState<Position?>, MutableState<List<Move>>) -> Unit
) {
    val selectedPosition = remember { mutableStateOf<Position?>(null) }
    val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }

    if (session.value.isLogging()) {
        selectedPosition.value = null
        availableMoves.value = emptyList()
    }

    Column {
        ColumnsIdentifierView()
        Row {
            RowsIdentifierView()
            repeat(BOARD_SIDE_LENGTH) { columnIdx ->
                Column {
                    repeat(BOARD_SIDE_LENGTH) { rowIdx ->
                        val position = Position(FIRST_COL + columnIdx, LAST_ROW - rowIdx)
                        val piece = session.value.game.board.getPiece(position)

                        Tile(
                            position = position,
                            piece = piece,
                            isAvailable = targetsOn.value && position in availableMoves.value.map { it.to },
                            isSelected = selectedPosition.value == position && piece != null,
                            onClick = { clickedPosition ->
                                if (session.value.isPlayable()) {
                                    selectedPosition.value = clickedPosition
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (session.value.isPlayable() && selectedPosition.value != null) {
        useSelectedTile(selectedPosition, availableMoves)
    }

    EndGamePopUp(session)
}


/**
 * Use the selected position.
 * @param selectedPosition selected position
 * @param dataBase database where the games are stored
 * @param session game session
 * @param availableMoves all possible moves of the selected position
 */
@Composable
fun UseSelectedPosition(
    selectedPosition: Position,
    dataBase: GameStorage,
    session: MutableState<Session>,
    availableMoves: MutableState<List<Move>>
) {
    val move = availableMoves.value.find { it.to == selectedPosition }

    when {
        move?.promotion != null ->
            PromotionView(session.value) { pieceSymbol ->
                makeMove(move.copy(promotion = pieceSymbol), dataBase, session, availableMoves)
            }
        move != null -> makeMove(move, dataBase, session, availableMoves)
        else -> availableMoves.value = session.value.game.getAvailableMoves(selectedPosition)
    }
}


/**
 * Makes a move in the session game, clearing the available moves.
 * @param move move to make
 * @param dataBase database where the games are stored
 * @param session game session
 * @param availableMoves all possible moves of the selected position
 */
private fun makeMove(
    move: Move,
    dataBase: GameStorage,
    session: MutableState<Session>,
    availableMoves: MutableState<List<Move>>
) {
    session.value = PlayCommand(dataBase, session.value).execute(move.toString()).getOrThrow()
    availableMoves.value = emptyList()
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

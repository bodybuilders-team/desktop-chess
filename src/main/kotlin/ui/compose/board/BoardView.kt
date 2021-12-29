package ui.compose.board

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import domain.Session
import domain.SessionState
import domain.board.*
import domain.commands.PlayCommand
import domain.game.getAvailableMoves
import domain.game.state
import domain.isLogging
import domain.isPlayable
import domain.move.Move
import storage.GameStorage


// Constants
val BOARD_HEIGHT = TILE_SIZE * BOARD_SIDE_LENGTH
val BOARD_WIDTH = BOARD_HEIGHT


/**
 * Composable used to display a Chess Board
 * @param session game session
 * @param targetsOn when true, the available move targets are on
 * @param dataBase database where the games are stored
 */
@Composable
fun BoardView(session: MutableState<Session>, targetsOn: MutableState<Boolean>, dataBase: GameStorage) {
    val selectedPosition = remember { mutableStateOf<Board.Position?>(null) }
    val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }

    if (session.value.isLogging()) {
        selectedPosition.value = null
        availableMoves.value = emptyList()
    }

    Row {
        repeat(BOARD_SIDE_LENGTH) { columnIdx ->
            Column {
                repeat(BOARD_SIDE_LENGTH) { rowIdx ->
                    val position = Board.Position(FIRST_COL + columnIdx, LAST_ROW - rowIdx)
                    Tile(
                        position,
                        piece = session.value.game.board.getPiece(position),
                        isAvailable = targetsOn.value && position in availableMoves.value.map { it.to },
                        onClick = { clickedPosition ->
                            if (session.value.isPlayable())
                                selectedPosition.value = clickedPosition
                        }
                    )
                }
            }
        }
    }

    if (session.value.isPlayable() && selectedPosition.value != null)
        UseSelectedPosition(selectedPosition.value!!, dataBase, session, availableMoves)

    EndGamePopUp(session)
}


/**
 * Composable used to show the endgame pop-up in case of a play that ends the game, i.e. checkmate.
 * @param session game session
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EndGamePopUp(session: MutableState<Session>) {
    // TODO: 29/12/2021  
    val showEnd = remember { mutableStateOf(0) }

    if (session.value.state == SessionState.ENDED && showEnd.value == 0) showEnd.value = 1

    if (showEnd.value == 1) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Game ended") },
            text = { Text("${session.value.game.state}!") },
            buttons = {
                Button(onClick = { showEnd.value = 2 }) {
                    Text("OK")
                }
            }
        )
    }
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
    selectedPosition: Board.Position,
    dataBase: GameStorage,
    session: MutableState<Session>,
    availableMoves: MutableState<List<Move>>
) {
    val move = availableMoves.value.find { it.to == selectedPosition }

    if (move != null) {
        if (move.promotion != null)
            PromotionView(session.value) { pieceSymbol ->
                makeMove(move.copy(promotion = pieceSymbol), dataBase, session, availableMoves)
            }
        else
            makeMove(move, dataBase, session, availableMoves)
    } else
        availableMoves.value = session.value.game.getAvailableMoves(selectedPosition)
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

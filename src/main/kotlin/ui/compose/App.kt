package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import domain.*
import domain.board.Board
import domain.commands.*
import domain.game.*
import domain.move.Move
import domain.pieces.Army
import storage.*


// Constants
val WINDOW_PADDING = 32.dp
val WINDOW_WIDTH = BOARD_WIDTH + MOVES_WIDTH + WINDOW_PADDING * 4
val WINDOW_HEIGHT = BOARD_HEIGHT + WINDOW_PADDING * 4 + 60.dp + GAME_INFO_HEIGHT + COLS_VIEW_HEIGHT
val BACKGROUND_COLOR = Color(0xFF2B2B2B)

val INITIAL_GAME = gameFromMoves()

val INITIAL_SESSION = Session(
    name = NO_NAME,
    state = SessionState.LOGGING,
    army = Army.WHITE,
    game = INITIAL_GAME
)


/**
 * Main Composable used to display the chess app.
 * @param dataBase database where the games are stored
 * @param options app options
 */
@Composable
@Preview
fun App(dataBase: GameStorage, options: Options) {
    val session = remember { mutableStateOf(INITIAL_SESSION) }
    if (options.closeGame.value) {
        session.value = INITIAL_SESSION
        options.closeGame.value = false
    }

    val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }
    var selectedPosition by mutableStateOf<Board.Position?>(null)
    var selectedCommand by mutableStateOf<MenuCommand?>(null)

    MaterialTheme {
        Box(modifier = Modifier.size(WINDOW_WIDTH, WINDOW_HEIGHT).background(BACKGROUND_COLOR)) {
            Row(modifier = Modifier.padding(WINDOW_PADDING)) {
                RowsView()
                Column {
                    ColumnsView()
                    Row {
                        BoardView(session.value.game, availableMoves.value, options.targetsOn) { position ->
                            selectedPosition = position
                        }
                        if (!session.value.isLogging() && selectedPosition != null)
                            UseSelectedPosition(selectedPosition!!, dataBase, session, availableMoves)

                        if (session.value.isLogging()) {
                            MenuView { option -> selectedCommand = option }

                            when (selectedCommand) {
                                in listOf(MenuCommand.OPEN, MenuCommand.JOIN) ->
                                    MenuOptionView(session, dataBase, selectedCommand!!, options.singlePlayer)
                                MenuCommand.EXIT -> ExitCommand().execute(null)
                                else -> {}
                            }

                        } else
                            MovesView(session.value.game)
                    }
                    GameInfoView(session.value)
                }
            }
        }
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


/**
 * App options
 * @property singlePlayer when true, single player mode is on
 * @property targetsOn when true, the available move targets are on
 * @property closeGame when true, the current game is closed
 */
data class Options(
    val singlePlayer: MutableState<Boolean>,
    val targetsOn: MutableState<Boolean>,
    val closeGame: MutableState<Boolean>
)

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import domain.*
import domain.board.Board
import domain.commands.*
import domain.game.*
import domain.move.Move
import domain.pieces.Army
import storage.*
import storage.mongodb.createMongoClient
import ui.compose.*
import ui.compose.menu.*


// Constants
val WINDOW_PADDING = 32.dp
val WINDOW_WIDTH = BOARD_WIDTH + MOVES_WIDTH + WINDOW_PADDING * 4
val WINDOW_HEIGHT = BOARD_HEIGHT + WINDOW_PADDING * 2 + 39.dp

const val SINGLE_PLAYER = true
val INITIAL_GAME = Game(
    Board(),
    emptyList()
)


/**
 * Main Composable used to display the chess app.
 * @param dataBase database where the games are stored
 */
@Composable
@Preview
fun App(dataBase: GameStorage) {
    val session = remember {
        mutableStateOf(
            Session(
                name = NO_NAME,
                state = SessionState.LOGGING,
                army = Army.WHITE,
                game = INITIAL_GAME
            )
        )
    }

    val availableMoves = remember { mutableStateOf<List<Move>>(emptyList()) }
    var selectedPosition by mutableStateOf<Board.Position?>(null)
    var optionSelected by mutableStateOf<String?>(null)

    MaterialTheme {
        Box(modifier = Modifier.width(WINDOW_WIDTH).height(WINDOW_HEIGHT).background(Color.Gray)) {
            Row(modifier = Modifier.padding(WINDOW_PADDING)) {

                BoardView(session.value.game, availableMoves.value) { position -> selectedPosition = position }

                if (!session.value.isLogging() && selectedPosition != null)
                    UseSelectedPosition(selectedPosition!!, dataBase, session, availableMoves)

                if (session.value.isLogging()) {
                    MenuView { option -> optionSelected = option }

                    when (optionSelected) {
                        "open" -> OpenView(session, dataBase)
                        "join" -> JoinView(session, dataBase)
                        "exit" -> ExitCommand().execute(null)
                    }

                } else
                    MovesView(session.value.game)
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
    move: Move, dataBase: GameStorage, session: MutableState<Session>, availableMoves: MutableState<List<Move>>
) {
    session.value = PlayCommand(dataBase, session.value).execute(move.toString()).getOrThrow()
    if (SINGLE_PLAYER) {
        session.value = when (session.value.game.armyToPlay) {
            Army.WHITE -> OpenCommand(dataBase).execute(session.value.name).getOrThrow()
            else -> JoinCommand(dataBase).execute(session.value.name).getOrThrow()
        }
    }
    availableMoves.value = emptyList()
}


/**
 * The application entry point.
 *
 * Execution is parameterized through the following environment variables:
 * - MONGO_DB_NAME, bearing the name of the database to be used
 * - MONGO_DB_CONNECTION, bearing the connection string to the database server. If absent, the application
 * uses a local server instance (it must be already running)
 */
fun main() {
    val dbInfo = getDBConnectionInfo()
    val driver = createMongoClient(if (dbInfo.mode == DbMode.REMOTE) dbInfo.connectionString else null)

    val dataBase = MongoDBGameStorage(tryDataBaseAccess { driver.getDatabase(System.getenv(ENV_DB_NAME)) })

    driver.use {
        application {
            Window(
                title = "Desktop Chess by Nyck, Jesus and Santos",
                state = WindowState(size = DpSize(WINDOW_WIDTH, WINDOW_HEIGHT)),
                onCloseRequest = ::exitApplication
            ) {
                App(dataBase)
            }
        }
    }
}

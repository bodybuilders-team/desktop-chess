import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import domain.NO_NAME
import domain.Session
import domain.SessionState
import domain.board.BOARD_SIDE_LENGTH
import domain.board.Board
import domain.commands.PlayCommand
import domain.game.*
import domain.move.Move
import domain.pieces.Army
import storage.*
import storage.mongodb.createMongoClient
import ui.compose.*


// Constants
val BOARD_WINDOW_HEIGHT = TILE_SIZE * BOARD_SIDE_LENGTH
val BOARD_WINDOW_WIDTH = BOARD_WINDOW_HEIGHT
val MOVES_WINDOW_WIDTH = 200.dp
val MOVES_WINDOW_HEIGHT = BOARD_WINDOW_HEIGHT
val WINDOW_PADDING = 32.dp
val WINDOW_WIDTH = BOARD_WINDOW_WIDTH + MOVES_WINDOW_WIDTH + WINDOW_PADDING * 4
val WINDOW_HEIGHT = BOARD_WINDOW_HEIGHT + WINDOW_PADDING * 2 + 39.dp


/**
 * Main Composable used to display the chess app.
 */
@Composable
@Preview
fun App() {
    val dbInfo = getDBConnectionInfo()
    val driver = createMongoClient(if (dbInfo.mode == DbMode.REMOTE) dbInfo.connectionString else null)

    driver.use {
        /*var session by mutableStateOf(
            Session(
                name = NO_NAME,
                state = SessionState.YOUR_TURN,
                army = Army.WHITE,
                game = Game(board = Board(), moves = emptyList())
            )
        )
        val dataBase = MongoDBGameStorage(tryDataBaseAccess { driver.getDatabase(System.getenv(ENV_DB_NAME)) })*/
        var game by mutableStateOf(gameFromMoves())
        var availableMoves by mutableStateOf<List<Move>>(emptyList())

        MaterialTheme {
            Box(modifier = Modifier.width(WINDOW_WIDTH).height(WINDOW_HEIGHT).background(Color.Gray)) {
                Row(modifier = Modifier.padding(WINDOW_PADDING)) {
                    BoardView(game, availableMoves) { position ->
                        availableMoves =
                            if (position in availableMoves.map { it.to }) {
                                val move = availableMoves.first { it.to == position }

                                //if (move.promotion != null) PromotionView { }

                                game = game.makeMove(move)
                                emptyList()
                            } else
                                game.getAvailableMoves(position)
                    }
                    MovesView(game)
                }
            }
        }
    }
}


/**
 * The application entry point.
 *
 * Execution is parameterized through the following environment variables:
 * - MONGO_DB_NAME, bearing the name of the database to be used
 * - MONGO_DB_CONNECTION, bearing the connection string to the database server. If absent, the application
 * uses a local server instance (it must be already running)
 */
fun main() = application {
    Window(
        title = "Desktop Chess by Nyck, Jesus and Santos",
        resizable = false,
        state = WindowState(size = DpSize(WINDOW_WIDTH, WINDOW_HEIGHT)),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}

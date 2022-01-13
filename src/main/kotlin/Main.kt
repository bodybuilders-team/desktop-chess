import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import storage.*
import ui.compose.*
import ui.compose.app.*


@Composable
fun MainWindow(appOptions: Options, dataBase: GameStorage){
    Window(
        title = "Desktop Chess by Nyck, Jesus and Santos",
        state = WindowState(
            size = DpSize(WINDOW_WIDTH, WINDOW_HEIGHT),
            position = WindowPosition(Alignment.Center)
        ),
        onCloseRequest = { appOptions.exitApp.value = true },
        icon = painterResource("chess_icon.png"),
        resizable = false
    ) {

        val session = remember { mutableStateOf(INITIAL_SESSION) }

        MenuBar(session, appOptions)
        App(session, dataBase, appOptions)
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
fun main() {
    //val dbInfo = getDBConnectionInfo()
    //val driver = createMongoClient(if (dbInfo.mode == DbMode.REMOTE) dbInfo.connectionString else null)

    //val dataBase = MongoDBGameStorage(tryDataBaseAccess { driver.getDatabase(System.getenv(ENV_DB_NAME)) })

    val dataBase = GameStorageStub()

    //driver.use {
        application {
            val appOptions = Options(
                singlePlayer = remember { mutableStateOf(true) },
                targetsOn = remember { mutableStateOf(true) },
                closeGame = remember { mutableStateOf(true) },
                exitApp = remember { mutableStateOf(false) },
                refreshGame = remember { mutableStateOf(false) }
            )

            MainWindow(appOptions, dataBase)

            if (appOptions.exitApp.value) {
                println("BYE.")
                exitApplication()
            }
        }
    //}
}

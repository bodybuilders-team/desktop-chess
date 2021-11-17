import domain.*
import domain.pieces.Army
import storage.*
import storage.mongodb.createMongoClient
import ui.console.*


/**
 * The application entry point.
 *
 * The application supports the following commands:
 * - open <game> - Opens or joins the game named <game> to play with the White pieces
 * - join <game> - Joins the game named <game> to play with the Black pieces
 * - play <move> - Makes the <move> play
 * - refresh - Refreshes the game
 * - moves - Prints all moves made
 * - exit - Ends the application
 * - help - Prints all the commands above.
 *
 * Execution is parameterized through the following environment variables:
 * - MONGO_DB_NAME, bearing the name of the database to be used
 * - MONGO_DB_CONNECTION, bearing the connection string to the database server. If absent, the application
 * uses a local server instance (it must be already running)
 */
fun main() {
    val dbInfo = getDBConnectionInfo()
    val driver =
        if (dbInfo.mode == DbMode.REMOTE) createMongoClient(dbInfo.connectionString)
        else createMongoClient()

    try {
        var chess = Session(
            name = "",
            state = SessionState.LOGGING,
            army = Army.WHITE,
            board = Board(),
            moves = emptyList()
        )
        val dataBase = MongoDBGameState(driver.getDatabase(System.getenv(ENV_DB_NAME)))

        while (true) {
            try {
                val dispatcher = buildCommandsHandler(chess, dataBase)
                val (command, parameter) = readCommand(getPrompt(chess))

                val handler = dispatcher[command]
                if (handler == null) {
                    println("Invalid command")
                    continue
                }

                val result = handler.action(parameter)
                if (result.isSuccess) {
                    chess = result.getOrThrow()
                    handler.display(chess)
                } else break

            } catch (err: Exception) {
                if (err is IllegalMoveException)
                    println("Illegal move \"${err.move}\". ${err.message}")
                else
                    println("ERROR: ${err.message}")
            }
        }

    } finally {
        println("BYE. \n")
        driver.close()
    }
}

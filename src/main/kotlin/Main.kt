import commands.buildCommands
import pieces.Color
import views.buildViews


/**
 * The application entry point.
 */
fun main() {
    /*val driver = if (checkEnvironment() == DbMode.REMOTE)
        createMongoClient(System.getenv(ENV_DB_CONNECTION))
    else
        createMongoClient()*/

    try {
        var chess = Session(name = null, state = GameState.LOGGING, army = null, board = null)

        val cmdDispatcher = buildCommands()
        val viewDispatcher = buildViews()

        while (true) {
            try {
                val (command, parameter) = readCommand(if (chess.name == null) "" else "${chess.name}:${chess.army}")

                val action = cmdDispatcher[command]
                if (action == null) {
                    println("Invalid command")
                    continue
                }

                val result = action(chess, parameter)
                if (result.isSuccess) {
                    chess = result.getOrThrow()
                    val view = viewDispatcher[command] ?: continue
                    view(chess)
                }

            } catch (err: Exception) {
                println("ERROR: ${err.message}")
            }
        }

    } finally {
        println("BYE. \n")
        //driver.close()
    }
}


/**
 * A game session.
 * @property name session name
 * @property state current session state
 * @property army session color
 * @property board current board
 */
data class Session(val name: String?, val state: GameState, val army: Color?, val board: Board?)

/**
 * Game state.
 */
enum class GameState { LOGGING, PLAYING, WAITING_FOR_OPPONENT }


/**
 * Reads the command entered by the user
 * @return Pair of command and its arguments
 */
fun readCommand(questString: String): Pair<String, String?> {
    print("$questString> ")
    val input = readLn()
    val command = input.substringBefore(' ').lowercase()
    val argument = if (' ' in input) input.substringAfter(' ') else null
    return Pair(command, argument)
}


/**
 * Let's use this while we don't get to Kotlin v1.6
 */
private fun readLn() = readLine()!!


// ------------------------- Mongo DB Stuff -------------------------
private const val ENV_DB_NAME = "MONGO_DB_NAME"
private const val ENV_DB_CONNECTION = "MONGO_DB_CONNECTION"

private enum class DbMode { LOCAL, REMOTE }

private fun checkEnvironment(): DbMode {
    requireNotNull(System.getenv(ENV_DB_NAME)) {
        "You must specify the environment variable $ENV_DB_NAME"
    }

    return if (System.getenv(ENV_DB_CONNECTION) != null)
        DbMode.REMOTE
    else
        DbMode.LOCAL
}

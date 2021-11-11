import commands.buildCommands
import game_state.MongoDBGameState
import mongodb.createMongoClient
import pieces.Army
import views.buildViews


/**
 * The application entry point.
 */
fun main() {
    val driver = if (checkEnvironment() == DbMode.REMOTE)
        createMongoClient(System.getenv(ENV_DB_CONNECTION))
    else
        createMongoClient()

    try {
        var chess = Session(
            name = null,
            state = SessionState.LOGGING,
            army = null,
            board = null,
            moves = emptyList()
        )
        val dataBase = MongoDBGameState(driver.getDatabase(System.getenv(ENV_DB_NAME)))

        val cmdDispatcher = buildCommands()
        val viewDispatcher = buildViews()

        while (true) {
            try {
                val (command, parameter) = readCommand(getPrompt(chess))

                val action = cmdDispatcher[command]
                if (action == null) {
                    println("Invalid command")
                    continue
                }

                val result = action(chess, parameter, dataBase)
                if (result.isSuccess) {
                    chess = result.getOrThrow()
                    val view = viewDispatcher[command] ?: continue
                    view(chess)
                } else break

            } catch (err: Exception) {
                if (err is IllegalMoveException) {
                    println("Illegal move \"${err.move}\". ${err.message}")
                } else println("ERROR: ${err.message}")
            }
        }

    } finally {
        println("BYE. \n")
        driver.close()
    }
}


/**
 * Returns the prompt
 * @param chess current session
 * @return prompt
 */
private fun getPrompt(chess: Session) =
    if (chess.name == null) ""
    else {
        val turn = (if (chess.state == SessionState.WAITING_FOR_OPPONENT) chess.army?.other() else chess.army).toString()
        "${chess.name}:${turn.first() + turn.substring(1).lowercase()}"
    }


// TODO(Remove !! in commands and views)
/**
 * A game session.
 * @property name session name
 * @property state current session state
 * @property army session color
 * @property board current board
 */
data class Session(
    val name: String?,
    val state: SessionState,
    val army: Army?,
    val board: Board?,
    val moves: List<Move>
)

/**
 * Game state.
 */
enum class SessionState { LOGGING, PLAYING, WAITING_FOR_OPPONENT }


/**
 * Reads the command entered by the user
 * @return Pair of command and its arguments
 */
fun readCommand(questString: String): Pair<String, String?> {
    print("$questString> ")
    val input = readLn().trim()
    val command = input.substringBefore(' ').lowercase()
    val argument = if (' ' in input) input.substringAfterLast(' ') else null
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

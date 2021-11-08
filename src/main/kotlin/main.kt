import commands.buildCommands

/**
 * TODO List:
 * - Queen checkMove
 * - Bishop checkMove
 * - substituir por rowsDistance e tal nos checkMove
 *
 * - checkMove tests for each piece
 *
 * - commands.ChessCommands
 * - MongoDBChess
 */

enum class GameState { LOGGING, PLAYING, WAITING_FOR_OPPONENT, ENDED }
data class Game(val name: String?, val state: GameState, val color: String?)

/**
 * The application entry point.
 */
fun main() {
    /*val driver = if (checkEnvironment() == DbMode.REMOTE)
        createMongoClient(System.getenv(ENV_DB_CONNECTION))
    else
        createMongoClient()*/

    var chess = Game(name = null, state = GameState.LOGGING, color = null)
    var board = Board()

    try {
        val dispatcher = buildCommands(chess, board)


        while (true) {
            val (command, argument) = readCommand(if (chess.name == null) "" else "${chess.name}:${chess.color}")

            when (command) {
                "open" -> {
                    if (argument == null) {
                        println("ERROR: Missing game name.")
                        continue
                    }

                    chess = Game(name = argument, state = GameState.PLAYING, color = "White")
                    printBoard(board)
                    println("Game ${chess.name} opened. Play with white pieces.")
                }

                "join" -> {
                    if (argument == null) {
                        println("ERROR: Missing game name.")
                        continue
                    }
                    /*if (chessGame.isUnknown()) {
                        println("Unknown game.")
                        continue
                    }*/
                }

                "play" -> {
                    if (chess.state == GameState.LOGGING) {
                        println("ERROR: Can't play without a game: try open or join commands.")
                        continue
                    }
                    if (chess.state == GameState.WAITING_FOR_OPPONENT) {
                        println("ERROR: Wait for your turn: try refresh command.")
                        continue
                    }

                    if (argument != null) {
                        board = board.makeMove(argument)
                        printBoard(board)
                        chess = chess.copy(state = GameState.WAITING_FOR_OPPONENT)
                    }
                    else println("ERROR: Missing move.")
                }

                "refresh" -> {
                    if (chess.state == GameState.LOGGING) {
                        println("ERROR: Can't refresh without a game: try open or join commands.")
                        continue
                    }
                    printBoard(board)
                }

                "moves" -> {
                    if (chess.state == GameState.LOGGING) {
                        println("No game, no moves.")
                        continue
                    }
                }

                "exit" -> {
                    chess = chess.copy(state = GameState.ENDED)
                    break
                }
                else -> println("Invalid command")
            }

        }
    } catch (err: Throwable) {
        //TODO("Create a specific Throwable regarding only errors from our own program")
        //Catches any message thrown during execution and prints it on the console
        println(err.message)
    } finally {
        println("BYE. \n")
        //driver.close()
    }
}


/**
 * Prints the board, one row in each line.
 * @param board board to print
 */
fun printBoard(board: Board) {
    println("     a b c d e f g h  ")
    println("    ----------------- ")
    board.toString().chunked(8).forEachIndexed { idx, cols ->
        print(" ${(BOARD_SIZE - idx)} | ")
        cols.forEach { print("$it ") }
        println("|")
    }
    println("    ----------------- ")
}


/**
 * Reads the command entered by the user
 * @return Pair of command and its arguments
 */
fun readCommand(questString: String): Pair<String, String?> {
    print("$questString> ")
    val input = readLn()
    val command = input.substringBefore(' ')
    val argument = input.substringAfter(' ')
    return Pair(command, argument)
}


/**
 * Let's use this while we don't get to Kotlin v1.6
 */
private fun readLn() = readLine()!!


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

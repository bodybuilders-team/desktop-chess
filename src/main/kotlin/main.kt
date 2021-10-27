/**
 * The application entry point.
 */
fun main() {
    // checkEnvironment()   // Initialize MongoDB
    // val game = readGameName()
    // val (command,argument) = readCommand()
    val chessBoard = Board()
}


/**
 * Reads from the console the name of the game the user wants to join
 * @return the name of the game
 */
fun readGameName(): String {
    print("Welcome to the Queen´s Gambit!! Please enter the name of the game you want to join: ")
    return readLn().trim()
}

/**
 * Reads the command entered by the user
 * @return Pair of command and its arguments
 */
fun readCommand(): Pair<String, String?> {
    print(">")
    val input = readLn()
    val command = input.substringBefore(' ')
    val argument = input.substringAfter(' ')
    return Pair(command, argument)
}


private fun readLn() = readLine()!!



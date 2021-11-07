

/**
 * The application entry point.
 */
fun main() {
    // checkEnvironment()   // Initialize MongoDB
    // val game = readGameName()
    // val (command,argument) = readCommand()

    var board = Board()

    board = board.makeMove("Pe7e5").makeMove("Pe2e4")

    board.toString().chunked(8).forEach { println(it) }


    /*
    while(true){
        try{
            val (command, argument) = readCommand()
            if(command == "play"){
                if(argument != null) {
                    var oldBoard = board.copy()
                    oldBoard = oldBoard.makeMove(argument)
                    //board = board.makeMove(argument)

                    oldBoard.toString().chunked(8).forEach { println(it) }
                    board.toString().chunked(8).forEach { println(it) }

                }
            }
        }
        catch (err: Throwable){
            //TODO("Create a specific Throwable regarding only errors from our own program")
            //Catches any message thrown during execution and prints it on the console
            println(err.message)
        }
    }*/
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
    print("> ")
    val input = readLn()
    val command = input.substringBefore(' ')
    val argument = input.substringAfter(' ')
    return Pair(command, argument)
}


private fun readLn() = readLine()!!


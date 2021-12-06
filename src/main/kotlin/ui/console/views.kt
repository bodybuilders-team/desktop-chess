package ui.console

import domain.*
import domain.board.*


/**
 * Representation of view
 */
typealias View = (Session) -> Unit


//TODO("Discuss checkmate views")

/**
 * Prints the board of the opened game.
 * @param game opened game
 */
fun openView(game: Session) {
    printBoard(game.board)
    println("Game ${game.name} opened. Play with white pieces.")

    if (game.state == SessionState.ENDED)
        println("Game ended.")
    else if (game.currentCheck == Check.CHECK)
        println("Your King is in check.")
}


/**
 * Prints the board of the joined game.
 * @param game joined game
 */
fun joinView(game: Session) {
    printBoard(game.board)
    println("Join to game ${game.name}. Play with black pieces.")
    if (game.state == SessionState.ENDED)
        println("Game ended.")
    else if (game.currentCheck == Check.CHECK)
        println("Your King is in check.")
}


/**
 * Prints the board after the play is made.
 * @param game game where the play happens
 */
fun playView(game: Session) {
    printBoard(game.board)
    when (game.currentCheck) {
        Check.CHECK      -> println("Enemy King is in check.")
        Check.CHECK_MATE -> println("Enemy King is in checkmate. Game ended.")
        Check.STALE_MATE -> println("Enemy King is in stalemate. Game ended.")
        else -> {}
    }

}


/**
 * Prints the board refreshed.
 * @param game current game
 */
fun refreshView(game: Session) {
    printBoard(game.board)
    if (game.state == SessionState.ENDED)
        println("Your King is in checkmate or stalemate. Game ended.")
    else if (game.currentCheck == Check.CHECK)
        println("Your King is in check.")
}


/**
 * Prints all the moves made in the game
 * @param game game where the play happens
 */
fun movesView(game: Session) {
    game.moves.forEachIndexed { index, move -> println("${index + 1}. $move") }
}


/**
 * Prints all the possible commands.
 * @param game game where the play happens
 */
fun helpView(game: Session) {
    println(
        "${game.name} -> These are the application commands you can use:\n" +
                "open <game> - Opens or joins the game named <game> to play with the White pieces\n" +
                "join <game> - Joins the game named <game> to play with the Black pieces\n" +
                "play <move> - Makes the <move> play\n" +
                "refresh     - Refreshes the game\n" +
                "moves       - Prints all moves made\n" +
                "exit        - Ends the application"
    )
}


/**
 * Prints the board, one row in each line.
 * @param board board to print
 */
fun printBoard(board: Board) {
    println("     a b c d e f g h  ")
    println("    ----------------- ")
    board.toString().chunked(BOARD_SIDE_LENGTH).forEachIndexed { idx, cols ->
        print(" ${(BOARD_SIDE_LENGTH - idx)} | ")
        cols.forEach { print("$it ") }
        println("|")
    }
    println("    ----------------- ")
}

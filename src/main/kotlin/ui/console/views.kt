package ui.console

import domain.*
import domain.board.*


/**
 * Representation of view
 */
typealias View = (Session) -> Unit


/**
 * Prints the board of the opened session.
 * @param session opened session
 */
fun openView(session: Session) {
    printBoard(session.game.board)
    println("Game ${session.name} opened. Play with white pieces.")

    if (session.state == SessionState.ENDED)
        println("Game ended.")
    else if (session.currentCheck == Check.CHECK)
        println("Your King is in check.")
}


/**
 * Prints the board of the joined session.
 * @param session joined session
 */
fun joinView(session: Session) {
    printBoard(session.game.board)
    println("Join to game ${session.name}. Play with black pieces.")
    if (session.state == SessionState.ENDED)
        println("Game ended.")
    else if (session.currentCheck == Check.CHECK)
        println("Your King is in check.")
}


/**
 * Prints the board after the play is made.
 * @param session game where the play happens
 */
fun playView(session: Session) {
    printBoard(session.game.board)
    when (session.currentCheck) {
        Check.CHECK      -> println("Enemy King is in check.")
        Check.CHECK_MATE -> println("Enemy King is in checkmate. Game ended.")
        Check.STALE_MATE -> println("Enemy King is in stalemate. Game ended.")
        else -> {}
    }

}


/**
 * Prints the board refreshed.
 * @param session current session
 */
fun refreshView(session: Session) {
    printBoard(session.game.board)
    if (session.state == SessionState.ENDED)
        println("Your King is in checkmate or stalemate. Game ended.")
    else if (session.currentCheck == Check.CHECK)
        println("Your King is in check.")
}


/**
 * Prints all the moves made in the game
 * @param session session where the play happens
 */
fun movesView(session: Session) {
    session.game.moves.forEachIndexed { index, move -> println("${index + 1}. $move") }
}


/**
 * Prints all the possible commands.
 * @param session session where the play happens
 */
fun helpView(session: Session) {
    println(
        "${session.name} -> These are the application commands you can use:\n" +
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

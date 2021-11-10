package views

import BOARD_SIDE_LENGTH
import Board
import Session


/**
 * Representation of view
 */
typealias View = (Session) -> Unit


/**
 * Returns a map of views
 * @return map of views
 */
fun buildViews(): Map<String, View> {
    return mapOf(
        "open"    to ::openView,
        "join"    to ::joinView,
        "play"    to ::playView,
        "refresh" to ::refreshView,
        "moves"   to ::movesView,
        "exit"    to { }
    )
}


/**
 * Displays the result of open command executions
 */
private fun openView(game: Session) {
    game.board?.let { printBoard(it) }
    println("Game ${game.name} opened. Play with white pieces.")
}


/**
 * Displays the result of join command executions
 */
private fun joinView(game: Session) {
    // TODO("To be implemented")
}


/**
 * Displays the result of play command executions
 */
private fun playView(game: Session) {
    game.board?.let { printBoard(it) }
}


/**
 * Displays the result of refresh command executions
 */
private fun refreshView(game: Session) {
    game.board?.let { printBoard(it) }
}


/**
 * Displays the result of moves command executions
 */
private fun movesView(game: Session) {
    // TODO("To be implemented")
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
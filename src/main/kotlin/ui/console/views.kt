package ui.console

import domain.BOARD_SIDE_LENGTH
import domain.Board
import Session


/**
 * Representation of view
 */
typealias View = (Session) -> Unit


/**
 * Prints the board of the opened game.
 * @param game opened game
 */
fun openView(game: Session) {
    game.board?.let { printBoard(it) }
    println("Game ${game.name} opened. Play with white pieces.")
}


/**
 * Prints the board of the joined game.
 * @param game joined game
 */
fun joinView(game: Session) {
    game.board?.let { printBoard(it) }
    println("Join to game ${game.name}. Play with black pieces.")
}


/**
 * Prints the board after the play is made.
 * @param game game where the play happens
 */
fun playView(game: Session) {
    game.board?.let { printBoard(it) }
}


/**
 * Prints the board refreshed.
 * @param game current game
 */
fun refreshView(game: Session) {
    game.board?.let { printBoard(it) }
}


/**
 * Prints all the moves made in the game
 * @param game game where the play happens
 */
fun movesView(game: Session) {
    game.moves.forEachIndexed { index, move -> println("${index + 1}. $move") }
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

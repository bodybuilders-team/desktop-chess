package views

import BOARD_SIDE_LENGTH
import Board
import Session


/**
 * Representation of view
 */
typealias View = (Result<Session>?) -> Unit


/**
 * Returns a map of String : View
 * @param chess current game
 * @return map of views
 */
fun buildViews(chess: Session): Map<String, View> {
    return mapOf(
        "open" to { },
        "join" to { },
        "play" to { },
        "refresh" to { },
        "moves" to {  },
        "exit" to {  }
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
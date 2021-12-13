package ui.console

import domain.*
import domain.board.*
import domain.move.Move
import domain.move.currentTurnArmy


/**
 * Representation of view
 */
typealias View = (Session) -> Unit


/**
 * Shows view after opening game.
 * @param session opened session
 */
fun openView(session: Session) {
    openingGameView(session, whiteArmy = true)
}


/**
 * Shows view after joining game.
 * @param session joined session
 */
fun joinView(session: Session) {
    openingGameView(session, whiteArmy = false)
}


/**
 * Shows view after playing a move.
 * @param session session after play
 */
fun playView(session: Session) {
    afterMoveView(session, playerTurn = false)
}


/**
 * Shows view after refreshing.
 * @param session refreshed session
 */
fun refreshView(session: Session) {
    afterMoveView(session, playerTurn = true)
}


/**
 * Shows view for an opening game command.
 * @param session session after opening game
 * @param whiteArmy if the current army playing is white
 */
private fun openingGameView(session: Session, whiteArmy: Boolean) {
    printBoard(session.game.board)
    println("${if (whiteArmy) "Opened" else "Joined"} game ${session.name}. Play with ${if (whiteArmy) "white" else "black"} pieces.")
    if(session.currentCheck != Check.NO_CHECK){
        println(
            when (session.currentCheck) {
                Check.CHECK     -> "Your King is in check."
                Check.CHECKMATE -> "Game ended in checkmate, ${currentTurnArmy(session.game.moves).toString().lowercase()} won!"
                Check.STALEMATE -> "Game ended in stalemate, it's a draw!"
                Check.NO_CHECK   -> ""
            }
        )
    }
}


/**
 * Shows view for a command that makes a move in the board (Play or Refresh).
 * @param session session after making a move in the board
 * @param playerTurn if it's the player's turn to play
 */
private fun afterMoveView(session: Session, playerTurn: Boolean) {
    printBoard(session.game.board)
    if(session.currentCheck != Check.NO_CHECK){
        println(
            "${if (playerTurn) "Your" else "Enemy"} King is in " +
                    when (session.currentCheck) {
                        Check.CHECK     -> "check."
                        Check.CHECKMATE -> "checkmate. Game ended, you ${if (playerTurn) "lose" else "win"}!"
                        Check.STALEMATE -> "stalemate. Game ended, it's a draw!"
                        Check.NO_CHECK   -> ""
                    }
        )
    }
}


/**
 * Prints all the moves made in the game
 * @param session current session
 */
fun movesView(session: Session) {
    session.game.moves.forEachIndexed { index, move -> println("${index + 1}. $move") }
}


/**
 * Prints all the possible commands.
 * @param session current session
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


//TODO("Comment")
fun viewGameExecution(vararg movesInString: String) {
    var newGame = Game(Board(), emptyList())

    printBoard(newGame.board)

    movesInString.forEach { moveInString ->
        println("Next move: $moveInString")
        
        readLine()!!
        println("\n\n".repeat(30))
        
        newGame = newGame.makeMove(Move.validated(moveInString, newGame))
        
        printBoard(newGame.board)
    }
}

fun main(){
    viewGameExecution("f3", "e5", "g4", "Qh4")
}

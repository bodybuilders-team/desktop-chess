package commands

import Board
import Game
import printBoard
import kotlin.system.exitProcess

typealias Command = (String?) -> Unit

fun buildCommands(chess: Game, board: Board): Map<String, Command> {
    return mapOf(
        "open"    to { open(chess, board, it) },
        "join"    to { join(it) },
        "play"    to { play(chess, board, it) },
        "refresh" to { refresh(chess, board) },
        "moves"   to { moves(chess) },
        "exit"    to { exit() }
    )
}


private fun open(chess: Game, board: Board, parameter: String?) {
    if (parameter == null) {
        println("ERROR: Missing game name.")
        return
    }

    printBoard(board)
    println("Game ${chess.name} opened. Play with white pieces.")
    //Game(name = parameter, state = GameState.PLAYING, color = "White")

}


private fun join(parameter: String?) {
    if (parameter == null) {
        println("ERROR: Missing game name.")
        return
    }
    /*if (chessGame.isUnknown()) {
        println("Unknown game.")
        continue
    }*/
}


private fun play(chess: Game, board: Board, parameter: String?) {
    if (chess.state == GameState.LOGGING) {
        println("ERROR: Can't play without a game: try open or join commands.")
        return
    }
    if (chess.state == GameState.WAITING_FOR_OPPONENT) {
        println("ERROR: Wait for your turn: try refresh command.")
        return
    }

    if (parameter != null) {
        //board = board.makeMove(parameter)
        printBoard(board)
        //chess = chess.copy(state = GameState.WAITING_FOR_OPPONENT)
    } else println("ERROR: Missing move.")

}


private fun refresh(chess: Game, board: Board) {
    if (chess.state == GameState.LOGGING) {
        println("ERROR: Can't refresh without a game: try open or join commands.")
        return
    }
    printBoard(board)

}


private fun moves(chess: Game) {
    if (chess.state == GameState.LOGGING) {
        println("No game, no moves.")
        return
    }
}


private fun exit() {
    exitProcess(0)
}

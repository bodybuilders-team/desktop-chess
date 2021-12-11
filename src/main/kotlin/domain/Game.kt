package domain

import domain.board.*
import domain.move.*


/**
 * A chess game.
 * @property board game board
 * @property moves previously played moves
 */
data class Game(
    val board: Board,
    val moves: List<Move>
)


/**
 * Returns a new game with the moves [movesInString] consecutively made and validated in the board.
 * @param movesInString moves in string
 * @return new game with the moves [movesInString] consecutively made and validated in the board
 */
fun gameFromMoves(vararg movesInString: String): Game {
    var newBoard = Board()
    val previousMoves = mutableListOf<Move>()

    movesInString.forEach { moveInString ->
        val validatedMove = Move.validated(moveInString, newBoard, previousMoves)
        newBoard = newBoard.makeMove(validatedMove)
        previousMoves.add(validatedMove)
    }

    return Game(board = newBoard, moves = previousMoves)
}

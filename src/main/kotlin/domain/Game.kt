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
 * Returns a new game with the moves [movesInString] consecutively made and validated in the game.
 * @param movesInString moves in string
 * @return new game with the moves [movesInString] consecutively made and validated in the game
 */
fun gameFromMoves(vararg movesInString: String): Game {
    var newGame = Game(Board(), emptyList())

    movesInString.forEach { moveInString ->
        val validatedMove = Move.validated(moveInString, newGame)
        newGame = Game(board = newGame.board.makeMove(validatedMove), moves = newGame.moves + validatedMove)
    }

    return newGame
}

import domain.board.Board
import domain.game.Game
import domain.game.gameFromMoves
import domain.move.IllegalMoveException
import domain.move.Move

val defaultGameResultingInBlackCheck = gameFromMoves("c3", "d6", "a3", "e6", "Qa4")
val defaultGameResultingInWhiteCheck = gameFromMoves("d3", "c6", "a3", "Qa5")

// Fool's mate! 🤡 - https://www.chess.com/article/view/fastest-chess-checkmates
val defaultGameResultingInCheckMate = gameFromMoves("f3", "e5", "g4", "Qh4")

// Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
val defaultGameResultingInStaleMate = gameFromMoves(
    "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
    "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
)
val defaultGameResultingInTie = gameFromMoves(List(25) { listOf("Nb1c3", "Nb8c6", "Nc3b1", "Nc6b8") }.flatten())

/**
 * Checks if a move [moveInString] is valid.
 * Returns false when an [IllegalMoveException] is thrown.
 *
 * ONLY USED IN TESTS. Has no knowledge of previous moves in the board.
 *
 * @param moveInString piece move in string
 * @return true if the move is valid
 */
fun Board.isValidMove(moveInString: String): Boolean {
    try {
        Move.validated(moveInString, Game(this, emptyList()))
    } catch (err: IllegalMoveException) {
        return false
    }
    return true
}

/**
 * Returns a list of moves from moves [movesInString].
 * @return list of moves from moves [movesInString]
 */
fun listOfMoves(vararg movesInString: String) = movesInString.map { Move(it) }

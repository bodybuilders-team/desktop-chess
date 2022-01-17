package domain.game

import domain.board.Board
import domain.move.Move


/**
 * Makes the moves [movesInString] in the game.
 * @param movesInString moves to make
 * @return new game with the moves [movesInString] made in the game.
 */
fun Game.makeMoves(movesInString: List<String>): Game =
    movesInString.fold(this.copy()) { newGame, moveInString ->
        newGame.makeMove(Move.validated(moveInString, newGame))
    }


/**
 * Returns a new game with the moves [movesInString] consecutively made and validated in the game.
 * @param movesInString moves to make
 * @return new game with the moves [movesInString] consecutively made and validated in the game
 */
fun gameFromMoves(movesInString: List<String>): Game =
    Game(Board(), emptyList()).makeMoves(movesInString)


/**
 * Returns a new game with the [moves] consecutively made and validated in the game.
 * @param moves moves to make
 * @return new game with the [moves] consecutively made and validated in the game
 */
@JvmName("gameFromMovesListOfMove")
fun gameFromMoves(moves: List<Move>): Game =
    gameFromMoves(moves.map { it.toString() })


/**
 * Returns a new game with the moves [movesInString] consecutively made and validated in the game.
 * @param movesInString moves to make in string
 * @return new game with the moves [movesInString] consecutively made and validated in the game
 */
fun gameFromMoves(vararg movesInString: String): Game =
    gameFromMoves(movesInString.toList())


/**
 * Returns a game obtained from a FEN string (Forsyth-Edwards Notation).
 * @param fen FEN in string
 * @return game obtained from a FEN string
 */
fun gameFromFEN(fen: String): Game {
    // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

    val boardRepresentation = fen.substringBefore(" ")

    val boardInString = boardRepresentation.replace("/", "")
        .map { if (it in '1'..'8') " ".repeat(it.digitToInt()) else it.toString() }
        .joinToString(separator = "")

    val (armyToPlay, castlesAvailable,
        enPassantTarget, movesSinceCapture, totalMoves) = fen.substringAfter(" ").split(" ")

    val moves = mutableListOf<String>()

    if ('K' !in castlesAvailable) moves.add("Rh1f1")
    if ('Q' !in castlesAvailable) moves.add("Ra1b1")
    if ('k' !in castlesAvailable) moves.add("Rh8f8")
    if ('q' !in castlesAvailable) moves.add("Ra8b8")

    if (enPassantTarget != "-") {
        val enPassantCol = enPassantTarget[0]
        val enPassantRow = enPassantTarget[1]
        moves.add(
            "P$enPassantCol${if (enPassantRow == '6') "7" else "2"}$enPassantCol${if (enPassantRow == '6') "5" else "4"}"
        )
    }
    if ((moves.size % 2 == 0) != (armyToPlay == "w"))
        moves.add(0, "Pe2e4")

    return Game(Board(boardInString), moves.map { Move(it) })
}
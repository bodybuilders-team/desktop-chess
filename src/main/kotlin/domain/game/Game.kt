package domain.game

import domain.board.*
import domain.move.*
import domain.pieces.Army


/**
 * A chess game.
 * @property board game board
 * @property moves previously played moves
 */
data class Game(
    val board: Board,
    val moves: List<Move>
) {
    override fun toString(): String {
        return board.toString().chunked(BOARD_SIDE_LENGTH).joinToString("\n")
    }
}


/**
 * Different states the game can be in.
 */
enum class GameState {
    NO_CHECK,
    CHECK,
    CHECKMATE,
    STALEMATE,
    TIE
}


/**
 * Makes a move in the game.
 * @param move move to make
 * @return new game with the move made
 */
fun Game.makeMove(move: Move) =
    Game(board = board.makeMove(move), moves = moves + move)


/**
 * Returns the army playing in the current turn.
 * @return the army playing in the current turn
 */
val Game.armyToPlay
    get() = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Gets the current state of the game.
 * @return state of the game
 */
val Game.state
    get() =
        when {
            board.isKingInCheckMate(Army.WHITE) || board.isKingInCheckMate(Army.BLACK)  -> GameState.CHECKMATE
            isKingInStaleMate(Army.WHITE) || isKingInStaleMate(Army.BLACK)              -> GameState.STALEMATE
            isTiedByFiftyMoveRule()                                                     -> GameState.TIE
            board.isKingInCheck(Army.WHITE) || board.isKingInCheck(Army.BLACK)          -> GameState.CHECK
            else -> GameState.NO_CHECK
        }


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

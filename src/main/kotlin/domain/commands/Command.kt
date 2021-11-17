package domain.commands

import domain.Session
import domain.Board
import domain.Move
import domain.pieces.*


/**
 * Representation of command
 */
fun interface Command {

    /**
     * Executes this command passing it the given parameter
     * @param parameter the commands' parameter, or null, if no parameter has been passed
     */
    fun execute(parameter: String?): Result<Session>

    /**
     * Overload of invoke operator, for convenience.
     */
    operator fun invoke(parameter: String? = null) = execute(parameter)
}


/**
 * Returns the army playing in the current turn
 * @param moves all game moves
 */
fun currentTurnArmy(moves: List<Move>) = if (moves.size % 2 == 0) Army.WHITE else Army.BLACK


/**
 * Returns true if it's the white army turn
 * @param moves all game moves
 */
fun isWhiteTurn(moves: List<Move>) = currentTurnArmy(moves) == Army.WHITE


/**
 * Returns a new board with all the moves in [moves]
 * @param moves moves to make
 * @return board with moves
 */
fun boardWithMoves(moves: List<Move>): Board {
    var newBoard = Board()
    moves.forEach { move -> newBoard = newBoard.makeMove(move) }
    return newBoard
}

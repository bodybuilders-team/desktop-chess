package domain.commands

import domain.*
import domain.board.Board
import domain.move.Move
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * Representation of command
 */
fun interface Command {

    /**
     * Executes this command passing it the given parameter
     * @param parameter the commands' parameter, or null, if no parameter has been passed
     * @return result with updated session
     */
    fun execute(parameter: String?): Result<Session>

    /**
     * Overload of invoke operator, for convenience.
     */
    operator fun invoke(parameter: String? = null) = execute(parameter)
}


/**
 * Returns a new board with all the moves in [moves]
 * @param moves moves to make
 * @return board with moves
 */
fun boardWithMoves(moves: List<Move>): Board {
    var newBoard = Board()
    val previousMoves = mutableListOf<Move>()
    moves.forEach { move ->
        val validatedMove = Move.validated(move.toString(), newBoard, previousMoves)
        newBoard = newBoard.makeMove(validatedMove)
        previousMoves.add(validatedMove)
    }
    return newBoard
}


/**
 * Exception thrown when an error occurs while executing a command.
 * @param message cause of the error
 */
class CommandException(override val message: String?) : Exception(message)


/**
 * Throws an [CommandException] with the result of calling [lazyMessage] if the [value] is false.
 */
fun cmdRequire(value: Boolean, lazyMessage: () -> Any) {
    if (!value) throw CommandException(lazyMessage().toString())
}


/**
 * Throws an [CommandException] with the result of calling lazyMessage if the value is null.
 * Otherwise, returns the not null value.
 */
@OptIn(ExperimentalContracts::class)
fun <T> cmdRequireNotNull(value: T?, lazyMessage: () -> Any): T {
    contract {
        returns() implies (value != null)
    }

    if (value == null) throw CommandException(lazyMessage().toString())
    return value
}

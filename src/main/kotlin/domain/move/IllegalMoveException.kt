package domain.move

/**
 * Exception thrown when an Illegal move is made by one of the users
 * @param move Illegal move made
 * @param message Used to specify what went wrong
 */
class IllegalMoveException(val move: String, override val message: String) : Exception(message)

package commands

enum class Result {
    CONTINUE,
    EXIT
}

/**
 * Desktop Chess commands.
 */
interface ChessCommands {

    /**
     * Opens a new game with a new name unless a game with that name already exists
     * @param gameId the game´s name
     */
    fun open(gameId: String): Result

    /**
     * Used by a second user to open the game with the name [gameId]
     * @param gameId the game´s name
     */
    fun join(gameId: String): Result

    /**
     * Executes a move command if it corresponds to the rules
     * @param stringMove the play to be made
     */
    fun play(stringMove: String): Result

    /**
     * Updates the state of the game in MongoDB
     */
    fun refresh(): Result

    /**
     * Lists all moves made since the beggining of the game
     */
    fun moves(): Result

    /**
     * Terminates the application by saving the actual state of the game
     */
    fun exit(): Result
}

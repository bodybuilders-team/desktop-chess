

/**
 * Desktop Chess commands.
 */
interface ChessCommands {

    /**
     * Opens a new game with a new name unless a game with that name already exists
     * @param gameId the game´s name
     */
    fun open(gameId: String)

    /**
     * used by a second user to open the game with the name [gameId]
     * @param gameId the game´s name
     */
    fun join(gameId: String)

    /**
     * Executes a move command if it corresponds to the rules
     * @param move the play to be made
     */
    fun play(move: Move)

    /**
     * Updates the state of the game in MongoDB
     */
    fun refresh()

    /**
     * Lists all moves made since the beggining of the game
     * @return all moves correspondent to the current game present in MongoDB
     */
    fun moves(): Iterable<Move>

    /**
     * Terminates the application by saving the actual state of the game
     */
    fun exit()
}
package game_state

import Move

interface GameState {
    /**
     * Returns the last move from game [game]
     * @param game game name
     * @return the last move in string
     */
    fun getLastMove(game: String): Move

    /**
     * Returns all the moves from game [game]
     * @param game game name
     * @return list of moves in string
     */
    fun getAllMoves(game: String): List<Move>

    /**
     * Post a move in the game [game]
     * @param game game name
     * @param move move in string to post
     * @return true if the operation was successful
     */
    fun postMove(game: String, move: Move): Boolean

    /**
     * Creates a game
     * @param game game name
     * @return true if the operation was successful
     */
    fun createGame(game: String): Boolean

    /**
     * Returns the game name specified or null if it doesn't exist
     * @param game game name
     * @return game name
     */
    fun getGame(game: String): String?
}
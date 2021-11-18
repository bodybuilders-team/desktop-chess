package storage

import domain.Move


/**
 * The GameState contract
 */
interface GameState {

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
     */
    fun createGame(game: String)

    /**
     * Checks if the game with the specified name exists
     * @param game game name
     * @return true if the game exists
     */
    fun gameExists(game: String): Boolean
}

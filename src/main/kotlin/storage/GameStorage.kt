package storage

import domain.move.Move

/**
 * The GameState contract
 */
interface GameStorage {

    /**
     * Returns all the moves from game [gameName]
     * @param gameName game name
     * @return list of moves in string
     */
    suspend fun getAllMoves(gameName: String): List<Move>

    /**
     * Post a move in the game [gameName]
     * @param gameName game name
     * @param move move in string to post
     * @return true if the operation was successful
     */
    suspend fun postMove(gameName: String, move: Move)

    /**
     * Creates a game
     * @param gameName game name
     */
    suspend fun createGame(gameName: String)

    /**
     * Checks if the game with the specified name exists
     * @param gameName game name
     * @return true if the game exists
     */
    suspend fun gameExists(gameName: String): Boolean
}

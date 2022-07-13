package storage

import domain.move.Move

/**
 * [GameStorage] implementation for testing.
 */
class GameStorageStub : GameStorage {
    private val database: MutableMap<String, MutableList<Move>> = mutableMapOf()

    override suspend fun getAllMoves(gameName: String): List<Move> {
        require(gameExists(gameName)) { "A game with the name \"$gameName\" does not exist." }
        return database[gameName]!!
    }

    override suspend fun postMove(gameName: String, move: Move) {
        require(gameExists(gameName)) { "A game with the name \"$gameName\" does not exist." }
        database[gameName]?.add(move)
    }

    override suspend fun createGame(gameName: String) {
        require(!gameExists(gameName)) { "A game with the name \"$gameName\" already exists." }
        database[gameName] = mutableListOf()
    }

    override suspend fun gameExists(gameName: String): Boolean {
        return gameName in database
    }
}

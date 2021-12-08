package storage

import com.mongodb.MongoException
import domain.move.Move
import com.mongodb.client.MongoDatabase
import storage.mongodb.*


/**
 * Tries to execute a code block. If a MongoException is caught, throws a GameStateAccessException.
 * @throws GameStateAccessException if a MongoException is caught
 */
fun <T> tryDataBaseAccess(codeBlock: () -> T) =
    try {
        codeBlock()
    } catch (err: MongoException) {
        throw GameStateAccessException(err.message ?: "")
    }


/**
 * Implementation of GameState with MongoDB.
 * @property db Mongo database
 */
class MongoDBGameState(private val db: MongoDatabase) : GameState {

    override fun getAllMoves(game: String): List<Move> {
        return tryDataBaseAccess { db.getCollectionWithId<Move>(game).getAll().toList() }
    }

    override fun postMove(game: String, move: Move): Boolean {
        return tryDataBaseAccess { db.createDocument(game, move) }
    }

    override fun createGame(game: String) {
        require(!gameExists(game))
        tryDataBaseAccess { db.createCollection(game) }
    }

    override fun gameExists(game: String): Boolean {
        return tryDataBaseAccess { game in db.getRootCollectionsIds() }
    }
}

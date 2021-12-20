package storage

import com.mongodb.MongoException
import domain.move.Move
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.replaceOne
import storage.mongodb.*


/**
 * Tries to execute a code block. If a MongoException is caught, throws a GameStateAccessException.
 * @throws GameStorageAccessException if a MongoException is caught
 */
fun <T> tryDataBaseAccess(codeBlock: () -> T) =
    try {
        codeBlock()
    } catch (err: MongoException) {
        throw GameStorageAccessException(err.message ?: "")
    }


const val COLLECTION_ID = "games"


/**
 * Storage game with name and played moves.
 * @property name game name
 * @property moves previously played moves
 */
private data class StoredGame(val name: String, val moves: List<String>)


/**
 * Implementation of GameState with MongoDB.
 * @property db Mongo database
 */
class MongoDBGameStorage(private val db: MongoDatabase) : GameStorage {

    override fun getAllMoves(gameName: String): List<Move> {
        require(gameExists(gameName)) { "A game with the name \"$gameName\" does not exist." }
        
        return tryDataBaseAccess {
            db.getAllDocuments<StoredGame>(COLLECTION_ID).first { it.name == gameName }.moves.map { Move(it) }
        }
    }

    override fun postMove(gameName: String, move: Move): Boolean {
        require(gameExists(gameName)) { "A game with the name \"$gameName\" does not exist." }
        
        return tryDataBaseAccess {
            val oldStoredGame = db.getAllDocuments<StoredGame>(COLLECTION_ID).first { it.name == gameName }
            db.getCollectionWithId<StoredGame>(COLLECTION_ID).replaceOne(
                "{name: \"$gameName\"}", oldStoredGame.copy(moves = oldStoredGame.moves + move.toString())
            )
            true
        }
    }

    override fun createGame(gameName: String) {
        require(!gameExists(gameName)) { "A game with the name \"$gameName\" already exists." }
        tryDataBaseAccess {
            db.createDocument(COLLECTION_ID, StoredGame(name = gameName, moves = emptyList()))
        }
    }

    override fun gameExists(gameName: String): Boolean {
        return tryDataBaseAccess {
            db.getAllDocuments<StoredGame>(COLLECTION_ID).any { it.name == gameName }
        }
    }
}

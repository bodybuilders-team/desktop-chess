package storage

import com.mongodb.MongoException
import domain.move.Move
import com.mongodb.client.MongoDatabase
import storage.mongodb.*


/**
 * Tries to execute a code block. If a MongoException is caught, throws a GameStateAccessException.
 * @throws GameStorageAccessException if a MongoException is caught
 */
suspend fun <T> tryDataBaseAccess(codeBlock: suspend () -> T) =
    try {
        codeBlock()
    } catch (err: MongoException) {
        throw GameStorageAccessException(err.message ?: "")
    }


private const val GAMES_COLLECTION_ID = "games"


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

    override suspend fun getAllMoves(gameName: String): List<Move> {
        require(gameExists(gameName)) { "A game with the name \"$gameName\" does not exist." }

        return tryDataBaseAccess {
            db.getAllDocuments<StoredGame>(GAMES_COLLECTION_ID).first { it.name == gameName }.moves.map { Move(it) }
        }
    }

    override suspend fun postMove(gameName: String, move: Move) {
        require(gameExists(gameName)) { "A game with the name \"$gameName\" does not exist." }

        return tryDataBaseAccess {
            val oldStoredGame = db.getAllDocuments<StoredGame>(GAMES_COLLECTION_ID).first { it.name == gameName }

            db.replaceDocument(
                GAMES_COLLECTION_ID,
                "{name: \"$gameName\"}", oldStoredGame.copy(moves = oldStoredGame.moves + move.toString())
            )
        }
    }

    override suspend fun createGame(gameName: String) {
        require(!gameExists(gameName)) { "A game with the name \"$gameName\" already exists." }
        tryDataBaseAccess {
            db.createDocument(GAMES_COLLECTION_ID, StoredGame(name = gameName, moves = emptyList()))
        }
    }

    override suspend fun gameExists(gameName: String): Boolean {
        return tryDataBaseAccess {
            db.getAllDocuments<StoredGame>(GAMES_COLLECTION_ID).any { it.name == gameName }
        }
    }
}

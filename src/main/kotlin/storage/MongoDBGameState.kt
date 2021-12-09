package storage

import com.mongodb.MongoException
import domain.move.Move
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.replaceOne
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

const val COLLECTION_ID = "games"
private data class Game(val name: String, val moves: List<String>)


/**
 * Implementation of GameState with MongoDB.
 * @property db Mongo database
 */
class MongoDBGameState(private val db: MongoDatabase) : GameState {

    override fun getAllMoves(gameName: String): List<Move> {
        return tryDataBaseAccess {
            db.getAllDocuments<Game>(COLLECTION_ID).first { it.name == gameName }.moves.map { Move(it) }
        }
    }

    override fun postMove(gameName: String, move: Move): Boolean {
        return tryDataBaseAccess {
            val oldGame = db.getAllDocuments<Game>(COLLECTION_ID).first { it.name == gameName }
            db.getCollectionWithId<Game>(COLLECTION_ID).replaceOne(
                "{name: \"$gameName\"}", oldGame.copy(moves = oldGame.moves + move.toString())
            )
            true
        }
    }

    override fun createGame(gameName: String) {
        require(!gameExists(gameName))
        tryDataBaseAccess {
            db.createDocument(COLLECTION_ID, Game(name = gameName, moves = emptyList()))
        }
    }

    override fun gameExists(gameName: String): Boolean {
        return tryDataBaseAccess {
            db.getAllDocuments<Game>(COLLECTION_ID).any { it.name == gameName }
        }
    }
}

package storage

import domain.Move
import com.mongodb.client.MongoDatabase
import storage.mongodb.createDocument
import storage.mongodb.getAll
import storage.mongodb.getCollectionWithId


/**
 * Implementation of GameState with MongoDB.
 * @property db Mongo database
 */
class MongoDBGameState(private val db: MongoDatabase) : GameState {

    override fun getLastMove(game: String): Move {
        return getAllMoves(game).last()
    }

    override fun getAllMoves(game: String): List<Move> {
        return db.getCollectionWithId<Move>(game).getAll().toList()
    }

    override fun postMove(game: String, move: Move): Boolean {
        return db.createDocument(game, move)
    }

    override fun createGame(game: String): Boolean {
        db.createCollection(game)
        return true
    }

    override fun getGame(game: String): String? {
        return if (game !in db.listCollectionNames()) null else game
    }
}

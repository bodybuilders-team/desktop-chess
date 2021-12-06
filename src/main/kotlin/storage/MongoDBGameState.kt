package storage

import domain.move.Move
import com.mongodb.client.MongoDatabase
import storage.mongodb.*


/**
 * Implementation of GameState with MongoDB.
 * @property db Mongo database
 */
class MongoDBGameState(private val db: MongoDatabase) : GameState {

    override fun getAllMoves(game: String): List<Move> {
        return db.getCollectionWithId<Move>(game).getAll().toList()
    }

    override fun postMove(game: String, move: Move): Boolean {
        return db.createDocument(game, move)
    }

    override fun createGame(game: String) {
        require(!gameExists(game))
        db.createCollection(game)
    }

    override fun gameExists(game: String): Boolean {
        return game in db.getRootCollectionsIds()
    }
}

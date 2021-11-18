package storage

import com.mongodb.MongoException


/**
 * Exception thrown when the mongo database fails
 * @param message Used to specify what went wrong
 */
class GameStateAccessException(message: String): MongoException(message)

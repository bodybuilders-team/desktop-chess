package storage.mongodb

import com.mongodb.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.KMongo
import org.litote.kmongo.replaceOne


/**
 * Creates an instance of the mongo db client driver. The instance must be closed when no longer needed.
 *
 * @return  the driver instance
 */
fun createMongoClient(connectionString: String? = null): MongoClient =
    if (connectionString == null) KMongo.createClient()
    else KMongo.createClient(connectionString)


/**
 * Extension function of [MongoDatabase] that gets the collection with the given identifier. The generic parameter <T>
 * is the type of the documents contained in the collection.
 *
 * @param   id    the collection identifier
 * @return  the corresponding [MongoCollection<T>] instance
 */
suspend inline fun <reified T : Any> MongoDatabase.getCollectionWithId(id: String): MongoCollection<T> =
    withContext(Dispatchers.IO) {
        this@getCollectionWithId.getCollection(id, T::class.java)
    }


/**
 * Extension function of [MongoDatabase] that creates a document with [document] contents and adds it to the collection
 * identified by [parentCollectionId]. The generic parameter <T> is the type of the document to be created.
 *
 * @param   parentCollectionId  the identifier of the collection where the document will be created
 * @param   document            the object bearing the document data
 * @return  a boolean value indicating if the creation was successful (true), or not (false)
 */
suspend inline fun <reified T : Any> MongoDatabase.createDocument(parentCollectionId: String, document: T): Boolean =
    withContext(Dispatchers.IO) {
        getCollectionWithId<T>(parentCollectionId).insertOne(document).wasAcknowledged()
    }


/**
 * Extension function of [MongoDatabase] that gets the names of all collections at the root of the database.
 *
 * @return  the names of the root collections
 */
suspend fun MongoDatabase.getRootCollectionsIds(): Iterable<String> =
    withContext(Dispatchers.IO) {
        this@getRootCollectionsIds.listCollectionNames()
    }


/**
 * Extension function of [MongoCollection<T>] that creates a document with [document]'s contents and adds it to this
 * collection. The generic parameter <T> is the type of the document to be created.
 *
 * @param document the object bearing the document data
 * @return  a boolean value indicating if the creation was successful (true), or not (false)
 */
suspend fun <T> MongoCollection<T>.createDocument(document: T): Boolean =
    withContext(Dispatchers.IO) {
        this@createDocument.insertOne(document).wasAcknowledged()
    }


/**
 * Extension function of [MongoCollection<T>] that returns all the documents in this collection. The generic parameter
 * <T> is the type of the documents contained in the collection.
 *
 * @return  the documents in the collection
 */
suspend fun <T> MongoCollection<T>.getAll(): Iterable<T> =
    withContext(Dispatchers.IO) {
        this@getAll.find()
    }


/**
 * Extension function of [MongoDatabase] that gets all documents from a collection.
 * @param id collection id
 * @return the documents in the collection
 */
suspend inline fun <reified T : Any> MongoDatabase.getAllDocuments(id: String): Iterable<T> =
    withContext(Dispatchers.IO) {
        getCollectionWithId<T>(id).getAll()
    }

/**
 * Extension function of [MongoDatabase] that replaces a document from a collection.
 * @param id collection id
 * @param filter the query filter to apply to the replace operation
 * @param replacement the replacement document
 * @return the documents in the collection
 */
suspend inline fun <reified T : Any> MongoDatabase.replaceDocument(id: String, filter: String, replacement: T) =
    withContext(Dispatchers.IO) {
        getCollectionWithId<T>(id).replaceOne(filter, replacement)
    }

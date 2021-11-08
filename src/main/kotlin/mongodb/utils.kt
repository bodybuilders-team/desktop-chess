package mongodb

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo


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
inline fun <reified T : Any> MongoDatabase.getCollectionWithId(id: String): MongoCollection<T> =
    this.getCollection(id, T::class.java)

/**
 * Extension function of [MongoDatabase] that creates a document with [document] contents and adds it to the collection
 * identified by [parentCollectionId]. The generic parameter <T> is the type of the document to be created.
 *
 * @param   parentCollectionId  the identifier of the collection where the document will be created
 * @param   document            the object bearing the document data
 * @return  a boolean value indicating if the creation was successful (true), or not (false)
 */
inline fun <reified T : Any> MongoDatabase.createDocument(parentCollectionId: String, document: T): Boolean =
    getCollectionWithId<T>(parentCollectionId).insertOne(document).wasAcknowledged()

/**
 * Extension function of [MongoDatabase] that creates gets the names of all collections at the root of the database.
 *
 * @return  the names ot the root collections
 */
fun MongoDatabase.getRootCollectionsIds(): Iterable<String> = this.listCollectionNames()

/**
 * Extension function of [MongoCollection<T>] that creates a document with [document]'s contents and adds it to this
 * collection. The generic parameter <T> is the type of the document to be created.
 *
 * @param   document            the object bearing the document data
 * @return  a boolean value indicating if the creation was successful (true), or not (false)
 */
fun <T> MongoCollection<T>.createDocument(document: T): Boolean = this.insertOne(document).wasAcknowledged()

/**
 * Extension function of [MongoCollection<T>] that returns all the documents in this collection. The generic parameter
 * <T> is the type of the documents contained in the collection.
 *
 * @return  the documents in the collection
 */
fun <T> MongoCollection<T>.getAll(): Iterable<T> = this.find()

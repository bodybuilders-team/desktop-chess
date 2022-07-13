package storageTests

import com.mongodb.MongoException
import storage.GameStorageAccessException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameStorageAccessExceptionTests { // [✔]
    @Test
    fun `Thrown GameStateAccessException is caught as Exception`() {
        assertFailsWith<Exception> {
            throw GameStorageAccessException("Error accessing.")
        }
    }

    @Test
    fun `Thrown GameStateAccessException is caught as MongoException`() {
        assertFailsWith<MongoException> {
            throw GameStorageAccessException("Error accessing.")
        }
    }

    @Test
    fun `Thrown GameStateAccessException is caught as GameStateAccessException`() {
        assertFailsWith<GameStorageAccessException> {
            throw GameStorageAccessException("Error accessing.")
        }
    }

    @Test
    fun `GameStateAccessException message is thrown correctly`() {
        val message = "Error accessing."
        try {
            throw GameStorageAccessException(message)
        } catch (err: GameStorageAccessException) {
            assertEquals(message, err.message)
        }
    }
}

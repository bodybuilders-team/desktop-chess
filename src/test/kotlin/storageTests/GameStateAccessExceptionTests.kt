package storageTests

import com.mongodb.MongoException
import storage.GameStateAccessException
import kotlin.test.*

class GameStateAccessExceptionTests {
    @Test
    fun `GameStateAccessException is Exception`() {
        assertTrue(GameStateAccessException("Error accessing.") is Exception)
    }

    @Test
    fun `GameStateAccessException is MongoException`() {
        assertTrue(GameStateAccessException("Error accessing.") is MongoException)
    }

    @Test
    fun `Thrown GameStateAccessException is caught as Exception`() {
        assertFailsWith<Exception> {
            throw GameStateAccessException("Error accessing.")
        }
    }

    @Test
    fun `Thrown GameStateAccessException is caught as MongoException`() {
        assertFailsWith<MongoException> {
            throw GameStateAccessException("Error accessing.")
        }
    }

    @Test
    fun `Thrown GameStateAccessException is caught as GameStateAccessException`() {
        assertFailsWith<GameStateAccessException> {
            throw GameStateAccessException("Error accessing.")
        }
    }

    @Test
    fun `GameStateAccessException message is thrown correctly`() {
        val message = "Error accessing."
        try {
            throw GameStateAccessException(message)
        } catch (err: GameStateAccessException) {
            assertEquals(message, err.message)
        }
    }
}
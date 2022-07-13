package domainTests.moveTests

import domain.move.IllegalMoveException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IllegalMoveExceptionTests { // [✔]

    @Test
    fun `Thrown IllegalMoveException is caught as Exception`() {
        assertFailsWith<Exception> {
            throw IllegalMoveException("Pe2f5", "Illegal Move.")
        }
    }

    @Test
    fun `Thrown IllegalMoveException is caught as IllegalMoveException`() {
        assertFailsWith<IllegalMoveException> {
            throw IllegalMoveException("Pe2f5", "Illegal Move.")
        }
    }

    @Test
    fun `IllegalMoveException message is thrown correctly`() {
        val message = "Illegal Move."
        try {
            throw IllegalMoveException("Pe2f5", message)
        } catch (err: IllegalMoveException) {
            assertEquals(message, err.message)
        }
    }

    @Test
    fun `IllegalMoveException move is thrown correctly`() {
        val move = "Pe2f5"
        try {
            throw IllegalMoveException(move, "Illegal Move.")
        } catch (err: IllegalMoveException) {
            assertEquals(move, err.move)
        }
    }
}

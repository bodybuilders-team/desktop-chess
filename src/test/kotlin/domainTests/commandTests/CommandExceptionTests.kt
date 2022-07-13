package domainTests.commandTests

import domain.commands.CommandException
import domain.commands.cmdRequire
import domain.commands.cmdRequireNotNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CommandExceptionTests { // [✔]

    // CommandException [✔]

    @Test
    fun `Thrown CommandException is caught as Exception`() {
        assertFailsWith<Exception> {
            throw CommandException("Can't play without a game.")
        }
    }

    @Test
    fun `Thrown CommandException is caught as CommandException`() {
        assertFailsWith<CommandException> {
            throw CommandException("Can't play without a game.")
        }
    }

    @Test
    fun `CommandException message is thrown correctly`() {
        val message = "Can't play without a game."
        try {
            throw CommandException(message)
        } catch (err: CommandException) {
            assertEquals(message, err.message)
        }
    }

    // cmdRequire [✔]

    @Test
    fun `cmdRequire doesn't throw CommandException if value is true`() {
        val num = listOf(420, 89).random()
        cmdRequire(num > 69) { }
    }

    @Test
    fun `cmdRequire throws CommandException if value is false`() {
        assertFailsWith<CommandException> {
            val num = listOf(420, 89).random()
            cmdRequire(num < 69) { }
        }
    }

    // cmdRequireNotNull [✔]

    @Test
    fun `cmdRequireNotNull doesn't throw CommandException if value is not null`() {
        val value = 42
        cmdRequireNotNull(value) { "Value is null!" }
    }

    @Test
    fun `cmdRequireNotNull throws CommandException if value is null`() {
        assertFailsWith<CommandException> {
            val value: Int? = null
            cmdRequireNotNull(value) { "Value is null!" }
        }
    }

    @Test
    fun `cmdRequireNotNull successfully smart casts value to non-nullable type`() {
        var value: Int? = 42; assertTrue(value in listOf(null, 42))

        value = 42
        cmdRequireNotNull(value) { "Value is null!" }
        value > 2
    }
}

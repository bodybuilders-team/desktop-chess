package domainTests.commandTests

import domain.*
import domain.game.*
import GameStorageStub
import domain.board.*
import domain.commands.*
import kotlin.test.*


class CommandTests { // [✔]

    // ExitCommand [✔]

    @Test
    fun `Exit command returns failure`() {
        assertTrue(ExitCommand().invoke().isFailure)
    }

    // HelpCommand [✔]

    @Test
    fun `Help command returns successful result with the same session`() {
        val session = Session("test", SessionState.YOUR_TURN, Game(Board(), emptyList()))
        val result = HelpCommand(session).invoke()

        assertTrue(result.isSuccess)
        assertEquals(session, result.getOrThrow())
    }

    // MovesCommand [✔]

    @Test
    fun `Moves command throws CommandException if the session state is LOGGING`() {
        val gameName = "test"

        val db = GameStorageStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.LOGGING, Game(Board(), emptyList()))

        assertEquals(
            "No game, no moves: try open or join commands.",
            assertFailsWith<CommandException> {
                MovesCommand(db, session).execute(gameName)
            }.message
        )
    }

    @Test
    fun `Moves command returns successful result with the same session`() {
        val gameName = "test"

        val db = GameStorageStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.YOUR_TURN, Game(Board(), emptyList()))

        val result = MovesCommand(db, session).execute(gameName)
        val newSession = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(session, newSession)
    }
}

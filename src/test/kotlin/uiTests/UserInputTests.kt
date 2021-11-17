package uiTests

import Session
import domain.*
import domain.pieces.Army
import ui.console.getPrompt
import kotlin.test.*


// TODO("Ask: It's possible to simulate inputs for testability?")
class UserInputTests {
    @Test
    fun `getPrompt with null session name returns empty string`() {
        val session = Session(null, SessionState.LOGGING, null, null, emptyList())
        assertEquals("", getPrompt(session))
    }

    @Test
    fun `getPrompt with White turn being White`() {
        val session = Session("name", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList())
        assertEquals("name:White", getPrompt(session))
    }

    @Test
    fun `getPrompt with White turn being Black`() {
        val session = Session("name", SessionState.WAITING_FOR_OPPONENT, Army.BLACK, Board(), emptyList())
        assertEquals("name:White", getPrompt(session))
    }

    @Test
    fun `getPrompt with Black turn being Black`() {
        val session = Session("name", SessionState.YOUR_TURN, Army.BLACK, Board(), emptyList())
        assertEquals("name:Black", getPrompt(session))
    }

    @Test
    fun `getPrompt with Black turn being White`() {
        val session = Session("name", SessionState.WAITING_FOR_OPPONENT, Army.WHITE, Board(), emptyList())
        assertEquals("name:Black", getPrompt(session))
    }
}

package ui.consoleTests

import domain.*
import domain.game.*
import domain.board.*
import domain.pieces.Army
import ui.console.getPrompt
import kotlin.test.*


class UserInputTests { // [✔]
    
    // getPrompt [✔]
    
    @Test
    fun `getPrompt with logging session name returns empty string`() {
        val session = Session("", SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()))
        assertEquals("", getPrompt(session))
    }

    @Test
    fun `getPrompt with White turn being White`() {
        val session = Session("name", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()))
        assertEquals("name:White", getPrompt(session))
    }

    @Test
    fun `getPrompt with White turn being Black`() {
        val session =
            Session("name", SessionState.WAITING_FOR_OPPONENT, Army.BLACK, Game(Board(), emptyList()))
        assertEquals("name:White", getPrompt(session))
    }

    @Test
    fun `getPrompt with Black turn being Black`() {
        val session = Session("name", SessionState.YOUR_TURN, Army.BLACK, Game(Board(), emptyList()))
        assertEquals("name:Black", getPrompt(session))
    }

    @Test
    fun `getPrompt with Black turn being White`() {
        val session =
            Session("name", SessionState.WAITING_FOR_OPPONENT, Army.WHITE, Game(Board(), emptyList()))
        assertEquals("name:Black", getPrompt(session))
    }
}

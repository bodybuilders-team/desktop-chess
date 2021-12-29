package ui.consoleTests

import domain.*
import domain.game.*
import domain.board.*
import ui.console.getPrompt
import kotlin.test.*


class UserInputTests { // [✔]

    // getPrompt [✔]

    @Test
    fun `getPrompt with logging session name returns empty string`() {
        val session = Session("", SessionState.LOGGING, Game(Board(), emptyList()))
        assertEquals("", getPrompt(session))
    }

    @Test
    fun `getPrompt with White turn`() {
        val session = Session("name", SessionState.YOUR_TURN, Game(Board(), emptyList()))
        assertEquals("name:White", getPrompt(session))
    }

    @Test
    fun `getPrompt with Black turn`() {
        val session = Session("name", SessionState.WAITING_FOR_OPPONENT, gameFromMoves("Pe2e4"))
        assertEquals("name:Black", getPrompt(session))
    }
}

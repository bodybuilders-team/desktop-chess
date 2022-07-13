package domainTests

import domain.Session
import domain.SessionState
import domain.board.Board
import domain.game.Game
import domain.game.GameState
import domain.game.gameFromMoves
import domain.game.state
import domain.getOpeningBoardSession
import domain.isLogging
import domain.pieces.Army
import listOfMoves
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionTests { // [✔]

    // isLogging [✔]

    @Test
    fun `isLogging returns true if the current state is LOGGING`() {
        val sut = Session("test", SessionState.LOGGING, Game(Board(), emptyList()))
        assertTrue(sut.isLogging())
    }

    @Test
    fun `isLogging returns false if the current state isn't LOGGING`() {
        val sut = Session("test", SessionState.YOUR_TURN, Game(Board(), emptyList()))
        assertFalse(sut.isLogging())
    }

    // getOpeningBoardSession [✔]

    @Test
    fun `getOpeningBoardSession returns ENDED if the game ended (checkmate in the board)`() {
        val game = gameFromMoves("f3", "e5", "g4", "Qh4")
        val session = getOpeningBoardSession("test", game.moves, Army.WHITE)

        assertEquals(SessionState.ENDED, session.state)
        assertEquals(GameState.CHECKMATE, session.game.state)
    }

    @Test
    fun `getOpeningBoardSession returns ENDED if the game ended (stalemate in the board)`() {
        val game = gameFromMoves(
            "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
            "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
        )
        val session = getOpeningBoardSession("test", game.moves, Army.WHITE)

        assertEquals(SessionState.ENDED, session.state)
        assertEquals(GameState.STALEMATE, session.game.state)
    }

    @Test
    fun `getOpeningBoardSession returns YOUR_TURN if it's the army's turn`() {
        val session = getOpeningBoardSession("test", emptyList(), Army.WHITE)

        assertEquals(SessionState.YOUR_TURN, session.state)

        val session2 = getOpeningBoardSession("test", listOfMoves("Pe2e4"), Army.BLACK)

        assertEquals(SessionState.YOUR_TURN, session2.state)
    }

    @Test
    fun `getOpeningBoardSession returns WAITING_FOR_OPPONENT if it's not the army's turn`() {
        val session = getOpeningBoardSession("test", emptyList(), Army.BLACK)

        assertEquals(SessionState.WAITING_FOR_OPPONENT, session.state)

        val session2 = getOpeningBoardSession("test", listOfMoves("Pe2e4"), Army.WHITE)

        assertEquals(SessionState.WAITING_FOR_OPPONENT, session2.state)
    }
}

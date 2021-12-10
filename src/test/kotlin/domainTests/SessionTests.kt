package domainTests

import domain.*
import domain.move.*
import domain.board.*
import domain.pieces.*
import kotlin.test.*


class SessionTests {

    // isLogging

    @Test
    fun `isLogging returns true if the current state is LOGGING`() {
        val sut = Session("test", SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)
        assertTrue(sut.isLogging())
    }

    @Test
    fun `isLogging returns false if the current state isn't LOGGING`() {
        val sut = Session("test", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)
        assertFalse(sut.isLogging())
    }

    // getCurrentState

    @Test
    fun `getCurrentState returns ENDED if the game ended (checkmate in the board)`() {
        val board = Board(
            getMatrix2DFromString(
                "        " +
                "    Kq  " +
                "      b " +
                "   r    " +
                "        " +
                "  k     " +
                "        " +
                "        "
            )
        )
        assertEquals(SessionState.ENDED, getCurrentState(board, emptyList(), Army.WHITE))
    }

    @Test
    fun `getCurrentState returns ENDED if the game ended (stalemate in the board)`() {
        val board = Board(
            getMatrix2DFromString(
                "       K" +
                "     qr " +
                "      b " +
                "   r    " +
                "        " +
                " k      " +
                "        " +
                "        "
            )
        )
        assertEquals(SessionState.ENDED, getCurrentState(board, emptyList(), Army.WHITE))
    }

    @Test
    fun `getCurrentState returns YOUR_TURN if it's the army's turn`() {
        val board = Board()
        assertEquals(SessionState.YOUR_TURN, getCurrentState(board, emptyList(), Army.WHITE))
        assertEquals(SessionState.YOUR_TURN, getCurrentState(board, listOf(Move("Pe2e4")), Army.BLACK))
    }

    @Test
    fun `getCurrentState returns WAITING_FOR_OPPONENT if it's not the army's turn`() {
        val board = Board()
        assertEquals(SessionState.WAITING_FOR_OPPONENT, getCurrentState(board, listOf(Move("Pe2e4")), Army.WHITE))
        assertEquals(SessionState.WAITING_FOR_OPPONENT, getCurrentState(board, emptyList(), Army.BLACK))
    }
}

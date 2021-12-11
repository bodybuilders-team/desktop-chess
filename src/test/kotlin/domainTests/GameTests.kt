package domainTests

import domain.board.*
import domain.*
import domain.move.IllegalMoveException
import domain.move.Move
import kotlin.test.*

class GameTests {
    @Test
    fun `gameFromMoves return game with no moves made if no moves are passed`() {
        assertEquals(Board().toString(), gameFromMoves().board.toString())
    }

    @Test
    fun `gameFromMoves returns new game with the board with moves made`() {
        val board = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "p pppppp" +
                "        " +
                " p      " +
                "    P   " +
                "  N     " +
                "PPPP PPP" +
                "R BQKBNR"
            )
        )

        assertEquals(board.toString(), gameFromMoves("Pe2e4", "Pb7b5", "Nb1c3").board.toString())
    }

    @Test
    fun `gameFromMoves returns new game with moves validated`() {
        assertEquals(listOf("Pe2e4", "Pb7b5", "Nb1c3").map { Move(it) }, gameFromMoves("Pe4", "Pb5", "Nc3").moves)
    }

    @Test
    fun `gameFromMoves throws IllegalMoveException if at least one of the moves is invalid`() {
        assertFailsWith<IllegalMoveException>{
            gameFromMoves("Pe4", "Pb5", "Ne3")
        }
    }

    @Test
    fun `gameFromMoves throws IllegalMoveException if multiple moves are invalid`() {
        assertFailsWith<IllegalMoveException>{
            gameFromMoves("Pe4", "Pa2a5", "Ne3", "Ke9")
        }
    }
}
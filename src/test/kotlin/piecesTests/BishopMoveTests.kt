package piecesTests

import Board
import Move
import getBoardFromString
import kotlin.test.*

// Bishop in e2
private const val testBoard =
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "     p  " +
            "    B   " +
            "        "

class BishopMoveTests {
    private val board = Board(getBoardFromString(testBoard))

    @Test
    fun `Bishop vertical not valid`() {
        assertFalse(board.checkMove(Move("Be2e3")))
    }

    @Test
    fun `Bishop horizontal not valid`() {
        assertFalse(board.checkMove(Move("Be2d2")))
    }

    @Test
    fun `Bishop move(up,right) with capture is valid`() {
        assertTrue(board.checkMove(Move("Be2f3")))
    }

    @Test
    fun `Bishop move(up,left) is valid`() {
        assertTrue(board.checkMove(Move("Be2d3")))
    }

    @Test
    fun `Bishop move(down,right) is valid`() {
        assertTrue(board.checkMove(Move("Be2f1")))
    }

    @Test
    fun `Bishop move(down,left) is valid`() {
        assertTrue(board.checkMove(Move("Be2d1")))
    }
}
package piecesTests

import Board
import Move
import getMatrix2DFromString
import isValidMove
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
    private val board = Board(getMatrix2DFromString(testBoard))

    @Test
    fun `Bishop vertical not valid`() {
        assertFalse(board.isValidMove(Move("Be2e3")))
    }

    @Test
    fun `Bishop horizontal not valid`() {
        assertFalse(board.isValidMove(Move("Be2d2")))
    }

    @Test
    fun `Bishop move(up,right) with capture is valid`() {
        assertTrue(board.isValidMove(Move("Be2f3")))
    }

    @Test
    fun `Bishop move(up,left) is valid`() {
        assertTrue(board.isValidMove(Move("Be2d3")))
    }

    @Test
    fun `Bishop move(down,right) is valid`() {
        assertTrue(board.isValidMove(Move("Be2f1")))
    }

    @Test
    fun `Bishop move(down,left) is valid`() {
        assertTrue(board.isValidMove(Move("Be2d1")))
    }

    @Test
    fun `Bishop move to same place does not work`() {
        assertFailsWith<IllegalArgumentException> {board.isValidMove(Move("Be2e2"))}
    }
}
package piecesTests

import Board
import Move
import getBoardFromString
import kotlin.test.*

private const val testBoard =
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "   p    " +
            "    K   " +
            "        "

class KingMoveTests {
    private val board = Board(getBoardFromString(testBoard))

    @Test
    fun `King vertical(up) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2e3")))
    }

    @Test
    fun `King vertical(down) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2e1")))
    }

    @Test
    fun `King horizontal(right) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2f2")))
    }

    @Test
    fun `King horizontal(left) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2d2")))
    }

    @Test
    fun `King diagonal(up,right) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2f3")))
    }

    @Test
    fun `King diagonal(up,left) move with capture is valid`() {
        assertTrue(board.checkMove(Move("Ke2d3")))
    }

    @Test
    fun `King diagonal(down,right) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2f1")))
    }

    @Test
    fun `King diagonal(down,left) move is valid`() {
        assertTrue(board.checkMove(Move("Ke2d1")))
    }

    @Test
    fun `King double move is not valid`() {
        assertFalse(board.checkMove(Move("Ke2e4")))
    }
}
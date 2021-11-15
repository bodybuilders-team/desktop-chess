package piecesTests

import domain.*
import kotlin.test.*


// King in e2
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
    private val board = Board(getMatrix2DFromString(testBoard))

    @Test
    fun `King vertical(up) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2e3")))
    }

    @Test
    fun `King vertical(down) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2e1")))
    }

    @Test
    fun `King horizontal(right) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2f2")))
    }

    @Test
    fun `King horizontal(left) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2d2")))
    }

    @Test
    fun `King diagonal(up,right) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2f3")))
    }

    @Test
    fun `King diagonal(up,left) move with capture is valid`() {
        assertTrue(board.isValidMove(Move("Ke2d3")))
    }

    @Test
    fun `King diagonal(down,right) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2f1")))
    }

    @Test
    fun `King diagonal(down,left) move is valid`() {
        assertTrue(board.isValidMove(Move("Ke2d1")))
    }

    @Test
    fun `King double move is not valid`() {
        assertFalse(board.isValidMove(Move("Ke2e4")))
    }

    @Test
    fun `King move to same place is not valid`() {
        assertFalse(board.isValidMove(Move("Ke2e2")))
    }
}

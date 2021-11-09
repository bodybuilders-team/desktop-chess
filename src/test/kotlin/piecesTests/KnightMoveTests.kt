package piecesTests

import Board
import Move
import getMatrix2DFromString
import kotlin.test.*

// Knight in c3
private const val testBoard =
            "        " +
            "        " +
            "        " +
            "   p    " +
            "        " +
            "  N     " +
            "        " +
            "        "

class KnightMoveTests {
    private val board = Board(getMatrix2DFromString(testBoard))

    @Test
    fun `Knight upper right with capture is valid`() {
        assertTrue(board.checkMove(Move("Nc3d5")))
    }

    @Test
    fun `Knight upper left is valid`() {
        assertTrue(board.checkMove(Move("Nc3b5")))
    }

    @Test
    fun `Knight down right is valid`() {
        assertTrue(board.checkMove(Move("Nc3d1")))
    }

    @Test
    fun `Knight down left is valid`() {
        assertTrue(board.checkMove(Move("Nc3b1")))
    }

    @Test
    fun `Knight right up is valid`() {
        assertTrue(board.checkMove(Move("Nc3e4")))
    }

    @Test
    fun `Knight right down is valid`() {
        assertTrue(board.checkMove(Move("Nc3e2")))
    }

    @Test
    fun `Knight left up is valid`() {
        assertTrue(board.checkMove(Move("Nc3a4")))
    }

    @Test
    fun `Knight left down is valid`() {
        assertTrue(board.checkMove(Move("Nc3a2")))
    }

    @Test
    fun `Knight not L shaped move is not valid`() {
        assertFalse(board.checkMove(Move("Nc3e3")))
    }
}
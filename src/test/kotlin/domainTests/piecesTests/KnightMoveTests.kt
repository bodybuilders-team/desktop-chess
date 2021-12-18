package domainTests.piecesTests

import domain.board.*
import kotlin.test.*
import isValidMove


class KnightMoveTests {
    private val sut = Board(
        "        " +
        "        " +
        "        " +
        "   p    " +
        "        " +
        "  N     " +
        "        " +
        "K       "
    )

    @Test
    fun `Knight upper right with capture is valid`() {
        assertTrue(sut.isValidMove("Nc3d5"))
    }

    @Test
    fun `Knight upper left is valid`() {
        assertTrue(sut.isValidMove("Nc3b5"))
    }

    @Test
    fun `Knight down right is valid`() {
        assertTrue(sut.isValidMove("Nc3d1"))
    }

    @Test
    fun `Knight down left is valid`() {
        assertTrue(sut.isValidMove("Nc3b1"))
    }

    @Test
    fun `Knight right up is valid`() {
        assertTrue(sut.isValidMove("Nc3e4"))
    }

    @Test
    fun `Knight right down is valid`() {
        assertTrue(sut.isValidMove("Nc3e2"))
    }

    @Test
    fun `Knight left up is valid`() {
        assertTrue(sut.isValidMove("Nc3a4"))
    }

    @Test
    fun `Knight left down is valid`() {
        assertTrue(sut.isValidMove("Nc3a2"))
    }

    @Test
    fun `Knight not L shaped move is not valid`() {
        assertFalse(sut.isValidMove("Nc3e3"))
    }

    @Test
    fun `Knight move to same place is not valid`() {
        assertFalse(sut.isValidMove("Nc3c3"))
    }
}

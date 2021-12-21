package domainTests.piecesTests

import domain.board.*
import kotlin.test.*
import isValidMove


class BishopMoveTests { // [✔]
    private val sut = Board(
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "     p  " +
        "    B   " +
        "K       "
    )

    @Test
    fun `Bishop vertical move is not valid`() {
        assertFalse(sut.isValidMove("Be2e3"))
    }

    @Test
    fun `Bishop horizontal move is not valid`() {
        assertFalse(sut.isValidMove("Be2d2"))
    }

    @Test
    fun `Bishop move(up,right) with capture is valid`() {
        assertTrue(sut.isValidMove("Be2f3"))
    }

    @Test
    fun `Bishop move(up,left) is valid`() {
        assertTrue(sut.isValidMove("Be2d3"))
    }

    @Test
    fun `Bishop move(down,right) is valid`() {
        assertTrue(sut.isValidMove("Be2f1"))
    }

    @Test
    fun `Bishop move(down,left) is valid`() {
        assertTrue(sut.isValidMove("Be2d1"))
    }

    @Test
    fun `Bishop move to same place is not valid`() {
        assertFalse(sut.isValidMove("Be2e2"))
    }
}

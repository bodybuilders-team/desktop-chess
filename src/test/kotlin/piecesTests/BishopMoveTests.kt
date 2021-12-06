package piecesTests

import domain.board.Board
import domain.board.getMatrix2DFromString
import domain.board.isValidMove
import kotlin.test.*


class BishopMoveTests {
    private val sut = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "     p  " +
            "    B   " +
            "        "
    )
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

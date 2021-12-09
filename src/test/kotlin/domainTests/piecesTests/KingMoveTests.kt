package domainTests.piecesTests

import domain.board.*
import kotlin.test.*
import isValidMove


class KingMoveTests {
    private val sut = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "   p    " +
            "    K   " +
            "        "
    )
    )

    @Test
    fun `King vertical(up) move is valid`() {
        assertTrue(sut.isValidMove("Ke2e3"))
    }

    @Test
    fun `King vertical(down) move is valid`() {
        assertTrue(sut.isValidMove("Ke2e1"))
    }

    @Test
    fun `King horizontal(right) move is valid`() {
        assertTrue(sut.isValidMove("Ke2f2"))
    }

    @Test
    fun `King horizontal(left) move is valid`() {
        assertTrue(sut.isValidMove("Ke2d2"))
    }

    @Test
    fun `King diagonal(up,right) move is valid`() {
        assertTrue(sut.isValidMove("Ke2f3"))
    }

    @Test
    fun `King diagonal(up,left) move with capture is valid`() {
        assertTrue(sut.isValidMove("Ke2d3"))
    }

    @Test
    fun `King diagonal(down,right) move is valid`() {
        assertTrue(sut.isValidMove("Ke2f1"))
    }

    @Test
    fun `King diagonal(down,left) move is valid`() {
        assertTrue(sut.isValidMove("Ke2d1"))
    }

    @Test
    fun `King double move is not valid`() {
        assertFalse(sut.isValidMove("Ke2e4"))
    }

    @Test
    fun `King move to same place is not valid`() {
        assertFalse(sut.isValidMove("Ke2e2"))
    }

    private val sutCastle = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "R   K  R"
        )
    )

    @Test
    fun `King long castle move is valid`() {
        assertTrue(sutCastle.isValidMove("Ra1c1"))
    }

    @Test
    fun `King short castle move is valid`() {
        assertTrue(sutCastle.isValidMove("Rh1f1"))
    }
}

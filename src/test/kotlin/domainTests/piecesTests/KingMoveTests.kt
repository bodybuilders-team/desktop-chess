package domainTests.piecesTests

import domain.game.*
import domain.board.*
import domain.move.Move
import domain.move.MoveType
import kotlin.test.*
import isValidMove


class KingMoveTests {
    private val sut = Board(
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "   p    " +
        "    K   " +
        "        "
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

    // Castle

    private val sutCastle = Board(
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "R   K  R"
    )

    @Test
    fun `King long castle move is valid`() {
        val moveInString = "Ke1c1"
        assertEquals(MoveType.CASTLE, Move.validated(moveInString, Game(sutCastle, emptyList())).type)
        assertTrue(sutCastle.isValidMove(moveInString))
    }

    @Test
    fun `King short castle move is valid`() {
        val moveInString = "Ke1g1"
        assertEquals(MoveType.CASTLE, Move.validated(moveInString, Game(sutCastle, emptyList())).type)
        assertTrue(sutCastle.isValidMove(moveInString))
    }
}

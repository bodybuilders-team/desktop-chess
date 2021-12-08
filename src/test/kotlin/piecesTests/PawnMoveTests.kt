package piecesTests

import domain.board.*
import kotlin.test.*
import isValidMove


class PawnMoveTests {
    private val sut = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "   n    " +
            "    P   " +
            "        "
    )
    )

    @Test
    fun `Pawn vertical one move is valid`() {
        assertTrue(sut.isValidMove("Pe2e3"))
    }

    @Test
    fun `Pawn vertical double move is valid`() {
        assertTrue(sut.isValidMove("Pe2e4"))
    }

    @Test
    fun `Pawn diagonal move with capture is valid`() {
        assertTrue(sut.isValidMove("Pe2d3"))
    }

    @Test
    fun `Pawn horizontal move isn't valid`() {
        assertFalse(sut.isValidMove("Pe2f2"))
    }

    @Test
    fun `Pawn diagonal move without capture isn't valid`() {
        assertFalse(sut.isValidMove("Pe2f3"))
    }

    @Test
    fun `Pawn vertical move backwards isn't valid`() {
        assertFalse(sut.isValidMove("Pe2e1"))
    }

    @Test
    fun `Pawn diagonal move backwards isn't valid`() {
        assertFalse(sut.isValidMove("Pe2f1"))
    }

    @Test
    fun `Pawn move to same place is not valid`() {
        assertFalse(sut.isValidMove("Pe2e2"))
    }
}

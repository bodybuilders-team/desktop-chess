package domainTests.piecesTests

import domain.board.Board
import domain.game.Game
import domain.move.Move
import domain.move.MoveType
import domain.pieces.Army
import domain.pieces.King
import isValidMove
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KingMoveTests { // [✔]
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

    // isValidCastle [✔]

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
    fun `isValidCastle returns true if long castle move is valid`() {
        assertTrue(King(Army.WHITE).isValidCastle(sutCastle, Move("Ke1c1")))
    }

    @Test
    fun `isValidCastle returns true if short castle move is valid`() {
        assertTrue(King(Army.WHITE).isValidCastle(sutCastle, Move("Ke1g1")))
    }

    @Test
    fun `isValidCastle returns false if long castle move is invalid (not a rook piece)`() {
        val sutCastle2 = Board(
            "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "N   K  R"
        )
        assertFalse(King(Army.WHITE).isValidCastle(sutCastle2, Move("Ke1c1")))
    }

    @Test
    fun `isValidCastle returns false if short castle move is invalid (not a rook piece)`() {
        val sutCastle2 = Board(
            "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "R   K  P"
        )
        assertFalse(King(Army.WHITE).isValidCastle(sutCastle2, Move("Ke1g1")))
    }

    @Test
    fun `isValidCastle returns false if castle move is invalid`() {
        assertFalse(King(Army.WHITE).isValidCastle(sutCastle, Move("Ke1b1")))
    }

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

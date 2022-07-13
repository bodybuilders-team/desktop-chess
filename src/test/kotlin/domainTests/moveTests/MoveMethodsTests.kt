package domainTests.moveTests

import domain.board.Board
import domain.board.Board.Position
import domain.game.Game
import domain.move.IllegalMoveException
import domain.move.Move
import domain.move.MoveType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MoveMethodsTests { // [✔]

    // Move.invoke(string) [✔]
    // Basically only returns extractMoveInfo.move but also checks that the move doesn't have optional from position.
    // Guarantee extractMoveInfo is well tested.

    @Test
    fun `Move invoke with string returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Position('e', 2), capture = false, Position('e', 4), promotion = null, MoveType.NORMAL),
            Move("Pe2e4")
        )
    }

    @Test
    fun `Move invoke with string with optional from position throws IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> { Move("Pe4") }
        assertFailsWith<IllegalArgumentException> { Move("Pee4") }
        assertFailsWith<IllegalArgumentException> { Move("P2e4") }
    }

    @Test
    fun `Move invoke with string with supposedly invalid move returns unvalidated move correctly`() {
        assertEquals(
            Move('K', Position('e', 1), false, Position('g', 8), null, MoveType.NORMAL),
            Move("Ke1g8")
        )
    }

    // Move.validated() [✔]

    @Test
    fun `validated returns validated move correctly if move is valid`() {
        val sut = Board()

        assertEquals(
            Move("Pe2e4"),
            Move.validated("Pe2e4", Game(sut, emptyList()))
        )
    }

    @Test
    fun `validated throws IllegalMoveException if move isn't valid`() {
        val sut = Board()

        assertFailsWith<IllegalMoveException> {
            Move.validated("Pe2e5", Game(sut, emptyList()))
        }
    }

    @Test
    fun `validated throws IllegalMoveException if multiple valid moves are found`() {
        val sut = Board(
            "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "    P P " +
                "        " +
                "PPPP PPP" +
                "RNBQKBNR"
        )

        assertFailsWith<IllegalMoveException> {
            Move.validated("Pf5", Game(sut, emptyList()))
        }
    }

    // Move to same place [✔]

    @Test
    fun `Move to same place isn't vertical, horizontal, straight nor diagonal`() {
        assertFalse(Move("e2e2").isVertical())
        assertFalse(Move("e2e2").isHorizontal())
        assertFalse(Move("e2e2").isStraight())
        assertFalse(Move("e2e2").isDiagonal())
    }

    // isVertical [✔]

    @Test
    fun `isVertical with vertical move works`() {
        assertTrue(Move("e2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`() {
        assertFalse(Move("e2f2").isVertical())
    }

    @Test
    fun `isVertical with diagonal move doesn't work`() {
        assertFalse(Move("e2g4").isVertical())
    }

    // isHorizontal [✔]

    @Test
    fun `isHorizontal with horizontal move works`() {
        assertTrue(Move("e2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`() {
        assertFalse(Move("e2e4").isHorizontal())
    }

    @Test
    fun `isHorizontal with diagonal move doesn't work`() {
        assertFalse(Move("e2g4").isHorizontal())
    }

    // isStraight [✔]

    @Test
    fun `isStraight with horizontal move works`() {
        assertTrue(Move("e2f2").isStraight())
    }

    @Test
    fun `isStraight with vertical move works`() {
        assertTrue(Move("e2e4").isStraight())
    }

    @Test
    fun `isStraight with diagonal move doesn't work`() {
        assertFalse(Move("e2g4").isStraight())
    }

    // isDiagonal [✔]

    @Test
    fun `isDiagonal with diagonal move works`() {
        assertTrue(Move("e2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with straight move doesn't work`() {
        assertFalse(Move("e2f4").isDiagonal())
    }

    // Distances [✔]

    @Test
    fun `rowsDistance works`() {
        assertEquals(2, Move("e2e4").rowsDistance())
    }

    @Test
    fun `rowsDistance with no distance works`() {
        assertEquals(0, Move("e2f2").rowsDistance())
    }

    @Test
    fun `colsDistance works`() {
        assertEquals(1, Move("e2f2").colsDistance())
    }

    @Test
    fun `colsDistance with no distance works`() {
        assertEquals(0, Move("e2e5").colsDistance())
    }

    @Test
    fun `rowsAbsoluteDistance works`() {
        assertEquals(2, Move("e4e2").rowsAbsoluteDistance())
    }

    @Test
    fun `rowsAbsoluteDistance with no distance works`() {
        assertEquals(0, Move("e6f6").rowsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance works`() {
        assertEquals(1, Move("b2a2").colsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance with no distance works`() {
        assertEquals(0, Move("h2h7").colsAbsoluteDistance())
    }

    // toString [✔]

    @Test
    fun `toString works for normal move`() {
        assertEquals("Pe2e4", Move("Pe2e4").toString())
    }

    @Test
    fun `toString works for capture move`() {
        assertEquals("Pe2xe4", Move("Pe2xe4").toString())
    }

    @Test
    fun `toString works for promotion move`() {
        assertEquals("Pe7e8=Q", Move("Pe7e8=Q").toString())
    }

    @Test
    fun `toString works for short castle`() {
        assertEquals("O-O", Move("O-O").toString())
    }

    @Test
    fun `toString works for long castle`() {
        assertEquals("O-O-O", Move("O-O-O").toString())
    }
}

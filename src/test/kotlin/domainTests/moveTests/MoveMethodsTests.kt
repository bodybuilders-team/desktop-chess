package domainTests.moveTests

import domain.game.*
import domain.board.*
import domain.move.*
import kotlin.test.*


class MoveMethodsTests {

    // Move.invoke(string)

    @Test
    fun `Move constructor with string returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Board.Position('e', 2), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pe2e4")
        )
    }

    @Test
    fun `Move constructor with string with optional from position returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Board.Position('a', 1), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pe4")
        )
        assertEquals(
            Move('P', Board.Position('e', 1), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pee4")
        )
        assertEquals(
            Move('P', Board.Position('a', 2), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("P2e4")
        )
    }

    @Test
    fun `Move constructor with string with supposedly invalid move returns unvalidated move correctly`() {
        assertEquals(
            Move('K', Board.Position('e', 1), false, Board.Position('g', 8), null, MoveType.NORMAL),
            Move("Ke1g8")
        )
    }

    // Move.validated()

    @Test
    fun `validated returns validated move correctly if move is valid`() {
        val sut = Board()

        assertEquals(
            Move("Pe2e4"),
            Move.validated("Pe2e4", Game(sut, emptyList()))
        )
    }

    @Test
    fun `validated throws if move isn't valid`() {
        val sut = Board()

        assertFailsWith<IllegalMoveException> {
            Move.validated("Pe2e5", Game(sut, emptyList()))
        }
    }

    @Test
    fun `validated throws if multiple valid moves are found`() {
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

    // Directions

    @Test
    fun `Move to same place isn't vertical, horizontal, straight nor diagonal`() {
        assertFalse(Move("e2e2").isVertical())
        assertFalse(Move("e2e2").isHorizontal())
        assertFalse(Move("e2e2").isStraight())
        assertFalse(Move("e2e2").isDiagonal())
    }

    // Vertical

    @Test
    fun `isVertical with vertical move works`() {
        assertTrue(Move("e2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`() {
        assertFalse(Move("e2f2").isVertical())
    }

    // Horizontal

    @Test
    fun `isHorizontal with horizontal move works`() {
        assertTrue(Move("e2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`() {
        assertFalse(Move("e2e4").isHorizontal())
    }

    // Straight

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

    // Diagonal

    @Test
    fun `isDiagonal with diagonal move works`() {
        assertTrue(Move("e2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with straight move doesn't work`() {
        assertFalse(Move("e2f4").isDiagonal())
    }

    // Distances

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

    // toString

    @Test
    fun `toString works`() {
        assertEquals("Pe2e4", Move("Pe2e4").toString())
    }

    @Test
    fun `toString with optional from position works`() {
        assertEquals("Pe4", Move("Pe4").toString(optionalFromCol = true, optionalFromRow = true))
        assertEquals("Pee4", Move("Pee4").toString(optionalFromCol = false, optionalFromRow = true))
        assertEquals("P2e4", Move("P2e4").toString(optionalFromCol = true, optionalFromRow = false))
    }
}

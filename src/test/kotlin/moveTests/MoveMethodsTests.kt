package moveTests

import domain.*
import kotlin.test.*


class MoveMethodsTests {

    @Test
    fun `isHorizontal with horizontal move works`() {
        assertTrue(Move.getUnvalidatedMove("e2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`() {
        assertFalse(Move.getUnvalidatedMove("e2e4").isHorizontal())
    }

    @Test
    fun `isVertical with vertical move works`() {
        assertTrue(Move.getUnvalidatedMove("e2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`() {
        assertFalse(Move.getUnvalidatedMove("e2f2").isVertical())
    }

    @Test
    fun `isDiagonal with diagonal move works`() {
        assertTrue(Move.getUnvalidatedMove("e2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with non diagonal move doesn't work`() {
        assertFalse(Move.getUnvalidatedMove("e2f4").isDiagonal())
    }


    @Test
    fun `rowsDistance works`() {
        assertEquals(2, Move.getUnvalidatedMove("e2e4").rowsDistance())
    }

    @Test
    fun `rowsDistance with no distance works`() {
        assertEquals(0, Move.getUnvalidatedMove("e2f2").rowsDistance())
    }

    @Test
    fun `colsDistance works`() {
        assertEquals(1, Move.getUnvalidatedMove("e2f2").colsDistance())
    }

    @Test
    fun `colsDistance with no distance works`() {
        assertEquals(0, Move.getUnvalidatedMove("e2e5").colsDistance())
    }

    @Test
    fun `rowsAbsoluteDistance works`() {
        assertEquals(2, Move.getUnvalidatedMove("e4e2").rowsAbsoluteDistance())
    }

    @Test
    fun `rowsAbsoluteDistance with no distance works`() {
        assertEquals(0, Move.getUnvalidatedMove("e6f6").rowsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance works`() {
        assertEquals(1, Move.getUnvalidatedMove("b2a2").colsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance with no distance works`() {
        assertEquals(0, Move.getUnvalidatedMove("h2h7").colsAbsoluteDistance())
    }

    @Test
    fun `getUnvalidatedMove returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Board.Position('e', 2), false, Board.Position('e', 4), null),
            Move.getUnvalidatedMove("Pe2e4")
        )
    }

    @Test
    fun `getUnvalidatedMove with supposedly invalid move returns unvalidated move correctly`() {
        assertEquals(
            Move('K', Board.Position('e', 1), false, Board.Position('g', 8), null),
            Move.getUnvalidatedMove("Ke1g8")
        )
    }

    @Test
    fun `Move toString works`() {
        val moveInString = "Pe2e4"
        assertEquals(moveInString, Move.getUnvalidatedMove(moveInString).toString())
    }
}

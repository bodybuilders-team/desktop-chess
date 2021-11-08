package moveTests

import Move
import kotlin.test.*


class MoveMethodsTests {
    @Test
    fun `isHorizontal with horizontal move works`(){
        assertTrue(Move("Pe2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`(){
        assertFalse(Move("Pe2e4").isHorizontal())
    }

    @Test
    fun `isVertical with vertical move works`(){
        assertTrue(Move("Pe2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`(){
        assertFalse(Move("Pe2f2").isVertical())
    }

    @Test
    fun `isDiagonal with diagonal move works`(){
        assertTrue(Move("Pe2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with non diagonal move doesn't work`(){
        assertFalse(Move("Pe2f4").isDiagonal())
    }


    @Test
    fun `rowsDistance works`() {
        assertEquals(2 , Move("Pe2e4").rowsDistance())
    }

    @Test
    fun `rowsDistance with no distance works`() {
        assertEquals(0 , Move("Pe2f2").rowsDistance())
    }

    @Test
    fun `colsDistance works`() {
        assertEquals(1 , Move("Pe2f2").colsDistance())
    }

    @Test
    fun `colsDistance with no distance works`() {
        assertEquals(0 , Move("Pe2e5").colsDistance())
    }


    @Test
    fun `rowsAbsoluteDistance works`() {
        assertEquals(2 , Move("Pe4e2").rowsAbsoluteDistance())
    }

    @Test
    fun `rowsAbsoluteDistance with no distance works`() {
        assertEquals(0 , Move("Pe6f6").rowsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance works`() {
        assertEquals(1 , Move("Pb2a2").colsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance with no distance works`() {
        assertEquals(0 , Move("Ph2h7").colsAbsoluteDistance())
    }

}

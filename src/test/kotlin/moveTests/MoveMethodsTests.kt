package moveTests

import domain.*
import kotlin.test.*


class MoveMethodsTests {
    private val sut = Board()

    @Test
    fun `isHorizontal with horizontal move works`(){
        assertTrue(Move("Pe2f2", sut).isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`(){
        assertFalse(Move("Pe2e4", sut).isHorizontal())
    }

    @Test
    fun `isVertical with vertical move works`(){
        assertTrue(Move("Pe2e4", sut).isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`(){
        assertFalse(Move("Pe2f2", sut).isVertical())
    }

    @Test
    fun `isDiagonal with diagonal move works`(){
        assertTrue(Move("Pe2g4", sut).isDiagonal())
    }

    @Test
    fun `isDiagonal with non diagonal move doesn't work`(){
        assertFalse(Move("Pe2f4", sut).isDiagonal())
    }


    @Test
    fun `rowsDistance works`() {
        assertEquals(2 , Move("Pe2e4", sut).rowsDistance())
    }

    @Test
    fun `rowsDistance with no distance works`() {
        assertEquals(0 , Move("Pe2f2", sut).rowsDistance())
    }

    @Test
    fun `colsDistance works`() {
        assertEquals(1 , Move("Pe2f2", sut).colsDistance())
    }

    @Test
    fun `colsDistance with no distance works`() {
        assertEquals(0 , Move("Pe2e5", sut).colsDistance())
    }


    @Test
    fun `rowsAbsoluteDistance works`() {
        assertEquals(2 , Move("Pe4e2", sut).rowsAbsoluteDistance())
    }

    @Test
    fun `rowsAbsoluteDistance with no distance works`() {
        assertEquals(0 , Move("Pe6f6", sut).rowsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance works`() {
        assertEquals(1 , Move("Pb2a2", sut).colsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance with no distance works`() {
        assertEquals(0 , Move("Ph2h7", sut).colsAbsoluteDistance())
    }
}

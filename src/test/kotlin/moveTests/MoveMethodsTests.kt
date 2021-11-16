package moveTests

import domain.*
import kotlin.test.*


class MoveMethodsTests {
    private val sut = Board()
    
    private fun getMove(string: String) : Move{
        return Move('P', Board.Position(string[0], string[1].digitToInt()), false,
                         Board.Position(string[2], string[3].digitToInt()), null)
    }

    @Test
    fun `isHorizontal with horizontal move works`(){
        assertTrue(getMove("e2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`(){
        assertFalse(getMove("e2e4").isHorizontal())
    }

    @Test
    fun `isVertical with vertical move works`(){
        assertTrue(getMove("e2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`(){
        assertFalse(getMove("e2f2").isVertical())
    }

    @Test
    fun `isDiagonal with diagonal move works`(){
        assertTrue(getMove("e2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with non diagonal move doesn't work`(){
        assertFalse(getMove("e2f4").isDiagonal())
    }


    @Test
    fun `rowsDistance works`() {
        assertEquals(2 , getMove("e2e4").rowsDistance())
    }

    @Test
    fun `rowsDistance with no distance works`() {
        assertEquals(0 , getMove("e2f2").rowsDistance())
    }

    @Test
    fun `colsDistance works`() {
        assertEquals(1 , getMove("e2f2").colsDistance())
    }

    @Test
    fun `colsDistance with no distance works`() {
        assertEquals(0 , getMove("e2e5").colsDistance())
    }

    @Test
    fun `rowsAbsoluteDistance works`() {
        assertEquals(2 , getMove("e4e2").rowsAbsoluteDistance())
    }
    
    @Test
    fun `rowsAbsoluteDistance with no distance works`() {
        assertEquals(0 , getMove("e6f6").rowsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance works`() {
        assertEquals(1 , getMove("b2a2").colsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance with no distance works`() {
        assertEquals(0 , getMove("h2h7").colsAbsoluteDistance())
    }
}

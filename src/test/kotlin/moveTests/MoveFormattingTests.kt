package moveTests

import domain.*
import kotlin.test.*


class MoveFormattingTests {
    private val sut = Board()

    //TODO("Add tests to test all optional fields in the move")

    @Test
    fun `Valid simple move is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe2e4"))
    }

    @Test
    fun `Valid capture is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe2xd3"))
    }

    @Test
    fun `Valid promotion is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe7e8=Q"))
    }

    @Test
    fun `Empty is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted(""))
    }

    @Test
    fun `Invalid piece type is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Ae2e4"))
    }

    @Test
    fun `Invalid 'from position' is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("P92e4"))
    }

    @Test
    fun `Invalid 'capture char' is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe2fe4"))
    }

    @Test
    fun `Invalid 'to position' is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe2e9"))
    }

    @Test
    fun `Invalid promotion symbol is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe2e9+Q"))
    }

    @Test
    fun `Invalid promotion piece is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe2e9=L"))
    }

    @Test
    fun `Too lengthy is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe2e9aaaaaa"))
    }
}

package domainTests.moveTests

import domain.move.Move
import kotlin.test.*


class MoveFormattingTests {

    @Test
    fun `Empty is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted(""))
    }

    @Test
    fun `Too lengthy is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe2e9aaaaaa"))
    }

    @Test
    fun `Valid simple move is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe2e4"))
    }

    @Test
    fun `Valid capture symbol is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe2xd3"))
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
    fun `Valid promotion symbol and piece is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe7e8=Q"))
    }

    @Test
    fun `Invalid promotion symbol is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe7e8+Q"))
    }

    @Test
    fun `Invalid promotion piece is incorrectly formatted`() {
        assertFalse(Move.isCorrectlyFormatted("Pe7e8=L"))
    }

    @Test
    fun `Move with optional piece symbol is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("e2e4"))
    }

    @Test
    fun `Move with optional fromCol is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("P2e4"))
    }

    @Test
    fun `Move with optional fromRow is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pee4"))
    }

    @Test
    fun `Move with optional fromPos is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("Pe4"))
    }

    @Test
    fun `Move with optional piece symbol and optional fromCol is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("2e4"))
    }

    @Test
    fun `Move with optional piece symbol and optional fromRow is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("ee4"))
    }

    @Test
    fun `Move with optional piece symbol and optional fromPos is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("e4"))
    }

    @Test
    fun `Short castle move is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("O-O"))
    }

    @Test
    fun `Long castle move is correctly formatted`() {
        assertTrue(Move.isCorrectlyFormatted("O-O-O"))
    }
}

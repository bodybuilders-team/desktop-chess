package piecesTests

import domain.pieces.*
import kotlin.test.*


class PieceArmyTests {
    @Test
    fun `Army other() function with WHITE returns BLACK`() {
        assertEquals(Army.BLACK, Army.WHITE.other())
    }

    @Test
    fun `Army other() function with BLACK returns WHITE`() {
        assertEquals(Army.WHITE, Army.BLACK.other())
    }

    @Test
    fun `isWhite returns true with White army piece`() {
        assertTrue(Pawn(Army.WHITE).isWhite())
    }

    @Test
    fun `isWhite returns false with Black army piece`() {
        assertFalse(Pawn(Army.BLACK).isWhite())
    }
}

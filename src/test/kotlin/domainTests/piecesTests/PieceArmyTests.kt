package domainTests.piecesTests

import domain.pieces.Army
import domain.pieces.Pawn
import domain.pieces.isWhite
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PieceArmyTests { // [✔]
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

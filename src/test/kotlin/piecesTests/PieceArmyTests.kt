package piecesTests

import domain.pieces.Army
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
}

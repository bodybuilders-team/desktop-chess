package boardTests

import domain.*
import domain.pieces.Army
import kotlin.test.*


class BoardCheckTests {
    @Test
    fun `isPositionInCheck with Pawn returns true`(){
        val sut = Board(getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "    P   " +
                "        " +
                "        " +
                "        " +
                "PPPP PPP" +
                "RNBQKBNR"))

        val position = Board.Position('e', 6)

        assertTrue(sut.positionAttackers(position, Army.BLACK) > 0)
        assertFalse(sut.positionAttackers(position, Army.WHITE) > 0)
    }

    @Test
    fun `isPositionInCheck with position that cannot be checked`(){
        val sut = Board(getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR"))

        val position = Board.Position('e', 4)

        assertFalse(sut.positionAttackers(position, Army.BLACK) > 0)
        assertFalse(sut.positionAttackers(position, Army.WHITE) > 0)
    }
}

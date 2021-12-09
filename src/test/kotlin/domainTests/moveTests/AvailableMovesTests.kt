package domainTests.moveTests

import domain.board.*
import domain.move.*
import domain.pieces.Army
import kotlin.test.*


class AvailableMovesTests {
    @Test
    fun `getAvailableMoves returns emptyList when there are no available moves for queen`(){
        val sut = Board()
        val position = Board.Position('d', 1)
        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(emptyList(), piece.getAvailableMoves(sut, position, emptyList()))
    }

    @Test
    fun `getAvailableMoves returns available moves for queen`(){
        val sut = Board(
            getMatrix2DFromString(
                        "rnbqkbnr" +
                        "pppppppp" +
                        "        " +
                        "        " +
                        "        " +
                        " P P    " +
                        "PP  PPPP" +
                        "RN QKBNR")
        )
        val position = Board.Position('d', 1)
        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(
            setOf("Qd1c1", "Qd1c2", "Qd1d2"),
            piece.getAvailableMoves(sut, position, emptyList()).map { it.toString() }.toSet()
        )
    }

    @Test
    fun `getAvailableMoves returns available moves, including castle move, for king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "R   KBNR")
        )
        val position = Board.Position('e', 1)
        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(
            setOf("Ke1c1", "Ke1d1"),
            piece.getAvailableMoves(sut, position, emptyList()).map { it.toString() }.toSet()
        )
    }

    @Test
    fun `getAvailableMoves returns available moves, including en passant move, for pawn`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppp pppp" +
                "        " +
                "   pP   " +
                "        " +
                "        " +
                "PPPP PPP" +
                "R   KBNR")
        )
        val position = Board.Position('e', 5)
        val piece = sut.getPiece(position)
        val previousMoves = listOf(Move("Pd7d5"))

        assertNotNull(piece)
        assertEquals(
            setOf("Pe5e6", "Pe5d6"),
            piece.getAvailableMoves(sut, position, previousMoves).map { it.toString() }.toSet()
        )
    }

    @Test
    fun `hasAvailableMoves returns true with default board`(){
        val sut = Board()
        assertTrue(sut.hasAvailableMoves(Army.WHITE, emptyList()))
        assertTrue(sut.hasAvailableMoves(Army.BLACK, emptyList()))
    }

    @Test
    fun `hasAvailableMoves returns false with empty board`(){
        val sut = Board(getMatrix2DFromString("        ".repeat(BOARD_SIDE_LENGTH)))
        assertFalse(sut.hasAvailableMoves(Army.WHITE, emptyList()))
        assertFalse(sut.hasAvailableMoves(Army.BLACK, emptyList()))
    }
}

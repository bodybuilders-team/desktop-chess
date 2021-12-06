package boardTests

import domain.board.*
import domain.pieces.Army
import kotlin.test.*


class BoardCheckTests {
    @Test
    fun `positionAttackers' size is 2 if two enemy pieces can attack`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "    P   " +
                "        " +
                "        " +
                "        " +
                "PPPP PPP" +
                "RNBQKBNR")
        )

        val position = Board.Position('e', 6)

        assertTrue(sut.positionAttackers(position, Army.BLACK).size == 2)
        assertFalse(sut.positionAttackers(position, Army.WHITE).isNotEmpty())
    }

    @Test
    fun `positionAttackers is empty if no piece attacks the position`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR")
        )

        val position = Board.Position('e', 4)

        assertTrue(sut.positionAttackers(position, Army.BLACK).isEmpty())
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    @Test
    fun `getPositionOfKing returns position of king of specified army`(){
        val sut = Board(
            getMatrix2DFromString(
                    "        " +
                    "        " +
                    "        " +
                    "  q     " +
                    "        " +
                    "   K    " +
                    "    N   " +
                    "        ")
        )

        assertEquals(Board.Position('d', 3), sut.getKingPosition(army = Army.WHITE))
    }

    @Test
    fun `getPositionOfKing throws if king of specified army isn't found`(){
        val sut = Board(
            getMatrix2DFromString(
                    "        " +
                    "        " +
                    "        " +
                    "  q     " +
                    "        " +
                    "        " +
                    "    N   " +
                    "        ")
        )

        assertFailsWith<IllegalArgumentException> {
            sut.getKingPosition(army = Army.WHITE)
        }
    }

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is vertical`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "    q   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR")
        )
        
        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is vertical`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "    q   " +
                    "    K   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQ BNR")
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is horizontal`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "        " +
                    "    K  r" +
                    "PPPP PPP" +
                    "RNBQ BNR")
        )

        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is horizontal`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "        " +
                    "    K  r" +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNB QBNR")
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is diagonal`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "      b " +
                    "        " +
                    "    K   " +
                    "PPPP PPP" +
                    "RNBQ BNR")
        )

        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is diagonal`(){
        val sut = Board(
            getMatrix2DFromString(
                    "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "      b " +
                    "        " +
                    "    K   " +
                    "PPPP  PP" +
                    "RNBQ BNR")
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingInCheck returns true if king is in check`(){
        val sut = Board(
            getMatrix2DFromString(
                    "        " +
                    "        " +
                    "        " +
                    "   q    " +
                    "        " +
                    "   K    " +
                    "        " +
                    "        ")
        )
        
        val army = Army.WHITE
        assertTrue(sut.isKingInCheck(army))
    }

    @Test
    fun `isKingInCheck returns false if king isn't in check`(){
        val sut = Board(
            getMatrix2DFromString(
            "        " +
                    "        " +
                    "        " +
                    "   q    " +
                    "        " +
                    "    K   " +
                    "        " +
                    "        ")
        )

        val army = Army.WHITE
        assertFalse(sut.isKingInCheck(army))
    }

    @Test
    fun `isKingInCheckMate returns true if king is in checkmate`(){
        val sut = Board(
            getMatrix2DFromString(
                    "        " +
                    "    Kq  " +
                    "      b " +
                    "   r    " +
                    "        " +
                    "        " +
                    "        " +
                    "        ")
        )

        assertTrue(sut.isKingInCheckMate(Army.WHITE))
    }

    @Test
    fun `isKingInCheckMate returns false if king isn't in checkmate`(){
        val sut = Board(
            getMatrix2DFromString(
                    "        " +
                    "    K   " +
                    "      b " +
                    "   r    " +
                    "        " +
                    "        " +
                    "        " +
                    "        ")
        )

        assertFalse(sut.isKingInCheckMate(Army.WHITE))
    }
}

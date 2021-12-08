package boardTests

import domain.board.*
import domain.pieces.Army
import kotlin.test.*


class BoardCheckTests {

    //isKingInCheck

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

    //isKingInCheckMate

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

    //isKingInStaleMate

    @Test
    fun `isKingInStaleMate returns true if king is in stalemate`(){
        val sut = Board(
            getMatrix2DFromString(
                "       K" +
                "     qr " +
                "      b " +
                "   r    " +
                "        " +
                "        " +
                "        " +
                "        ")
        )
        //TODO("Finish isKingInStaleMate, by creating hasAvailableMoves()")
        assertTrue(sut.isKingInStaleMate(Army.WHITE))
    }

    @Test
    fun `isKingInStaleMate returns false if king isn't in stalemate`(){
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

        assertFalse(sut.isKingInStaleMate(Army.WHITE))
    }

    //kingAttackers

    @Test
    fun `kingAttackers size is 2 if two enemy pieces attacks the king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnb kb r" +
                        "pppppppp" +
                        "     n  " +
                        "   q    " +
                        "    K   " +
                        "        " +
                        "PPPP PPP" +
                        "RNBQ BNR")
        )

        assertTrue(sut.kingAttackers(sut.getKingPosition(Army.WHITE), Army.WHITE).size == 2)
    }

    @Test
    fun `kingAttackers is empty if no piece attacks the king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                        "pppppppp" +
                        "        " +
                        "        " +
                        "    K   " +
                        "        " +
                        "PPPP PPP" +
                        "RNBQ BNR")
        )

        assertTrue(sut.kingAttackers(sut.getKingPosition(Army.WHITE), Army.WHITE).isEmpty())
    }

    @Test
    fun `kingAttackers throws if more than 2 pieces attack the king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnb kb r" +
                        "ppppp pp" +
                        "     n  " +
                        "   q p  " +
                        "    K   " +
                        "        " +
                        "PPPP PPP" +
                        "RNBQ BNR")
        )

        assertFailsWith<IllegalArgumentException> {
            sut.kingAttackers(sut.getKingPosition(Army.WHITE), Army.WHITE)
        }
    }

    //isKingProtectable

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

    //canKingMove

    @Test
    fun `canKingMove returns true if king can move`(){
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                        "        " +
                        "        " +
                        "  q     " +
                        "   K    " +
                        "        " +
                        "    N   " +
                        "        ")
        )

        assertTrue(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove returns false if king can't move`(){
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                        "        " +
                        "    r   " +
                        "  q     " +
                        "   K    " +
                        "       r" +
                        "   N    " +
                        "        ")
        )

        assertFalse(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }
    
    //positionAttackers
    
    @Test
    fun `positionAttackers size is 2 if two enemy pieces attack the occupied position`(){
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
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    @Test
    fun `positionAttackers is empty if no piece attacks the occupied position`(){
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
    fun `positionAttackers size is 1 if one enemy piece attacks the empty position`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    p   " +
                "        " +
                "        " +
                "PPPP PPP" +
                "RNBQKBNR")
        )

        val position = Board.Position('e', 4)

        assertTrue(sut.positionAttackers(position, Army.BLACK).size == 1)
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    @Test
    fun `positionAttackers is empty if no piece attacks the empty position`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
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

    //getKingPosition
    
    @Test
    fun `getKingPosition returns position of king of specified army`(){
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
    fun `getKingPosition throws if king of specified army isn't found`(){
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
}

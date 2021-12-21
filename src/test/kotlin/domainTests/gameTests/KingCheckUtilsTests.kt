package domainTests.gameTests

import domain.board.Board
import domain.game.*
import domain.pieces.Army
import kotlin.test.*


class KingCheckUtilsTests { // [✔]
    // kingAttackers [✔]

    @Test
    fun `kingAttackers size is 2 if two enemy pieces attacks the king`() {
        val sut = Board(
            "rnb kb r" +
                    "pppppppp" +
                    "     n  " +
                    "   q    " +
                    "    K   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQ BNR"
        )

        assertTrue(sut.kingAttackers(sut.getKingPosition(Army.WHITE), Army.WHITE).size == 2)
    }

    @Test
    fun `kingAttackers is empty if no piece attacks the king`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "        " +
                    "        " +
                    "    K   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQ BNR"
        )

        assertTrue(sut.kingAttackers(sut.getKingPosition(Army.WHITE), Army.WHITE).isEmpty())
    }

    @Test
    fun `kingAttackers throws if more than 2 pieces attack the king`() {
        val sut = Board(
            "rnb kb r" +
                    "ppppp pp" +
                    "     n  " +
                    "   q p  " +
                    "    K   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQ BNR"
        )

        assertFailsWith<IllegalArgumentException> {
            sut.kingAttackers(sut.getKingPosition(Army.WHITE), Army.WHITE)
        }
    }

    // isKingProtectable [✔]

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is vertical`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "    q   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR"
        )

        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is vertical`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "    q   " +
                    "    K   " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQ BNR"
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is horizontal`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "        " +
                    "    K  r" +
                    "PPPP PPP" +
                    "RNBQ BNR"
        )

        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is horizontal`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "        " +
                    "    K  r" +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNB QBNR"
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is diagonal`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "      b " +
                    "        " +
                    "    K   " +
                    "PPPP PPP" +
                    "RNBQ BNR"
        )

        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is diagonal`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "      b " +
                    "        " +
                    "    K   " +
                    "PPPP  PP" +
                    "RNBQ BNR"
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns true if king is protectable and the attacking move is from a horse`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "    P   " +
            "        " +
            "      n " +
            "    K  P" +
            "PPPP PPP" +
            "RNBQ BNR"
        )

        assertTrue(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    @Test
    fun `isKingProtectable returns false if king isn't protectable and the attacking move is from a horse`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "    P   " +
            "        " +
            "      n " +
            "    K   " +
            "PPPPPPPP" +
            "RNBQ BNR"
        )

        assertFalse(sut.isKingProtectable(sut.getKingPosition(army = Army.WHITE), army = Army.WHITE))
    }

    // canKingMove [✔]

    @Test
    fun `canKingMove returns true if all adjacent pieces are empty and not attacked`() {
        val sut = Board(
            "        " +
                    "        " +
                    "        " +
                    "        " +
                    "   K    " +
                    "        " +
                    "        " +
                    "        "
        )

        assertTrue(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove returns false if all adjacent pieces are of the same army`() {
        val sut = Board(
            "        " +
                    "        " +
                    "        " +
                    "  PPP   " +
                    "  PKP   " +
                    "  PPP   " +
                    "        " +
                    "        "
        )

        assertFalse(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove returns true if at least one adjacent piece is of the other army and not attacked`() {
        val sut = Board(
            "        " +
                    "        " +
                    "        " +
                    "  PPP   " +
                    "  rKP   " +
                    "  PPP   " +
                    "        " +
                    "        "
        )

        assertTrue(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove returns false if the only adjacent position unoccupied by pieces of the same army is attacked`() {
        val sut = Board(
            "        " +
                    "        " +
                    "        " +
                    "  PPP   " +
                    "   KP   " +
                    "  PPP   " +
                    "b       " +
                    "        "
        )

        assertFalse(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove returns false if all adjacent positions are attacked`() {
        val sut = Board(
            "        " +
                    "        " +
                    "  q r   " +
                    "        " +
                    "   K    " +
                    "       r" +
                    "   N    " +
                    "        "
        )

        assertFalse(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove returns false if an adjacent piece is of the other army but the position is attacked`() {
        val sut = Board(
            "        " +
                    "        " +
                    "   br   " +
                    "  q     " +
                    "   K    " +
                    "       r" +
                    "   N    " +
                    "        "
        )

        assertFalse(sut.canKingMove(sut.getKingPosition(Army.WHITE), Army.WHITE))
    }

    @Test
    fun `canKingMove with king in corner doesn't throw`() {
        val sut = listOf(
            "       K" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "   q    " +
                    "        ",

            "K       " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "   q    " +
                    "        ",

            "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "   q    " +
                    "K       ",

            "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "   q    " +
                    "       K"
        ).map { stringBoard -> Board(stringBoard) }

        sut.forEach { board ->
            board.canKingMove(board.getKingPosition(Army.WHITE), Army.WHITE)
        }
    }

    // positionAttackers [✔]

    @Test
    fun `positionAttackers size is 2 if two enemy pieces attack the occupied position`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR"
        )

        val position = Board.Position('e', 6)

        assertTrue(sut.positionAttackers(position, Army.BLACK).size == 2)
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    @Test
    fun `positionAttackers is empty if no piece attacks the occupied position`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "    P   " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR"
        )

        val position = Board.Position('e', 4)

        assertTrue(sut.positionAttackers(position, Army.BLACK).isEmpty())
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    @Test
    fun `positionAttackers size is 1 if one enemy piece attacks the empty position`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppp ppp" +
                    "        " +
                    "    p   " +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR"
        )

        val position = Board.Position('e', 4)

        assertTrue(sut.positionAttackers(position, Army.BLACK).size == 1)
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    @Test
    fun `positionAttackers is empty if no piece attacks the empty position`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPP PPP" +
                    "RNBQKBNR"
        )

        val position = Board.Position('e', 4)

        assertTrue(sut.positionAttackers(position, Army.BLACK).isEmpty())
        assertTrue(sut.positionAttackers(position, Army.WHITE).isEmpty())
    }

    // getKingPosition [✔]

    @Test
    fun `getKingPosition returns position of king of specified army`() {
        val sut = Board(
            "        " +
                    "        " +
                    "        " +
                    "  q     " +
                    "        " +
                    "   K    " +
                    "    N   " +
                    "        "
        )

        assertEquals(Board.Position('d', 3), sut.getKingPosition(army = Army.WHITE))
    }

    @Test
    fun `getKingPosition throws if king of specified army isn't found`() {
        val sut = Board(
            "        " +
                    "        " +
                    "        " +
                    "  q     " +
                    "        " +
                    "        " +
                    "    N   " +
                    "        "
        )

        assertFailsWith<IllegalArgumentException> {
            sut.getKingPosition(army = Army.WHITE)
        }
    }

    // getAdjacentPositions [✔]

    @Test
    fun `getAdjacentPositions returns adjacent positions of corner position correctly`() {
        /*
        "       B" +
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        "
        */

        assertEquals(
            setOf(Board.Position(col = 'g', row = 8), Board.Position(col = 'g', row = 7), Board.Position(col = 'h', row = 7)),
            getAdjacentPositions(Board.Position(col = 'h', row = 8)).toSet()
        )
    }

    @Test
    fun `getAdjacentPositions returns adjacent positions of border (non-corner) position correctly`() {
        /*
        "  B     " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        " +
        "        "
        */

        assertEquals(
            setOf(
                Board.Position(col = 'b', row = 8), Board.Position(col = 'd', row = 8), Board.Position(col = 'c', row = 7),
                Board.Position(col = 'b', row = 7), Board.Position(col = 'd', row = 7)
            ),
            getAdjacentPositions(Board.Position(col = 'c', row = 8)).toSet()
        )
    }

    @Test
    fun `getAdjacentPositions returns adjacent positions of non-border position correctly`() {
        /*
        "        " +
        "        " +
        "        " +
        "    B   " +
        "        " +
        "        " +
        "        " +
        "        "
        */

        assertEquals(
            setOf(
                Board.Position(col = 'd', row = 5), Board.Position(col = 'f', row = 5), Board.Position(col = 'e', row = 4),
                Board.Position(col = 'e', row = 6), Board.Position(col = 'd', row = 4), Board.Position(col = 'd', row = 6),
                Board.Position(col = 'f', row = 4), Board.Position(col = 'f', row = 6)
            ),
            getAdjacentPositions(Board.Position(col = 'e', row = 5)).toSet()
        )
    }
}
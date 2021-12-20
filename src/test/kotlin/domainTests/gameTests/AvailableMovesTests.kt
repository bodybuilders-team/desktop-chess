package domainTests.gameTests

import domain.game.*
import domain.board.*
import domain.move.*
import domain.pieces.Army
import kotlin.test.*


class AvailableMovesTests {

    // getAvailableMoves

    @Test
    fun `getAvailableMoves returns emptyList when there are no available moves for queen`() {
        val sut = Board()
        val position = Board.Position('d', 1)
        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(emptyList(), piece.getAvailableMoves(Game(sut, emptyList()), position))
    }

    @Test
    fun `getAvailableMoves returns available moves for queen`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            " P P    " +
            "PP  PPPP" +
            "RN QKBNR"
        )
        val position = Board.Position('d', 1)
        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(
            setOf("Qd1c1", "Qd1c2", "Qd1d2"),
            piece.getAvailableMoves(Game(sut, emptyList()), position).map { it.toString() }.toSet()
        )
    }

    @Test
    fun `getAvailableMoves returns available moves, including castle move, for king`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "R   KBNR"
        )
        val position = Board.Position('e', 1)
        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(
            setOf("O-O-O", "Ke1d1"),
            piece.getAvailableMoves(Game(sut, emptyList()), position).map { it.toString() }.toSet()
        )
    }

    @Test
    fun `getAvailableMoves returns available moves, including en passant move, for pawn`() {
        val sut = Board(
            "rnbqkbnr" +
            " pp pppp" +
            "        " +
            "p  pP   " +
            "        " +
            "        " +
            "PPPP PPP" +
            "RNBQKBNR"
        )
        val position = Board.Position('e', 5)
        val previousMoves = listOfMoves("Pe2e4", "Pa7a5", "Pe4e5", "Pd7d5")

        val piece = sut.getPiece(position)

        assertNotNull(piece)
        assertEquals(
            setOf("Pe5e6", "Pe5d6"),
            piece.getAvailableMoves(Game(sut, previousMoves), position).map { it.toString() }.toSet()
        )
    }

    // hasAvailableMoves

    @Test
    fun `hasAvailableMoves with the army returns true if it's the army's turn and there are available moves`() {
        val game = Game(Board(), emptyList())
        assertTrue(game.hasAvailableMoves(Army.WHITE))
        assertFalse(game.hasAvailableMoves(Army.BLACK))
    }

    @Test
    fun `hasAvailableMoves with the army returns false if it's the army's turn and there are no available moves`() {
        val game = gameFromMoves(
            "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
            "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
        )
        assertFalse(game.hasAvailableMoves(Army.BLACK))
    }

    @Test
    fun `hasAvailableMoves returns false with empty board`() {
        val game = Game(Board("        ".repeat(BOARD_SIDE_LENGTH)), emptyList())
        assertFalse(game.hasAvailableMoves(Army.WHITE))
        assertFalse(game.hasAvailableMoves(Army.BLACK))
    }
}

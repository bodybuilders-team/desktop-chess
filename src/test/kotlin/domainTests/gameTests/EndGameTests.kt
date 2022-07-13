package domainTests.gameTests

import defaultGameResultingInBlackCheck
import defaultGameResultingInCheckMate
import defaultGameResultingInStaleMate
import defaultGameResultingInTie
import defaultGameResultingInWhiteCheck
import domain.board.Board
import domain.game.Game
import domain.game.ended
import domain.game.gameFromMoves
import domain.game.isKingInCheck
import domain.game.isKingInCheckMate
import domain.game.isKingInStaleMate
import domain.game.isTiedByFiftyMoveRule
import domain.game.isTiedByThreefold
import domain.game.makeMove
import domain.move.Move
import domain.pieces.Army
import listOfMoves
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EndGameTests { // [✔]

    // Game.ended [✔]

    @Test
    fun `Game is ended when there's a checkmate in game`() {
        assertTrue(defaultGameResultingInCheckMate.ended())
    }

    @Test
    fun `Game is ended when there's a stalemate in game`() {
        assertTrue(defaultGameResultingInStaleMate.ended())
    }

    @Test
    fun `Game is ended when there's a tie in game`() {
        assertTrue(defaultGameResultingInTie.ended())
    }

    // isKingInCheck [✔]

    @Test
    fun `isKingInCheck returns true if king is in check`() {
        val sut = Board(
            "        " +
                "        " +
                "        " +
                "   q    " +
                "        " +
                "   K    " +
                "        " +
                "        "
        )

        val army = Army.WHITE
        assertTrue(sut.isKingInCheck(army))
    }

    @Test
    fun `isKingInCheck returns false if king isn't in check`() {
        val sut = Board(
            "        " +
                "        " +
                "        " +
                "   q    " +
                "        " +
                "    K   " +
                "        " +
                "        "
        )

        val army = Army.WHITE
        assertFalse(sut.isKingInCheck(army))
    }

    // isKingInCheckMate [✔]

    @Test
    fun `isKingInCheckMate returns true if king is in checkmate`() {
        val sut = Board(
            "        " +
                "    Kq  " +
                "      b " +
                "   r    " +
                "        " +
                "        " +
                "        " +
                "        "
        )

        assertTrue(sut.isKingInCheckMate(Army.WHITE))
    }

    @Test
    fun `isKingInCheckMate returns false if king isn't in checkmate`() {
        val sut = Board(
            "        " +
                "    K   " +
                "      b " +
                "   r    " +
                "        " +
                "        " +
                "        " +
                "        "
        )

        assertFalse(sut.isKingInCheckMate(Army.WHITE))
    }

    // isKingInStaleMate [✔]

    @Test
    fun `isKingInStaleMate returns true if white king is in stalemate`() {
        val sut = Board(
            "       K" +
                "     qr " +
                "      b " +
                "   r    " +
                "        " +
                "        " +
                "        " +
                "        "
        )
        assertTrue(Game(sut, emptyList()).isKingInStaleMate(Army.WHITE))
    }

    @Test
    fun `isKingInStaleMate returns false if white king can't move and isn't in check but it's not its turn to play`() {
        val sut = Board(
            "       K" +
                "     qr " +
                "      b " +
                "   r    " +
                "        " +
                "        " +
                "        " +
                "        "
        )
        assertFalse(Game(sut, listOfMoves("Pe2e4")).isKingInStaleMate(Army.WHITE))
    }

    @Test
    fun `isKingInStaleMate returns true if black king is in stalemate`() {
        val sut = Board(
            "    K   " +
                "        " +
                "        " +
                "        " +
                "        " +
                " Q      " +
                " R      " +
                "k       "
        )
        assertTrue(Game(sut, listOfMoves("Pe2e4")).isKingInStaleMate(Army.BLACK))
    }

    @Test
    fun `isKingInStaleMate returns false if black king can't move and isn't in check but it's not its turn to play`() {
        val sut = Board(
            "    K   " +
                "        " +
                "        " +
                "        " +
                "        " +
                " Q      " +
                " R      " +
                "k       "
        )
        assertFalse(Game(sut, emptyList()).isKingInStaleMate(Army.BLACK))
    }

    @Test
    fun `isKingInStaleMate returns false if white king isn't in stalemate`() {
        val sut = Board(
            "        " +
                "    K   " +
                "      b " +
                "   r    " +
                "        " +
                "        " +
                "        " +
                "        "
        )

        assertFalse(Game(sut, emptyList()).isKingInStaleMate(Army.WHITE))
    }

    @Test
    fun `isKingInStaleMate returns false if black king isn't in stalemate`() {
        val sut = Board(
            "        " +
                "        " +
                "      B " +
                "   R    " +
                "        " +
                "        " +
                "     k  " +
                "        "
        )

        assertFalse(Game(sut, listOfMoves("Pe2e4")).isKingInStaleMate(Army.BLACK))
    }

    // isTiedByFiftyMoveRule [✔]

    @Test
    fun `Game is tied by 50 move rule if no pawn move and no capture happens in the last 100 moves (50 by each side)`() {
        assertTrue(defaultGameResultingInTie.isTiedByFiftyMoveRule())
    }

    @Test
    fun `Game is not tied by 50 move rule if a pawn moved and no capture happens in the last 100 moves (50 by each side)`() {
        assertFalse(defaultGameResultingInTie.makeMove(Move("Pe2e4")).isTiedByFiftyMoveRule())
    }

    @Test
    fun `Game is not tied by 50 move rule if no pawn moved and a capture happens in the last 100 moves (50 by each side)`() {
        val startingMoves = listOf("Nb1c3", "Nb8c6", "Nc3b5", "Nc6d4", "Nb5xd4")

        val moves = List(24) { listOf("Ng8h6", "Nd4b5", "Nh6g8", "Nb5d4") }.flatten()
        assertFalse(gameFromMoves(startingMoves + moves).isTiedByFiftyMoveRule())
    }

    @Test
    fun `Game is not tied by 50 move rule if a pawn moved and a capture happens in the last 100 moves (50 by each side)`() {
        val startingMoves = listOf("Pe2e4", "Pd7d5", "Pe4xd5", "Ph7h6")

        val moves = List(24) { listOf("Nb1c3", "Nb8c6", "Nc3b1", "Nc6b8") }.flatten()
        assertFalse(gameFromMoves(startingMoves + moves).isTiedByFiftyMoveRule())
    }

    // isTiedByThreefold [✔]
    @Test
    fun `Game is tied by threefold rule if knights move into the same position 3 times`() {
        assertTrue(defaultGameResultingInTie.isTiedByThreefold())
    }

    @Test
    fun `Game is not tied by threefold with game resulting in check mate`() {
        assertFalse(defaultGameResultingInCheckMate.isTiedByThreefold())
    }

    @Test
    fun `Game is not tied by threefold with game resulting in stale mate`() {
        assertFalse(defaultGameResultingInStaleMate.isTiedByThreefold())
    }

    @Test
    fun `Game is not tied by threefold with game resulting in white check`() {
        assertFalse(defaultGameResultingInWhiteCheck.isTiedByThreefold())
    }

    @Test
    fun `Game is not tied by threefold with game resulting in black check`() {
        assertFalse(defaultGameResultingInBlackCheck.isTiedByThreefold())
    }
}

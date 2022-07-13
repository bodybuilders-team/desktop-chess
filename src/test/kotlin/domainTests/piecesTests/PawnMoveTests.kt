package domainTests.piecesTests

import domain.board.Board
import domain.game.Game
import domain.move.Move
import domain.move.MoveType
import domain.pieces.Army
import domain.pieces.Pawn
import isValidMove
import listOfMoves
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PawnMoveTests { // [✔]
    private val sut = Board(
        "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "   n    " +
            "    P   " +
            "K       "
    )

    @Test
    fun `Pawn vertical one move is valid`() {
        assertTrue(sut.isValidMove("Pe2e3"))
    }

    @Test
    fun `Pawn vertical double move is valid`() {
        assertTrue(sut.isValidMove("Pe2e4"))
    }

    @Test
    fun `Pawn diagonal move with capture is valid`() {
        assertTrue(sut.isValidMove("Pe2d3"))
    }

    @Test
    fun `Pawn horizontal move isn't valid`() {
        assertFalse(sut.isValidMove("Pe2f2"))
    }

    @Test
    fun `Pawn diagonal move without capture isn't valid`() {
        assertFalse(sut.isValidMove("Pe2f3"))
    }

    @Test
    fun `Pawn vertical move backwards isn't valid`() {
        assertFalse(sut.isValidMove("Pe2e1"))
    }

    @Test
    fun `Pawn diagonal move backwards isn't valid`() {
        assertFalse(sut.isValidMove("Pe2f1"))
    }

    @Test
    fun `Pawn move to same place is not valid`() {
        assertFalse(sut.isValidMove("Pe2e2"))
    }

    @Test
    fun `Pawn single vertical move with piece in toPos is invalid`() {
        val sut2 = Board(
            "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "    N   " +
                "    P   " +
                "K       "
        )
        assertFalse(sut2.isValidMove("Pe2e3"))
    }

    @Test
    fun `Pawn double move with piece in toPos is invalid`() {
        val sut2 = Board(
            "        " +
                "        " +
                "        " +
                "        " +
                "    N   " +
                "        " +
                "    P   " +
                "K       "
        )
        assertFalse(sut2.isValidMove("Pe2e4"))
    }

    @Test
    fun `Pawn double move with piece in the way is invalid`() {
        val sut2 = Board(
            "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "    N   " +
                "    P   " +
                "K       "
        )
        assertFalse(sut2.isValidMove("Pe2e4"))
    }

    // isValidEnPassant [✔]

    @Test
    fun `isValidEnPassant returns true with valid en passant`() {
        val sutEnPassant = Board(
            "rnbqkbnr" +
                " ppp ppp" +
                "        " +
                "p   pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
        )

        assertTrue(Pawn(Army.WHITE).isValidEnPassant(sutEnPassant, Move("Pf5e6")))
    }

    @Test
    fun `isValidEnPassant returns false with invalid en passant`() {
        val sutEnPassant = Board(
            "rnbqkbnr" +
                " ppp ppp" +
                "        " +
                "p   pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
        )

        assertFalse(Pawn(Army.WHITE).isValidEnPassant(sutEnPassant, Move("Pe2e3")))
    }

    @Test
    fun `Pawn capture en passant to the left move is valid`() {
        val sutEnPassant = Board(
            "rnbqkbnr" +
                " ppp ppp" +
                "        " +
                "p   pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
        )

        val moves = listOfMoves("Pf2f4", "Pa7a5", "Pf4f5", "Pe7e5")

        assertEquals(MoveType.EN_PASSANT, Move.validated("Pf5e6", Game(sutEnPassant, moves)).type)
    }

    @Test
    fun `Pawn capture en passant to the right move is valid`() {
        val sutEnPassant = Board(
            "rnbqkbnr" +
                " ppppp p" +
                "        " +
                "p    Pp " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
        )
        val moves = listOfMoves("Pf2f4", "Pa7a5", "Pf4f5", "Pg7g5")

        assertEquals(MoveType.EN_PASSANT, Move.validated("Pf5g6", Game(sutEnPassant, moves)).type)
    }
}

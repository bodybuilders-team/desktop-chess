package domainTests.piecesTests

import domain.Game
import domain.board.*
import domain.move.Move
import domain.move.MoveType
import kotlin.test.*
import isValidMove


class PawnMoveTests {
    private val sut = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "   n    " +
            "    P   " +
            "K       "
        )
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

    // Pawn double move with piece in the way is invalid

    private val sut2 = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "    N   " +
            "    P   " +
            "K       "
        )
    )

    @Test
    fun `Pawn double move with piece in the way is invalid`() {
        assertFalse(sut2.isValidMove("Pe2e4"))
    }

    // En passant

    @Test
    fun `Pawn capture en passant to the left move is valid`() {
        val sutEnPassant = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                " ppp ppp" +
                "        " +
                "p   pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
            )
        )

        val moves = listOf("Pf2f4", "Pa7a5", "Pf4f5", "Pe7e5").map { Move(it) }

        assertEquals(MoveType.EN_PASSANT, Move.validated("Pf5e6", Game(sutEnPassant, moves)).type)
    }

    @Test
    fun `Pawn capture en passant to the right move is valid`() {
        val sutEnPassant = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                " ppppp p" +
                "        " +
                "p    Pp " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
            )
        )
        val moves = listOf("Pf2f4", "Pa7a5", "Pf4f5", "Pg7g5").map { Move(it) }

        assertEquals(MoveType.EN_PASSANT, Move.validated("Pf5g6", Game(sutEnPassant, moves)).type)
    }
}

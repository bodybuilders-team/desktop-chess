package domainTests.piecesTests

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

    //En passant

    @Test
    fun `Pawn capture en passant to the left move is valid`() {
        val sutEnPassant = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
            )
        )
        
        assertEquals(MoveType.EN_PASSANT, Move.validated("Pf5e6", sutEnPassant, listOf(Move("Pe7e5"))).type)
    }

    @Test
    fun `Pawn capture en passant to the right move is valid`() {
        val sutEnPassant = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppp p" +
                "        " +
                "     Pp " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
            )
        )
        
        assertEquals(MoveType.EN_PASSANT, Move.validated("Pf5g6", sutEnPassant, listOf(Move("Pg7g5"))).type)
    }
}

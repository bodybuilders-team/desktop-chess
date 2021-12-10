package domainTests.boardTests

import domain.board.*
import domain.move.IllegalMoveException
import makeMove
import kotlin.test.*


class BoardMakeMoveTests {

    @Test
    fun `MakeMove in Board`() {
        val sut = Board().makeMove("Pe2e4").makeMove("Pe7e5").makeMove("Nb1c3")
        assertEquals(
            "rnbqkbnr" +
            "pppp ppp" +
            "        " +
            "    p   " +
            "    P   " +
            "  N     " +
            "PPPP PPP" +
            "R BQKBNR", sut.toString()
        )
    }

    //Pawn promotion

    @Test
    fun `Promote pawn to queen`() {
        val sut = Board().makeMove("Pf2f4").makeMove("Pg7g5").makeMove("Pf4xg5").makeMove("Pg5g6").makeMove("Pg6g7")
            .makeMove("Ng8f6").makeMove("Pg7g8=Q")
        assertEquals(
            "rnbqkbQr" +
            "pppppp p" +
            "     n  " +
            "        " +
            "        " +
            "        " +
            "PPPPP PP" +
            "RNBQKBNR", sut.toString()
        )
    }

    @Test
    fun `Removing piece previously protecting king leaves king in a check and throws`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "    q   " +
                "        " +
                "    B   " +
                "        " +
                "    K   " +
                "        " +
                "        "
            )
        )

        assertFailsWith<IllegalMoveException> { sut.makeMove("Be5d6") }
    }

    @Test
    fun `King moving into check throws`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "    q   " +
                "        " +
                "        " +
                "        " +
                "     K  " +
                "        " +
                "        "
            )
        )

        assertFailsWith<IllegalMoveException> { sut.makeMove("Kf3e3") }
    }
}

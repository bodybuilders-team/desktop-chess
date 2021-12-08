package boardTests

import domain.board.*
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

    @Test
    fun `Move pawn to promotion`() {
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
}

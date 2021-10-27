import kotlin.test.*


class BoardTests {
    @Test
    fun `Initial position Board`() {
        val sut = Board()
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        ".repeat(4) +
            "PPPPPPPP" +
            "RNBQKBNR", sut.toString()
        )
    }

    @Test
    fun `Make move in Board (one move)`() {
        val sut = Board()
        sut.makeMove("Pe2e4")
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "    P   " +
            "        " +
            "PPPP PPP" +
            "RNBQKBNR", sut.toString()
        )
    }

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
    fun `MakeMove with capture in Board`() {
        val sut = Board().makeMove("Pe2e4").makeMove("Pd7d5").makeMove("Pe4xd5")
        assertEquals(
            "rnbqkbnr" +
            "ppp pppp" +
            "        " +
            "   P    " +
            "        " +
            "        " +
            "PPPP PPP" +
            "RNBQKBNR", sut.toString()
        )
    }

    @Test
    fun `MakeMove with promotion in Board`() {
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
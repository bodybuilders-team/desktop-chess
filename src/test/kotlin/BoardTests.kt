import kotlin.test.*


class BoardTests {
    @Test
    fun `Position with collum outside bounds throws`() {
        assertFailsWith<IllegalArgumentException> {
            Board.Position(col = 'x', row = 2)
        }
    }

    @Test
    fun `Position with row outside bounds throws`() {
        assertFailsWith<IllegalArgumentException> {
            Board.Position(col = 'a', row = 9)
        }
    }

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
    fun `Move with wrong pieceSymbol is invalid`() {
        assertFalse(Board().isValidMove(Move("Ke2e3")))
    }

    
    /*
      ------------------------------Pawn tests------------------------------
     */

    @Test
    fun `Move pawn (pawn's first move, can walk 1 square)`() {
        val sut = Board().makeMove("Pe2e3")
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "    P   " +
            "PPPP PPP" +
            "RNBQKBNR", sut.toString()
        )
    }

    @Test
    fun `Move pawn (pawn's first move, can walk 2 squares)`() {
        val sut = Board().makeMove("Pe2e4")
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

    /*
      ------------------------------Rook tests------------------------------
     */

    @Test
    fun `Move Rook - stays in place if same color piece is in its path`() {
        assertFailsWith<IllegalMoveException> { Board().makeMove("Pa2a3").makeMove("Ra1a4") }
    }

    @Test
    fun `Move Rook - stays in place if opponent's piece is in its path`() {
        assertFailsWith<IllegalMoveException> { Board().makeMove("Pa2a4").makeMove("Pb7b5").makeMove("Pb5a4").makeMove("Ra1a5")}
    }

    @Test
    fun `Move Rook - moves if no piece is in its path`() {
        val sut = Board().makeMove("Pa2a4").makeMove("Ra1a3")
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "P       " +
            "R       " +
            " PPPPPPP" +
            " NBQKBNR", sut.toString()
        )
    }

    @Test
    fun `Move Rook - captures piece vertically`() {
        val sut = Board().makeMove("Pa2a4").makeMove("Ra1a3").makeMove("Ra3b3").makeMove("Rb3b7")
        assertEquals(
            "rnbqkbnr" +
            "pRpppppp" +
            "        " +
            "        " +
            "P       " +
            "        " +
            " PPPPPPP" +
            " NBQKBNR", sut.toString()
        )
    }

    /*
      ------------------------------King tests------------------------------
     */
    
    @Test
    fun `Move King goes as expected`() {
        val sut = Board().makeMove("Pe2e4").makeMove("Ke1e2")
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "    P   " +
            "        " +
            "PPPPKPPP" +
            "RNBQ BNR", sut.toString()
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


    /*
      ------------------------------Bishop tests------------------------------
     */

    @Test
    fun `Move Bishop goes as expected`() {
        val sut = Board().makeMove("Pe2e4").makeMove("Bf1a6")
        assertEquals(
                "rnbqkbnr" +
                "pppppppp" +
                "B       " +
                "        " +
                "    P   " +
                "        " +
                "PPPP PPP" +
                "RNBQK NR", sut.toString()
        )
    }

    @Test
    fun `Move Bishop - stays in place if opponent's piece is in its path`() {
        assertFailsWith<IllegalMoveException> { Board().makeMove("Pe2e4").makeMove("Pb7b5").makeMove("Bf1a6") }
    }


    /*
      ------------------------------Queen tests------------------------------
     */

    @Test
    fun `Move Queen - can't go through ally pieces 1 `() {
        assertFailsWith<IllegalMoveException> { Board().makeMove("Qd1d5") }
    }

    @Test
    fun `Move Queen - can't go through ally pieces 2 `() {
        assertFailsWith<IllegalMoveException> { Board().makeMove("Qd1a4") }
    }

    @Test
    fun `Move Queen goes as expected - `() {
        val sut = Board().makeMove("Pe2e4").makeMove("Qd1h5").makeMove("Qh5a5").makeMove("Qa5a3")
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "    P   " +
            "Q       " +
            "PPPP PPP" +
            "RNB KBNR", sut.toString()
        )
    }


    /*
    @Test
    fun `King in Check`() {
        val testBoardInString =
            "rn qkbnr" +
            "ppp pppp" +
            "        " +
            "   p    " +
            "    P b " +
            "        " +
            "PPPPKPPP" +
            "RNBQ BNR"
        
        val sut = Board(getMatrix2DFromString(testBoardInString))

        assertEquals(CHECK_BY_ONE, sut.isKingInCheck(Color.WHITE))
    }
    */
}
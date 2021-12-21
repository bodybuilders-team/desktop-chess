package domainTests.moveTests

import domain.game.*
import domain.board.*
import domain.move.*
import domain.pieces.*
import kotlin.test.*


class MoveValidationTests { // [✔]
    
    // getValidatedMove [✔]

    @Test
    fun `getValidatedMove returns validated move with the correct move type information if it should be normal`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppppppp" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPPPPPP" +
                    "RNBQKBNR"
        )

        val move = Move("Pe2e4").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(move.copy(type = MoveType.NORMAL), move.getValidatedMove(piece, Game(sut, emptyList())))
    }

    @Test
    fun `getValidatedMove returns validated move with the correct move type information if it should be castle`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQK  R"
        )

        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(MoveType.NORMAL, move.type)
        assertEquals(move.copy(type = MoveType.CASTLE), move.getValidatedMove(piece, Game(sut, emptyList())))
    }

    @Test
    fun `getValidatedMove returns validated move with the correct move type information if it should be en passant`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppp ppp" +
            "        " +
            "    pP  " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        val move = Move("Pf5e6")
        val piece = sut.getPiece(move.from)
        val game = Game(sut, listOfMoves("Pe7e5"))

        assertNotNull(piece)
        assertEquals(MoveType.NORMAL, move.type)
        assertEquals(move.copy(type = MoveType.EN_PASSANT, capture = true), move.getValidatedMove(piece, game))
    }

    @Test
    fun `getValidatedMove returns validated move with the correct capture information if it should be a capture`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppp ppp" +
                    "        " +
                    "        " +
                    "    p   " +
                    "     P  " +
                    "PPPPP PP" +
                    "RNBQKBNR"
        )

        val move = Move("Pf3e4")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(move.copy(capture = true), move.getValidatedMove(piece, Game(sut, emptyList())))
    }

    @Test
    fun `getValidatedMove returns null with invalid move`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppp ppp" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPPPPPP" +
                    "RNBQKBNR"
        )

        val move = Move("Pe2e6")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertNull(move.getValidatedMove(piece, Game(sut, emptyList())))
    }

    @Test
    fun `getValidatedMove returns null with move that leaves king in check`() {
        val sut = Board(
            "rnb kbnr" +
            "pppp ppp" +
            "        " +
            "        " +
            "    q   " +
            "        " +
            "PPPPBPPP" +
            "RNBQK NR"
        )

        val move = Move("Be2f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertNull(move.getValidatedMove(piece, Game(sut, emptyList())))
    }
    
    // isValidNormal [✔]
    // Calls isValidCapture and the individual isValidMove of the moving piece.
    // Guarantee testing of both.

    @Test
    fun `isValidNormal returns true if the move is a valid normal move`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        val move = Move("Pe2e4")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    // isValidCapture [✔]

    @Test
    fun `isValidCapture returns true with valid capture to empty square`() {
        val sut = Board(
            "rnbqkbnr" +
                    "ppppp pp" +
                    "        " +
                    "     p  " +
                    "        " +
                    "        " +
                    "PPPPPPPP" +
                    "RNBQKBNR"
        )

        val move = Move("Ng1f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns true with valid capture to occupied square`() {
        val sut = Board(
            "rnbqkbnr" +
                    "pppp ppp" +
                    "        " +
                    "    p   " +
                    "        " +
                    "     N  " +
                    "PPPPPPPP" +
                    "RNBQKB R"
        )

        val move = Move("Nf3e5")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with invalid capture (capture a piece of the same army)`() {
        val sut = Board(
            "rnbqkbnr" +
                    "ppppp pp" +
                    "        " +
                    "     p  " +
                    "        " +
                    "     N  " +
                    "PPPPPPPP" +
                    "RNBQKB R"
        )

        val move = Move("Pf2f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with valid capture to empty square with wrong capture information`() {
        val sut = Board(
            "rnbqkbnr" +
            "ppppp pp" +
            "        " +
            "     p  " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        val move = Move("Pe2xe4")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns true with valid promotion`() {
        val sut = Board(
            "rnbqkb r" +
            "ppppppPp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        val move = Move("Pg7g8=Q")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with invalid promotion (invalid pawn position)`() {
        val sut = Board(
            "rnbqkb r" +
            "pppppp p" +
            "        " +
            "      P " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        val move = Move("Pg5g6=Q")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture return false with invalid promotion (promotion piece not specified)`() {
        val sut = Board(
            "rnbqkb r" +
                    "ppppppPp" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "PPPPPPPP" +
                    "RNBQKBNR"
        )

        val move = Move("Pg7g8")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    // isValidEnPassant [✔]
    // Calls isValidEnPassant of the pawn and isEnPassantPossible.
    // Guarantee testing of both.

    @Test
    fun `isValidEnPassant returns true if the move is a valid en passant`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppp ppp" +
            "        " +
            "    pP  " +
            "        " +
            "        " +
            "PPPPP PP" +
            "RNBQKBNR"
        )

        val move = Move("Pf5e6")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidEnPassant(piece, Game(sut, listOfMoves("Pf2f4", "Pf4f5", "Pe7e5"))))
    }

    @Test
    fun `isValidEnPassant returns false if the move isn't a valid en passant`() {
        val sut = Board(
            "rnbqkbnr" +
            "ppp pppp" +
            "   p    " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQKBNR"
        )

        val move = Move("Pf4f5")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidEnPassant(piece, Game(sut, listOfMoves("Pf2f4", "Pd7d6"))))
    }

    // isValidCastle [✔]
    // Calls isValidCastle of the king and isValidCastle.
    // Guarantee testing of both.

    @Test
    fun `isValidCastle returns true if the move is a valid long castle with king`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCastle(piece, Game(sut, listOfMoves("Pf2f4"))))
    }

    @Test
    fun `isValidCastle returns false if the move isn't a valid long castle with king (rook moved already)`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, Game(sut, listOfMoves("Ra1b1", "Rb1a1"))))
    }

    @Test
    fun `isValidCastle returns true if the move is a valid short castle with king`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCastle(piece, Game(sut, listOfMoves("Pf2f4"))))
    }

    @Test
    fun `isValidCastle returns false if the move isn't a valid short castle with king (rook moved already)`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, Game(sut, listOfMoves("Rh1g1", "Rg1h1"))))
    }

    @Test
    fun `isValidCastle returns false if the move is a valid long castle with king but the king is in check`() {
        val sut = Board(
            "rnbqk nr" +
            "ppp  ppp" +
            "        " +
            "        " +
            "     P b" +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )
        
        val game = Game(sut, listOfMoves("Pf2f4"))

        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertTrue(game.board.isKingInCheck(Army.WHITE))

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, game))
    }

    @Test
    fun `isValidCastle returns false if the move is a valid short castle with king but the king is in check`() {
        val sut = Board(
            "rnbqk nr" +
            "ppp  ppp" +
            "        " +
            "        " +
            "     P b" +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val game = Game(sut, listOfMoves("Pf2f4"))

        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertTrue(game.board.isKingInCheck(Army.WHITE))

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, game))
    }

    // isEnPassantPossible [✔]

    @Test
    fun `isEnPassantPossible returns true if an en passant move is possible to be made`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppp ppp" +
            "        " +
            "    pP  " +
            "        " +
            "        " +
            "PPPPP PP" +
            "RNBQKBNR"
        )

        val move = Move("Pf5e6")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOfMoves("Pf2f4", "Pf4f5", "Pe7e5")

        assertNotNull(piece)
        assertTrue(move.isEnPassantPossible(piece, previousMoves))
    }

    @Test
    fun `isEnPassantPossible returns false if an en passant move isn't possible to be made`() {
        val sut = Board(
            "rnbqkbnr" +
            "ppp pppp" +
            "   p    " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQKBNR"
        )

        val move = Move("Pf4f5")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOfMoves("Pf2f4", "Pd7d6")

        assertNotNull(piece)
        assertFalse(move.isEnPassantPossible(piece, previousMoves))
    }

    // isCastlePossible [✔]

    @Test
    fun `isCastlePossible returns true if a long castle move is possible to be made`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        val previousMoves = listOfMoves("Pf2f4")

        val game = Game(sut, previousMoves)
        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(piece is King && piece.isValidCastle(game.board, move))
        assertTrue(move.isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns false if a long castle move isn't possible to be made (rook moved already)`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        val previousMoves = listOfMoves("Ra1b1", "Rb1a1")
        
        val game = Game(sut, previousMoves)
        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(piece is King && piece.isValidCastle(game.board, move))
        assertFalse(move.isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns true if a long castle move is possible to be made if other rook moved`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        val previousMoves = listOfMoves("Rh1g1", "Rg1h1")

        val game = Game(sut, previousMoves)
        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(piece is King && piece.isValidCastle(game.board, move))
        assertTrue(move.isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns true if a short castle move is possible to be made`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val previousMoves = listOfMoves("Pf2f4")

        val game = Game(sut, previousMoves)
        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(piece is King && piece.isValidCastle(game.board, move))
        assertTrue(move.isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns false if a short castle move isn't possible to be made (rook moved already)`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val previousMoves = listOfMoves("Rh1g1", "Rg1h1")

        val game = Game(sut, previousMoves)
        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(piece is King && piece.isValidCastle(game.board, move))
        assertFalse(move.isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns true if a short castle move is possible to be made if other rook moved`() {
        val sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val previousMoves = listOfMoves("Ra1b1", "Rb1a1")

        val game = Game(sut, previousMoves)
        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(piece is King && piece.isValidCastle(game.board, move))
        assertTrue(move.isCastlePossible(piece, previousMoves))
    }

    // getEnPassantCapturedPawnPosition [✔]

    @Test
    fun `getEnPassantCapturedPawnPosition with valid attackerPos returns correct captured pawn position`() {
        assertEquals(
            Board.Position(col = 'b', row = 5),
            getEnPassantCapturedPawnPosition(Board.Position(col = 'b', row = 6), Pawn(Army.WHITE))
        )
    }
    
    // Castle.getRookPosition [✔]

    @Test
    fun `getRookPosition with valid short castle king toPos returns correct rook fromPos`() {
        assertEquals(
            Board.Position(col = INITIAL_ROOK_COL_CLOSER_TO_KING, row = WHITE_FIRST_ROW),
            Castle.getRookPosition(Board.Position(col = SHORT_CASTLE_KING_COL, row = WHITE_FIRST_ROW))
        )
    }

    @Test
    fun `getRookPosition with valid long castle king toPos returns correct rook fromPos`() {
        assertEquals(
            Board.Position(col = INITIAL_ROOK_COL_FURTHER_FROM_KING, row = WHITE_FIRST_ROW),
            Castle.getRookPosition(Board.Position(col = LONG_CASTLE_KING_COL, row = WHITE_FIRST_ROW))
        )
    }

    // Castle.getRookToPosition [✔]

    @Test
    fun `getRookToPosition with valid short castle king toPos returns correct rook toPos`() {
        assertEquals(
            Board.Position(col = SHORT_CASTLE_ROOK_COL, row = WHITE_FIRST_ROW),
            Castle.getRookToPosition(Board.Position(col = SHORT_CASTLE_KING_COL, row = WHITE_FIRST_ROW))
        )
    }

    @Test
    fun `getRookToPosition with valid long castle king toPos returns correct rook toPos`() {
        assertEquals(
            Board.Position(col = LONG_CASTLE_ROOK_COL, row = WHITE_FIRST_ROW),
            Castle.getRookToPosition(Board.Position(col = LONG_CASTLE_KING_COL, row = WHITE_FIRST_ROW))
        )
    }
}

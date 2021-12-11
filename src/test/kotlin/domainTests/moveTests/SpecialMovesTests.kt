package domainTests.moveTests

import domain.Game
import domain.board.*
import domain.move.*
import domain.pieces.*
import kotlin.test.*


class SpecialMovesTests {

    // isValidEnPassant

    @Test
    fun `isValidEnPassant returns true if the move is a valid en passant`() {
        val sut = Board(
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

        val move = Move("Pf5e6")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidEnPassant(piece, Game(sut, listOf("Pf2f4", "Pf4f5", "Pe7e5").map { Move(it) })))
    }

    @Test
    fun `isValidEnPassant returns false if the move isn't a valid en passant`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppp pppp" +
                "   p    " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pf4f5")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidEnPassant(piece, Game(sut, listOf("Pf2f4", "Pd7d6").map { Move(it) })))
    }

    // isValidCastle

    @Test
    fun `isValidCastle returns true if the move is a valid long castle with king`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR"
            )
        )

        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCastle(piece, Game(sut, listOf("Pf2f4").map { Move(it) })))
    }

    @Test
    fun `isValidCastle returns false if the move isn't a valid long castle with king`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR"
            )
        )

        val move = Move("Ke1c1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, Game(sut, listOf("Ra1b1", "Rb1a1").map { Move(it) })))
    }

    @Test
    fun `isValidCastle returns true if the move is a valid short castle with king`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQK  R"
            )
        )

        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCastle(piece, Game(sut, listOf("Pf2f4").map { Move(it) })))
    }

    @Test
    fun `isValidCastle returns false if the move isn't a valid short castle with king`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQK  R"
            )
        )

        val move = Move("Ke1g1")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, Game(sut, listOf("Rh1g1", "Rg1h1").map { Move(it) })))
    }

    // isEnPassantPossible

    @Test
    fun `isEnPassantPossible returns true if an en passant move is possible to be made`() {
        val sut = Board(
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

        val move = Move("Pf5e6")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(
            Move("Pf2f4"), Move("Pf4f5"),
            Move("Pe7e5")
        )

        assertNotNull(piece)
        assertTrue(move.isEnPassantPossible(piece, previousMoves))
    }

    @Test
    fun `isEnPassantPossible returns false if an en passant move isn't possible to be made`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppp pppp" +
                "   p    " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pf4f5")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move("Pf2f4"), Move("Pd7d6"))

        assertNotNull(piece)
        assertFalse(move.isEnPassantPossible(piece, previousMoves))
    }

    // isCastlePossible

    @Test
    fun `isCastlePossible returns true if a castle move is possible to be made`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR"
            )
        )

        val move = Move("Ra1d1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move("Pf2f4"))

        assertNotNull(piece)
        assertTrue(isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns false if a castle move isn't possible to be made`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR"
            )
        )

        val move = Move("Ra1d1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move("Ra1b1"), Move("Rb1a1"))

        assertNotNull(piece)
        assertFalse(isCastlePossible(piece, previousMoves))
    }

    // Castle.getRookPosition

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

    // Castle.getRookToPosition

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

    // getEnPassantCapturedPawnPosition

    @Test
    fun `getEnPassantCapturedPawnPosition with valid attackerPos returns correct captured pawn position`() {
        assertEquals(
            Board.Position(col = 'b', row = 5),
            getEnPassantCapturedPawnPosition(Board.Position(col = 'b', row = 6), Pawn(Army.WHITE))
        )
    }
}

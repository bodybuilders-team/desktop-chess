package moveTests

import domain.board.*
import domain.move.*
import domain.pieces.Army
import domain.pieces.Pawn
import kotlin.test.*

class SpecialMovesTests {
    
    //isValidEnPassant
    
    @Test
    fun `isValidEnPassant returns true if the move is a valid en passant`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR")
        )
        
        val move = Move.getUnvalidatedMove("Pf5e6")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"), Move.getUnvalidatedMove("Pf4f5"),
                                    Move.getUnvalidatedMove("Pe7e5"))
        
        assertNotNull(piece)
        assertTrue(move.isValidEnPassant(piece, sut, previousMoves))
    }

    @Test
    fun `isValidEnPassant returns false if the move isn't a valid en passant`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppp pppp" +
                "   p    " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR")
        )

        val move = Move.getUnvalidatedMove("Pf4f5")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"), Move.getUnvalidatedMove("Pd7d6"))

        assertNotNull(piece)
        assertFalse(move.isValidEnPassant(piece, sut, previousMoves))
    }
    
    //isValidCastle

    @Test
    fun `isValidCastle returns true if the move is a valid long castle with king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR")
        )

        val move = Move.getUnvalidatedMove("Ke1c1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"))

        assertNotNull(piece)
        assertTrue(move.isValidCastle(piece, sut, previousMoves))
    }

    @Test
    fun `isValidCastle returns false if the move isn't a valid long castle with king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR")
        )

        val move = Move.getUnvalidatedMove("Ke1c1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Ra1b1"), Move.getUnvalidatedMove("Rb1a1"))

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, sut, previousMoves))
    }

    @Test
    fun `isValidCastle returns true if the move is a valid short castle with king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQK  R")
        )

        val move = Move.getUnvalidatedMove("Ke1g1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"))

        assertNotNull(piece)
        assertTrue(move.isValidCastle(piece, sut, previousMoves))
    }

    @Test
    fun `isValidCastle returns false if the move isn't a valid short castle with king`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "RNBQK  R")
        )

        val move = Move.getUnvalidatedMove("Ke1g1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Rh1g1"), Move.getUnvalidatedMove("Rg1h1"))

        assertNotNull(piece)
        assertFalse(move.isValidCastle(piece, sut, previousMoves))
    }

    //isEnPassantPossible

    @Test
    fun `isEnPassantPossible returns true if an en passant move is possible to be made`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                        "pppp ppp" +
                        "        " +
                        "    pP  " +
                        "        " +
                        "        " +
                        "PPPPP PP" +
                        "RNBQKBNR")
        )

        val move = Move.getUnvalidatedMove("Pf5e6")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"), Move.getUnvalidatedMove("Pf4f5"),
            Move.getUnvalidatedMove("Pe7e5"))

        assertNotNull(piece)
        assertTrue(move.isEnPassantPossible(piece, previousMoves))
    }

    @Test
    fun `isEnPassantPossible returns false if an en passant move isn't possible to be made`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                        "ppp pppp" +
                        "   p    " +
                        "        " +
                        "     P  " +
                        "        " +
                        "PPPPP PP" +
                        "RNBQKBNR")
        )

        val move = Move.getUnvalidatedMove("Pf4f5")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"), Move.getUnvalidatedMove("Pd7d6"))

        assertNotNull(piece)
        assertFalse(move.isEnPassantPossible(piece, previousMoves))
    }
    
    //isCastlePossible

    @Test
    fun `isCastlePossible returns true if a castle move is possible to be made`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR")
        )

        val move = Move.getUnvalidatedMove("Ra1d1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Pf2f4"))

        assertNotNull(piece)
        assertTrue(isCastlePossible(piece, previousMoves))
    }

    @Test
    fun `isCastlePossible returns false if a castle move isn't possible to be made`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "     P  " +
                "        " +
                "PPPPP PP" +
                "R   KBNR")
        )

        val move = Move.getUnvalidatedMove("Ra1d1")
        val piece = sut.getPiece(move.from)
        val previousMoves = listOf(Move.getUnvalidatedMove("Ra1b1"), Move.getUnvalidatedMove("Rb1a1"))

        assertNotNull(piece)
        assertFalse(isCastlePossible(piece, previousMoves))
    }

    //Castle.getRookPosition
    
    @Test
    fun `getRookPosition with valid short castle king toPos returns correct rook fromPos`(){
        assertEquals(Board.Position(col = INITIAL_ROOK_COL_CLOSER_TO_KING, row = WHITE_FIRST_ROW),
            Castle.getRookPosition(Board.Position(col = SHORT_CASTLE_KING_COL, row = WHITE_FIRST_ROW)))
    }

    @Test
    fun `getRookPosition with valid long castle king toPos returns correct rook fromPos`(){
        assertEquals(Board.Position(col = INITIAL_ROOK_COL_FURTHER_FROM_KING, row = WHITE_FIRST_ROW),
            Castle.getRookPosition(Board.Position(col = LONG_CASTLE_KING_COL, row = WHITE_FIRST_ROW)))
    }

    //Castle.getRookToPosition

    @Test
    fun `getRookToPosition with valid short castle king toPos returns correct rook toPos`(){
        assertEquals(Board.Position(col = SHORT_CASTLE_ROOK_COL, row = WHITE_FIRST_ROW),
            Castle.getRookToPosition(Board.Position(col = SHORT_CASTLE_KING_COL, row = WHITE_FIRST_ROW)))
    }

    @Test
    fun `getRookToPosition with valid long castle king toPos returns correct rook toPos`(){
        assertEquals(Board.Position(col = LONG_CASTLE_ROOK_COL, row = WHITE_FIRST_ROW),
            Castle.getRookToPosition(Board.Position(col = LONG_CASTLE_KING_COL, row = WHITE_FIRST_ROW)))
    }

    //getEnPassantCapturedPawnPosition

    @Test
    fun `getEnPassantCapturedPawnPosition with valid attackerPos returns correct captured pawn position`(){
        assertEquals(Board.Position(col = 'b', row = 5),
            getEnPassantCapturedPawnPosition(Board.Position(col = 'b', row = 6), Pawn(Army.WHITE)))
    }
    
}
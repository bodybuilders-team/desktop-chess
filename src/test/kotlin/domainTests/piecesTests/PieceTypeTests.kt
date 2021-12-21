package domainTests.piecesTests

import domain.pieces.*
import kotlin.test.*


class PieceTypeTests { // [✔]
    
    // Piece type symbol [✔]

    @Test
    fun `Check if Pawn's piece type is equal to expected`() {
        assertEquals('P', Pawn(Army.WHITE).type.symbol)
    }

    @Test
    fun `Check if Rook's piece type is equal to expected`() {
        assertEquals('R', Rook(Army.WHITE).type.symbol)
    }

    @Test
    fun `Check if Bishop's piece type is equal to expected`() {
        assertEquals('B', Bishop(Army.WHITE).type.symbol)
    }

    @Test
    fun `Check if Knight's piece type is equal to expected`() {
        assertEquals('N', Knight(Army.WHITE).type.symbol)
    }

    @Test
    fun `Check if Queens's piece type is equal to expected`() {
        assertEquals('Q', Queen(Army.WHITE).type.symbol)
    }

    @Test
    fun `Check if King's piece type is equal to expected`() {
        assertEquals('K', King(Army.WHITE).type.symbol)
    }
    
    // getPieceFromSymbol [✔]

    @Test
    fun `getPieceFromSymbol returns correct PieceType Pawn`() {
        assertTrue(getPieceFromSymbol('P', Army.WHITE) is Pawn)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Rook`() {
        assertTrue(getPieceFromSymbol('R', Army.WHITE) is Rook)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Bishop`() {
        assertTrue(getPieceFromSymbol('B', Army.WHITE) is Bishop)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Knight`() {
        assertTrue(getPieceFromSymbol('N', Army.WHITE) is Knight)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Queen`() {
        assertTrue(getPieceFromSymbol('Q', Army.WHITE) is Queen)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType King`() {
        assertTrue(getPieceFromSymbol('K', Army.WHITE) is King)
    }

    // PieceType get() [✔]

    @Test
    fun `PieceType get with 'P' returns PieceType Pawn`() {
        assertEquals(PieceType.PAWN, PieceType['P'])
    }

    @Test
    fun `PieceType get with 'R' returns PieceType Rook`() {
        assertEquals(PieceType.ROOK, PieceType['R'])
    }

    @Test
    fun `PieceType get with 'N' returns PieceType Knight`() {
        assertEquals(PieceType.KNIGHT, PieceType['N'])
    }

    @Test
    fun `PieceType get with 'B' returns PieceType Bishop`() {
        assertEquals(PieceType.BISHOP, PieceType['B'])
    }

    @Test
    fun `PieceType get with 'K' returns PieceType King`() {
        assertEquals(PieceType.KING, PieceType['K'])
    }

    @Test
    fun `PieceType get with 'Q' returns PieceType Queen`() {
        assertEquals(PieceType.QUEEN, PieceType['Q'])
    }
    
    // Piece toString [✔]

    @Test
    fun `Piece toString with white pawn works correctly`() {
        assertEquals("WHITE PAWN", Pawn(Army.WHITE).toString())
    }

    @Test
    fun `Piece toString with black pawn works correctly`() {
        assertEquals("BLACK PAWN", Pawn(Army.BLACK).toString())
    }

    @Test
    fun `Piece toString with white rook works correctly`() {
        assertEquals("WHITE ROOK", Rook(Army.WHITE).toString())
    }

    @Test
    fun `Piece toString with black rook works correctly`() {
        assertEquals("BLACK ROOK", Rook(Army.BLACK).toString())
    }

    @Test
    fun `Piece toString with white knight works correctly`() {
        assertEquals("WHITE KNIGHT", Knight(Army.WHITE).toString())
    }

    @Test
    fun `Piece toString with black knight works correctly`() {
        assertEquals("BLACK KNIGHT", Knight(Army.BLACK).toString())
    }

    @Test
    fun `Piece toString with white bishop works correctly`() {
        assertEquals("WHITE BISHOP", Bishop(Army.WHITE).toString())
    }

    @Test
    fun `Piece toString with black bishop works correctly`() {
        assertEquals("BLACK BISHOP", Bishop(Army.BLACK).toString())
    }

    @Test
    fun `Piece toString with white queen works correctly`() {
        assertEquals("WHITE QUEEN", Queen(Army.WHITE).toString())
    }

    @Test
    fun `Piece toString with black queen works correctly`() {
        assertEquals("BLACK QUEEN", Queen(Army.BLACK).toString())
    }

    @Test
    fun `Piece toString with white king works correctly`() {
        assertEquals("WHITE KING", King(Army.WHITE).toString())
    }
    @Test
    fun `Piece toString with black king works correctly`() {
        assertEquals("BLACK KING", King(Army.BLACK).toString())
    }
    
    
    // Piece toChar [✔]

    @Test
    fun `White Pawn toChar returns character as seen in-game`() {
        assertEquals('P', Pawn(Army.WHITE).toChar())
    }

    @Test
    fun `Black Pawn toChar returns character as seen in-game`() {
        assertEquals('p', Pawn(Army.BLACK).toChar())
    }

    @Test
    fun `White Rook toChar returns character as seen in-game`() {
        assertEquals('R', Rook(Army.WHITE).toChar())
    }

    @Test
    fun `Black Rook toChar returns character as seen in-game`() {
        assertEquals('r', Rook(Army.BLACK).toChar())
    }

    @Test
    fun `White Knight toChar returns character as seen in-game`() {
        assertEquals('N', Knight(Army.WHITE).toChar())
    }

    @Test
    fun `Black Knight toChar returns character as seen in-game`() {
        assertEquals('n', Knight(Army.BLACK).toChar())
    }

    @Test
    fun `White Bishop toChar returns character as seen in-game`() {
        assertEquals('B', Bishop(Army.WHITE).toChar())
    }

    @Test
    fun `Black Bishop toChar returns character as seen in-game`() {
        assertEquals('b', Bishop(Army.BLACK).toChar())
    }

    @Test
    fun `White Queen toChar returns character as seen in-game`() {
        assertEquals('Q', Queen(Army.WHITE).toChar())
    }

    @Test
    fun `Black Queen toChar returns character as seen in-game`() {
        assertEquals('q', Queen(Army.BLACK).toChar())
    }

    @Test
    fun `White King toChar returns character as seen in-game`() {
        assertEquals('K', King(Army.WHITE).toChar())
    }

    @Test
    fun `Black King toChar returns character as seen in-game`() {
        assertEquals('k', King(Army.BLACK).toChar())
    }
}

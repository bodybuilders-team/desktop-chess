package piecesTests

import domain.pieces.*
import kotlin.test.*


class PieceTypeTests {

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
}

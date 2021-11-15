package piecesTests

import domain.pieces.*
import kotlin.test.*



class PieceSymbolTests {

    @Test
    fun `Check if Pawn's piece type is equal to expected`() {
        val pawn = Pawn(Army.WHITE)
        assertEquals('P', pawn.symbol)
    }

    @Test
    fun `Check if Rook's piece type is equal to expected`() {
        val rook = Rook(Army.WHITE)
        assertEquals('R', rook.symbol)
    }

    @Test
    fun `Check if Bishop's piece type is equal to expected`() {
        val bishop = Bishop(Army.WHITE)
        assertEquals('B', bishop.symbol)
    }

    @Test
    fun `Check if Knight's piece type is equal to expected`() {
        val knight = Knight(Army.WHITE)
        assertEquals('N', knight.symbol)
    }

    @Test
    fun `Check if Queens's piece type is equal to expected`() {
        val queen = Queen(Army.WHITE)
        assertEquals('Q', queen.symbol)
    }

    @Test
    fun `Check if King's piece type is equal to expected`() {
        val king = King(Army.WHITE)
        assertEquals('K', king.symbol)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Pawn`() {
        val piece = getPieceFromSymbol('P', Army.WHITE)
        assertTrue(piece is Pawn)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Rook`() {
        val piece = getPieceFromSymbol('R', Army.WHITE)
        assertTrue(piece is Rook)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Bishop`() {
        val piece = getPieceFromSymbol('B', Army.WHITE)
        assertTrue(piece is Bishop)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Knight`() {
        val piece = getPieceFromSymbol('N', Army.WHITE)
        assertTrue(piece is Knight)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Queen`() {
        val piece = getPieceFromSymbol('Q', Army.WHITE)
        assertTrue(piece is Queen)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType King`() {
        val piece = getPieceFromSymbol('K', Army.WHITE)
        assertTrue(piece is King)
    }
}

import kotlin.test.*
import pieces.*

class PieceTests {

    @Test
    fun `Check if Pawn's piece type is equal to expected`() {
        val pawn = Pawn(Color.WHITE)
        assertEquals('P', pawn.symbol)
    }

    @Test
    fun `Check if Rook's piece type is equal to expected`() {
        val rook = Rook(Color.WHITE)
        assertEquals('R', rook.symbol)
    }

    @Test
    fun `Check if Bishop's piece type is equal to expected`() {
        val bishop = Bishop(Color.WHITE)
        assertEquals('B', bishop.symbol)
    }

    @Test
    fun `Check if Knight's piece type is equal to expected`() {
        val knight = Knight(Color.WHITE)
        assertEquals('N', knight.symbol)
    }

    @Test
    fun `Check if Queens's piece type is equal to expected`() {
        val queen = Queen(Color.WHITE)
        assertEquals('Q', queen.symbol)
    }

    @Test
    fun `Check if King's piece type is equal to expected`() {
        val king = King(Color.WHITE)
        assertEquals('K', king.symbol)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Pawn`() {
        val piece = getPieceFromSymbol('P', Color.WHITE)
        assertTrue(piece is Pawn)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Rook`() {
        val piece = getPieceFromSymbol('R', Color.WHITE)
        assertTrue(piece is Rook)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Bishop`() {
        val piece = getPieceFromSymbol('B', Color.WHITE)
        assertTrue(piece is Bishop)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Knight`() {
        val piece = getPieceFromSymbol('N', Color.WHITE)
        assertTrue(piece is Knight)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Queen`() {
        val piece = getPieceFromSymbol('Q', Color.WHITE)
        assertTrue(piece is Queen)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType King`() {
        val piece = getPieceFromSymbol('K', Color.WHITE)
        assertTrue(piece is King)
    }


    @Test
    fun `checkMoveValidity works as expected`() {
        val piece = getPieceFromSymbol('P', Color.WHITE)

        assertTrue(piece.validInitialPiece(board = Board(), Move("Pe2e4")))
    }

    @Test
    fun `checkMoveValidity with wrong piece symbol returns false`() {
        val piece = getPieceFromSymbol('P', Color.WHITE)

        assertFalse(piece.validInitialPiece(board = Board(), Move("Ke2e4")))
    }

}
import kotlin.test.*


class PieceTests {

    @Test
    fun `Check if Pawn's piece type is equal to expected`() {
        val pawn = Pawn(Color.WHITE)
        assertEquals(pawn.symbol, 'P')
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
        val white = Color.WHITE
        val piece = getPieceFromSymbol('P', white)
        assertTrue(piece is Pawn)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Rook`() {
        val white = Color.WHITE
        val piece = getPieceFromSymbol('R', white)
        assertTrue(piece is Rook)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Bishop`() {
        val white = Color.WHITE
        val piece = getPieceFromSymbol('B', white)
        assertTrue(piece is Bishop)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Knight`() {
        val white = Color.WHITE
        val piece = getPieceFromSymbol('N', white)
        assertTrue(piece is Knight)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType Queen`() {
        val white = Color.WHITE
        val piece = getPieceFromSymbol('Q', white)
        assertTrue(piece is Queen)
    }

    @Test
    fun `getPieceFromSymbol returns correct PieceType King`() {
        val white = Color.WHITE
        val piece = getPieceFromSymbol('K', white)
        assertTrue(piece is King)
    }

}
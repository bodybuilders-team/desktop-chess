package moveTests

import domain.board.*
import domain.move.*
import domain.pieces.PieceType
import kotlin.test.*


class MoveMethodsTests {

    @Test
    fun `Move to same place doesn't work`() {
        assertFalse(Move("e2e2").isVertical())
        assertFalse(Move("e2e2").isHorizontal())
        assertFalse(Move("e2e2").isStraight())
        assertFalse(Move("e2e2").isDiagonal())
    }
    
    //Vertical

    @Test
    fun `isVertical with vertical move works`() {
        assertTrue(Move("e2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`() {
        assertFalse(Move("e2f2").isVertical())
    }
    
    //Horizontal
    
    @Test
    fun `isHorizontal with horizontal move works`() {
        assertTrue(Move("e2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`() {
        assertFalse(Move("e2e4").isHorizontal())
    }
    
    //Straight

    @Test
    fun `isStraight with horizontal move works`() {
        assertTrue(Move("e2f2").isStraight())
    }

    @Test
    fun `isStraight with vertical move works`() {
        assertTrue(Move("e2e4").isStraight())
    }

    @Test
    fun `isStraight with diagonal move doesn't work`() {
        assertFalse(Move("e2g4").isStraight())
    }
    
    //Diagonal

    @Test
    fun `isDiagonal with diagonal move works`() {
        assertTrue(Move("e2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with straight move doesn't work`() {
        assertFalse(Move("e2f4").isDiagonal())
    }

    //Distances
    
    @Test
    fun `rowsDistance works`() {
        assertEquals(2, Move("e2e4").rowsDistance())
    }

    @Test
    fun `rowsDistance with no distance works`() {
        assertEquals(0, Move("e2f2").rowsDistance())
    }

    @Test
    fun `colsDistance works`() {
        assertEquals(1, Move("e2f2").colsDistance())
    }

    @Test
    fun `colsDistance with no distance works`() {
        assertEquals(0, Move("e2e5").colsDistance())
    }

    @Test
    fun `rowsAbsoluteDistance works`() {
        assertEquals(2, Move("e4e2").rowsAbsoluteDistance())
    }

    @Test
    fun `rowsAbsoluteDistance with no distance works`() {
        assertEquals(0, Move("e6f6").rowsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance works`() {
        assertEquals(1, Move("b2a2").colsAbsoluteDistance())
    }

    @Test
    fun `colsAbsoluteDistance with no distance works`() {
        assertEquals(0, Move("h2h7").colsAbsoluteDistance())
    }
    
    //getUnvalidatedMove

    @Test
    fun `getUnvalidatedMove returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Board.Position('e', 2), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pe2e4")
        )
    }

    @Test
    fun `getUnvalidatedMove with optional from position returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Board.Position('a', 1), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pe4")
        )
        assertEquals(
            Move('P', Board.Position('e', 1), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pee4")
        )
        assertEquals(
            Move('P', Board.Position('a', 2), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("P2e4")
        )
    }

    @Test
    fun `getUnvalidatedMove with supposedly invalid move returns unvalidated move correctly`() {
        assertEquals(
            Move('K', Board.Position('e', 1), false, Board.Position('g', 8), null, MoveType.NORMAL),
            Move("Ke1g8")
        )
    }

    //toString
    
    @Test
    fun `toString works`() {
        assertEquals("Pe2e4", Move("Pe2e4").toString())
    }

    @Test
    fun `toString with optional from position works`() {
        assertEquals("Pe4", Move("Pe4").toString(optionalFromCol = true, optionalFromRow = true))
        assertEquals("Pee4", Move("Pee4").toString(optionalFromCol = false, optionalFromRow = true))
        assertEquals("P2e4", Move("P2e4").toString(optionalFromCol = true, optionalFromRow = false))
    }
    
    //isValidCapture

    @Test
    fun `isValidCapture returns true with valid capture to empty square`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR")
        )

        val move = Move("Ng1f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns true with valid capture to occupied square`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    p   " +
                "        " +
                "     N  " +
                "PPPPPPPP" +
                "RNBQKB R")
        )

        val move = Move("Nf3e5")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with invalid capture`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "        " +
                "     N  " +
                "PPPPPPPP" +
                "RNBQKB R")
        )

        val move = Move("Pf2f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with valid capture to empty square with wrong capture information`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR")
        )

        val move = Move("Pe2e4").copy(capture = true)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns true with valid promotion`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkb r" +
                "ppppppPp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR")
        )

        val move = Move("Pg7g8").copy(promotion = PieceType.QUEEN.symbol)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with invalid promotion`(){
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkb r" +
                "pppppp p" +
                "        " +
                "      P " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR")
        )

        val move = Move("Pg5g6").copy(promotion = PieceType.QUEEN.symbol)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }
    
    //getValidatedMove

    @Test
    fun `getValidatedMove returns validated move with the correct move type information`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbNr" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR")
        )

        val move = Move("Pe2e4").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(move.copy(type = MoveType.NORMAL), move.getValidatedMove(piece, sut, emptyList()))
    }

    @Test
    fun `getValidatedMove returns validated move with the correct capture information`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbNr" +
                "pppp ppp" +
                "        " +
                "        " +
                "    p   " +
                "     P  " +
                "PPPPP PP" +
                "RNBQKBNR")
        )

        val move = Move("Pf3e4")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(move.copy(capture = true), move.getValidatedMove(piece, sut, emptyList()))
    }

    @Test
    fun `getValidatedMove returns null with invalid move`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbNr" +
                "pppp ppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR")
        )

        val move = Move("Pe2e6")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertNull(move.getValidatedMove(piece, sut, emptyList()))
    }
}

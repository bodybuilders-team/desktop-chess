package boardTests

import domain.board.*
import domain.board.Board.*
import domain.move.Move
import domain.move.MoveType
import domain.move.isValidCastle
import domain.pieces.*
import ui.console.printBoard
import kotlin.test.*


class BoardMethodsTests {
    private val sut = Board()


    @Test
    fun `Initial position Board`() {
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        ".repeat(4) +
            "PPPPPPPP" +
            "RNBQKBNR", sut.toString()
        )
    }
    
    //getPiece

    @Test
    fun `getPiece on an occupied position returns piece`() {
        assertTrue(sut.getPiece(Position('e', 2)) is Pawn)
        assertTrue(sut.getPiece(Position('e', 1)) is King)
    }


    @Test
    fun `getPiece on an empty position returns null`() {
        assertNull(sut.getPiece(Position('e', 5)))
    }

    //setPiece

    @Test
    fun `setPiece places piece in the position`() {
        val piece = Pawn(Army.WHITE)
        val position = Position('e', 5)
        sut.setPiece(position, piece)

        assertEquals(piece, sut.getPiece(position))
    }

    //removePiece
    
    @Test
    fun `removePiece removes piece from occupied position`() {
        val position = Position('e', 2)
        sut.removePiece(position)

        assertNull(sut.getPiece(position))
    }

    @Test
    fun `removePiece removes piece from empty position`() {
        val position = Position('e', 5)
        sut.removePiece(position)

        assertNull(sut.getPiece(position))
    }

    //isPositionOccupied
    
    @Test
    fun `isPositionOccupied with an occupied position returns true`() {
        assertTrue(sut.isPositionOccupied(Position('e', 2)))
    }

    @Test
    fun `isPositionOccupied with an empty position returns false`() {
        assertFalse(sut.isPositionOccupied(Position('e', 5)))
    }

    //getMatrix2DFromString
    
    @Test
    fun `getMatrix2DFromString returns a Matrix containing the respective pieces`() {
        val sut =   "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp"

        val expected = Matrix2D<Piece?>(BOARD_SIDE_LENGTH) {
            Array(BOARD_SIDE_LENGTH) { Pawn(Army.BLACK) }
        }
        
        val matrix = getMatrix2DFromString(sut)
        
        expected.forEachIndexed { rowIdx, arrayOfPieces ->
            assertEquals(arrayOfPieces.map { it?.toChar() }, matrix[rowIdx].map { it?.toChar() })
        }
    }

    //copy
    
    @Test
    fun `Board copy works as expected`() {
        val sut =   "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp"

        val board = Board(getMatrix2DFromString(sut))
        val stringBoard = board.toString()
        val boardCopy = board.copy()
        val stringCopy = boardCopy.toString()

        assertEquals(stringBoard, stringCopy)
        assertNotEquals(board, boardCopy)
    }
    
    //placePieceFromSpecialMoves

    @Test
    fun `placePieceFromSpecialMoves places king from long castle with rook correctly`(){
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

        val move = Move.getUnvalidatedMove("Ra1d1").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut.placePieceFromSpecialMoves(move, piece)
        
        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R K  BNR", sut.toString())
    }

    @Test
    fun `placePieceFromSpecialMoves places rook from long castle with king correctly`(){
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

        val move = Move.getUnvalidatedMove("Ke1c1").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut.placePieceFromSpecialMoves(move, piece)

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "   RKBNR", sut.toString())
    }

    @Test
    fun `placePieceFromSpecialMoves places king from short castle with rook correctly`(){
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

        val move = Move.getUnvalidatedMove("Kh1f1").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut.placePieceFromSpecialMoves(move, piece)

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQ  KR", sut.toString())
    }

    @Test
    fun `placePieceFromSpecialMoves places rook from short castle with king correctly`(){
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

        val move = Move.getUnvalidatedMove("Ke1g1").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut.placePieceFromSpecialMoves(move, piece)

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQKR  ", sut.toString())
    }
}

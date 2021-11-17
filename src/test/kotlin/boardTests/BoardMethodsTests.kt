package boardTests

import domain.*
import domain.pieces.*
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

    @Test
    fun `getPiece with valid position returns piece`() {
        assertTrue(sut.getPiece(Board.Position('e', 2)) is Pawn)
    }


    @Test
    fun `getPiece with valid position returns null`() {
        assertNull(sut.getPiece(Board.Position('e', 5)))
    }


    @Test
    fun `setPiece with valid piece and position works`() {
        val piece = Pawn(Army.WHITE)
        val position = Board.Position('e', 5)
        sut.setPiece(position, piece)

        assertEquals(piece, sut.getPiece(position))
    }


    @Test
    fun `setPiece with null and valid position works`() {
        val position = Board.Position('e', 5)
        sut.setPiece(position, null)

        assertNull(sut.getPiece(position))
    }

    @Test
    fun `removePiece with valid position works`() {
        val position = Board.Position('e', 5)
        sut.removePiece(position)

        assertNull(sut.getPiece(position))
    }


    @Test
    fun `isPositionOccupied with a piece in the specified position returns true`() {
        assertTrue(sut.isPositionOccupied(Board.Position('e', 2)))
    }

    @Test
    fun `isPositionOccupied with null in the specified position returns false`() {
        assertFalse(sut.isPositionOccupied(Board.Position('e', 5)))
    }


    @Test
    fun `Move with wrong pieceSymbol is invalid`() {
        assertFalse(sut.isValidMove("Ke2e3"))
    }


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
        val stringCopy = board.copy().toString()

        assertEquals(stringBoard, stringCopy)
    }

    @Test
    fun `Board iterator hasNext returns true on a empty Board`() {

        val sut =   "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        "

        val board = Board(getMatrix2DFromString(sut))
        assertEquals(board.iterator().hasNext(), true)
    }

    @Test
    fun `Board iterator next works as expected`() {

        val sut =   "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "        "

        val board = Board(getMatrix2DFromString(sut))
        assertEquals(board.iterator().next(), Board.Slot(null, Board.Position('a', 8)))
    }
}

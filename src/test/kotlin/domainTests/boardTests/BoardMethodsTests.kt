package domainTests.boardTests

import domain.board.*
import domain.board.Board.Position
import domain.move.Move
import domain.move.MoveType
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

    // getPiece

    @Test
    fun `getPiece on an occupied position returns piece`() {
        assertTrue(sut.getPiece(Position('e', 2)) is Pawn)
        assertTrue(sut.getPiece(Position('e', 1)) is King)
    }


    @Test
    fun `getPiece on an empty position returns null`() {
        assertNull(sut.getPiece(Position('e', 5)))
    }

    // placePiece

    @Test
    fun `placePiece places piece in the position`() {
        val piece = Pawn(Army.WHITE)
        val position = Position('e', 5)

        assertEquals(piece, sut.placePiece(position, piece).getPiece(position))
    }

    // removePiece

    @Test
    fun `removePiece removes piece from occupied position`() {
        val position = Position('e', 2)
        assertNull(sut.removePiece(position).getPiece(position))
    }

    @Test
    fun `removePiece removes piece from empty position`() {
        val position = Position('e', 5)
        assertNull(sut.removePiece(position).getPiece(position))
    }

    // isPositionOccupied

    @Test
    fun `isPositionOccupied with an occupied position returns true`() {
        assertTrue(sut.isPositionOccupied(Position('e', 2)))
    }

    @Test
    fun `isPositionOccupied with an empty position returns false`() {
        assertFalse(sut.isPositionOccupied(Position('e', 5)))
    }

    // copy

    @Test
    fun `Board copy creates a new (different) board with the same contents`() {
        val sut =
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp"

        val board = Board(getMatrix2DFromString(sut))
        val boardCopy = board.copy()

        assertEquals(board.toString(), boardCopy.toString())
        assertEquals(board, boardCopy)
        assertNotSame(board, boardCopy)
    }
    
    // equals and hashCode

    @Test
    fun `Boards with same pieces in the same positions are equal but not same`() {
        val boardInString =
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp" +
            "pppppppp"
        
        val board = Board(getMatrix2DFromString(boardInString))
        val board2 = Board(getMatrix2DFromString(boardInString))

        assertEquals(board, board2)
        assertEquals(board.hashCode(), board2.hashCode())
        assertNotSame(board, board2)
    }

    @Test
    fun `Boards with same pieces in the different positions aren't equal`() {
        val board = Board(
            getMatrix2DFromString(
                "pppppppp" +
                "ppp   pp" +
                "pppppppp" +
                "pp    pp" +
                "pppppppp" +
                "pppppppp" +
                "pppppppp" +
                "pppppppp"
            )
        )
        val board2 = Board(
            getMatrix2DFromString(
                "pppppppp" +
                "pppppppp" +
                "pppppppp" +
                "pppppppp" +
                "pp   ppp" +
                "pppppppp" +
                "pppppppp" +
                "p p   pp"
            )
        )

        assertNotEquals(board, board2)
        assertNotEquals(board.hashCode(), board2.hashCode())
        assertNotSame(board, board2)
    }
    

    // placePieceFromSpecialMoves

    @Test
    fun `placePieceFromSpecialMoves places rook from long castle with king correctly`() {
        var sut = Board(
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

        val move = Move("Ke1c1").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut = sut.placePieceFromSpecialMoves(move, piece)

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "   RKBNR", sut.toString()
        )
    }

    @Test
    fun `placePieceFromSpecialMoves places rook from short castle with king correctly`() {
        var sut = Board(
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

        val move = Move("Ke1g1").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut = sut.placePieceFromSpecialMoves(move, piece)

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQKR  ", sut.toString()
        )
    }

    @Test
    fun `placePieceFromSpecialMoves removes captured piece from en passant correctly`() {
        var sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    pP  " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQK  R"
            )
        )

        val move = Move("Pf5e6").copy(type = MoveType.EN_PASSANT)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)

        sut = sut.placePieceFromSpecialMoves(move, piece)

        assertEquals(
            "rnbqkbnr" +
            "pppp ppp" +
            "        " +
            "     P  " +
            "        " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R", sut.toString()
        )
    }

    // getMatrix2DFromString

    @Test
    fun `getMatrix2DFromString returns a Matrix containing the respective pieces`() {
        val sut =
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        
        val expected = (0 until BOARD_SIDE_LENGTH).map { row ->
            when (row) {
                0, 7 -> {
                    arrayOf<Piece?>(
                        Rook(if (row == 0) Army.BLACK else Army.WHITE),
                        Knight(if (row == 0) Army.BLACK else Army.WHITE),
                        Bishop(if (row == 0) Army.BLACK else Army.WHITE),
                        Queen(if (row == 0) Army.BLACK else Army.WHITE),
                        King(if (row == 0) Army.BLACK else Army.WHITE),
                        Bishop(if (row == 0) Army.BLACK else Army.WHITE),
                        Knight(if (row == 0) Army.BLACK else Army.WHITE),
                        Rook(if (row == 0) Army.BLACK else Army.WHITE),
                    )
                }
                1, 6 -> Array<Piece?>(BOARD_SIDE_LENGTH) { Pawn(if (row == 1) Army.BLACK else Army.WHITE) }
                else -> arrayOfNulls(BOARD_SIDE_LENGTH)
            }
        }.toTypedArray()

        val matrix = getMatrix2DFromString(sut)

        expected.forEachIndexed { rowIdx, arrayOfPieces ->
            assertEquals(arrayOfPieces.map { it?.toChar() }, matrix[rowIdx].map { it?.toChar() })
        }
    }
}

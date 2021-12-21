package domainTests.boardTests

import domain.board.*
import domain.board.Board.Position
import domain.move.Move
import domain.move.MoveType
import domain.pieces.*
import kotlin.test.*


class BoardMethodsTests { // [✔]
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

    // getPiece [✔]

    @Test
    fun `getPiece on an occupied position returns piece`() {
        assertTrue(sut.getPiece(Position('e', 2)) is Pawn)
        assertTrue(sut.getPiece(Position('e', 1)) is King)
    }

    @Test
    fun `getPiece on an empty position returns null`() {
        assertNull(sut.getPiece(Position('e', 5)))
    }

    // placePiece [✔]

    @Test
    fun `placePiece places piece in the position`() {
        val piece = Pawn(Army.WHITE)
        val position = Position('e', 5)

        assertEquals(piece, sut.placePiece(position, piece).getPiece(position))
    }

    // removePiece [✔]

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

    // isPositionOccupied [✔]

    @Test
    fun `isPositionOccupied with an occupied position returns true`() {
        assertTrue(sut.isPositionOccupied(Position('e', 2)))
    }

    @Test
    fun `isPositionOccupied with an empty position returns false`() {
        assertFalse(sut.isPositionOccupied(Position('e', 5)))
    }
    
    // makeMove [✔]

    @Test
    fun `makeMove makes normal move in board correctly`() {
        var sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        sut = sut.makeMove(Move("Pe2e4"))

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "    P   " +
            "        " +
            "PPPP PPP" +
            "RNBQKBNR",sut.toString()
        )
    }

    @Test
    fun `makeMove makes en passant move in board correctly`() {
        var sut = Board(
            "rnbqkbnr" +
            "ppppp pp" +
            "        " +
            "    Pp  " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        sut = sut.makeMove(Move("Pe5f6").copy(type = MoveType.EN_PASSANT))

        assertEquals(
            "rnbqkbnr" +
            "ppppp pp" +
            "     P  " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR",sut.toString()
        )
    }

    @Test
    fun `makeMove makes long castle move in board correctly`() {
        var sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        sut = sut.makeMove(Move("O-O-O")) // Default (unvalidated) castle row is first row

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "  KR BNR",sut.toString()
        )
    }

    @Test
    fun `makeMove makes short castle move in board correctly`() {
        var sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        sut = sut.makeMove(Move("O-O")) // Default (unvalidated) castle row is first row

        assertEquals(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQ RK ",sut.toString()
        )
    }

    @Test
    fun `makeMove makes promotion move in board correctly`() {
        var sut = Board(
            "rnbqk nr" +
            "pppppPpp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR"
        )

        sut = sut.makeMove(Move("Pf7f8=Q"))

        assertEquals(
            "rnbqkQnr" +
            "ppppp pp" +
            "        " +
            "        " +
            "        " +
            "        " +
            "PPPPPPPP" +
            "RNBQKBNR",sut.toString()
        )
    }

    // getPiecesFromString [✔]

    @Test
    fun `getPiecesFromString returns a List containing the respective pieces`() {
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
                    listOf<Piece?>(
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
                1, 6 -> List<Piece?>(BOARD_SIDE_LENGTH) { Pawn(if (row == 1) Army.BLACK else Army.WHITE) }
                else -> List(BOARD_SIDE_LENGTH) { null }
            }
        }.flatten()

        val matrix = getPiecesFromString(sut)

        assertEquals(expected, matrix)
    }

    // placePieceFromSpecialMoves [✔]

    @Test
    fun `placePieceFromSpecialMoves places rook from long castle correctly`() {
        var sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "R   KBNR"
        )

        val move = Move("O-O-O") // Default (unvalidated) castle row is first row
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
    fun `placePieceFromSpecialMoves places rook from short castle correctly`() {
        var sut = Board(
            "rnbqkbnr" +
            "pppppppp" +
            "        " +
            "        " +
            "     P  " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
        )

        val move = Move("O-O") // Default (unvalidated) castle row is first row
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
            "rnbqkbnr" +
            "pppp ppp" +
            "        " +
            "    pP  " +
            "        " +
            "        " +
            "PPPPP PP" +
            "RNBQK  R"
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

    // copy [✔]

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

        val board = Board(sut)
        val boardCopy = board.copy()

        assertEquals(board.toString(), boardCopy.toString())
        assertEquals(board, boardCopy)
        assertNotSame(board, boardCopy)
    }

    // equals and hashCode [✔]

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

        val board = Board(boardInString)
        val board2 = Board(boardInString)

        assertEquals(board, board2)
        assertEquals(board.hashCode(), board2.hashCode())
        assertNotSame(board, board2)
    }

    @Test
    fun `Boards with same pieces in the different positions aren't equal`() {
        val board = Board(
            "pppppppp" +
                    "ppp   pp" +
                    "pppppppp" +
                    "pp    pp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp"
        )
        val board2 = Board(
            "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "pp   ppp" +
                    "pppppppp" +
                    "pppppppp" +
                    "p p   pp"
        )

        assertNotEquals(board, board2)
        assertNotEquals(board.hashCode(), board2.hashCode())
        assertNotSame(board, board2)
    }
    
    // <T> List<T>.replace [✔]
    
    @Test
    fun `List replace returns a copy of the list with the element at the given index replaced by a new one`(){
        assertEquals(listOf("hey", "hee", "ho", "ha"), listOf("hey", "hoo", "ho", "ha").replace(1, "hee"))
    }
}

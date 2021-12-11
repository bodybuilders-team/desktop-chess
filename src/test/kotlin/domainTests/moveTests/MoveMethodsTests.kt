package domainTests.moveTests

import domain.Game
import domain.board.*
import domain.move.*
import kotlin.test.*


class MoveMethodsTests {

    // Move.invoke(string)

    @Test
    fun `Move constructor with string returns unvalidated move correctly`() {
        assertEquals(
            Move('P', Board.Position('e', 2), false, Board.Position('e', 4), null, MoveType.NORMAL),
            Move("Pe2e4")
        )
    }

    @Test
    fun `Move constructor with string with optional from position returns unvalidated move correctly`() {
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
    fun `Move constructor with string with supposedly invalid move returns unvalidated move correctly`() {
        assertEquals(
            Move('K', Board.Position('e', 1), false, Board.Position('g', 8), null, MoveType.NORMAL),
            Move("Ke1g8")
        )
    }

    // Move.validated()

    @Test
    fun `validated returns validated move correctly if move is valid`() {
        val sut = Board()

        assertEquals(
            Move("Pe2e4"),
            Move.validated("Pe2e4", Game(sut, emptyList()))
        )
    }

    @Test
    fun `validated throws if move isn't valid`() {
        val sut = Board()

        assertFailsWith<IllegalMoveException> {
            Move.validated("Pe2e5", Game(sut, emptyList()))
        }
    }

    @Test
    fun `validated throws if multiple valid moves are found`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "    P P " +
                "        " +
                "PPPP PPP" +
                "RNBQKBNR"
            )
        )

        assertFailsWith<IllegalMoveException> {
            Move.validated("Pf5", Game(sut, emptyList()))
        }
    }

    // Move.searchMoves()

    @Test
    fun `searchMoves returns a list containing the only valid move`() {
        val sut = Board()

        assertEquals(
            setOf(Move("Pe2e4")),
            Move.searchMoves(
                Move("Pe2e4"), optionalFromCol = false, optionalFromRow = false, optionalToPos = false,
                Game(sut, emptyList())
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromRow`() {
        val sut = Board()

        assertEquals(
            setOf(Move("Pe2e4")),
            Move.searchMoves(
                Move("Pee4"), optionalFromCol = false, optionalFromRow = true, optionalToPos = false,
                Game(sut, emptyList())
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromCol`() {
        val sut = Board()

        assertEquals(
            setOf(Move("Pe2e4")),
            Move.searchMoves(
                Move("P2e4"), optionalFromCol = true, optionalFromRow = false, optionalToPos = false,
                Game(sut, emptyList())
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromPos`() {
        val sut = Board()

        assertEquals(
            setOf(Move("Pe2e4")),
            Move.searchMoves(
                Move("Pe4"), optionalFromCol = true, optionalFromRow = true, optionalToPos = false,
                Game(sut, emptyList())
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the valid moves, if optional toPos`() {
        val sut = Board()

        assertEquals(
            setOf(Move("Pe2e3"), Move("Pe2e4")),
            Move.searchMoves(
                Move("Pe2e4"), optionalFromCol = false, optionalFromRow = false, optionalToPos = true,
                Game(sut, emptyList())
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns empty list, if no valid moves`() {
        val sut = Board()

        assertEquals(
            emptyList(),
            Move.searchMoves(
                Move("Pe2e5"), optionalFromCol = false, optionalFromRow = false, optionalToPos = false,
                Game(sut, emptyList())
            )
        )
    }

    @Test
    fun `searchMoves returns a list containing all available moves of the piece type, if optional fromPos and toPos`() {
        val sut = Board()

        assertEquals(
            (COLS_RANGE.map { Move("P${it}2${it}3") } + COLS_RANGE.map { Move("P${it}2${it}4") } +
                    COLS_RANGE.map { Move("P${it}7${it}6") } + COLS_RANGE.map { Move("P${it}7${it}5") }
                    ).toSet(),
            Move.searchMoves(
                Move("Pe2e4"), optionalFromCol = true, optionalFromRow = true, optionalToPos = true,
                Game(sut, emptyList())
            ).toSet())
    }

    // extractMoveInfo

    @Test
    fun `extractMoveInfo extracts information from move correctly`() {
        assertEquals(
            Move.Companion.MoveExtraction(
                Move(
                    symbol = 'P',
                    from = Board.Position('e', 2),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = false,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("Pe2e4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with optional fromCol correctly`() {
        assertEquals(
            Move.Companion.MoveExtraction(
                Move(
                    symbol = 'P',
                    from = Board.Position(FIRST_COL, 2),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = true,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("P2e4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with optional fromRow correctly`() {
        assertEquals(
            Move.Companion.MoveExtraction(
                Move(
                    symbol = 'P',
                    from = Board.Position('e', FIRST_ROW),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = false,
                optionalFromRow = true
            ),
            Move.extractMoveInfo("Pee4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with optional fromPos correctly`() {
        assertEquals(
            Move.Companion.MoveExtraction(
                Move(
                    symbol = 'P',
                    from = Board.Position(FIRST_COL, FIRST_ROW),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = true,
                optionalFromRow = true
            ),
            Move.extractMoveInfo("Pe4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with promotion correctly (regardless of move validity)`() {
        assertEquals(
            Move.Companion.MoveExtraction(
                Move(
                    symbol = 'P',
                    from = Board.Position('e', 2),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = 'Q',
                    type = MoveType.NORMAL
                ),
                optionalFromCol = false,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("Pe2e4=Q")
        )
    }

    // Directions

    @Test
    fun `Move to same place isn't vertical, horizontal, straight nor diagonal`() {
        assertFalse(Move("e2e2").isVertical())
        assertFalse(Move("e2e2").isHorizontal())
        assertFalse(Move("e2e2").isStraight())
        assertFalse(Move("e2e2").isDiagonal())
    }

    // Vertical

    @Test
    fun `isVertical with vertical move works`() {
        assertTrue(Move("e2e4").isVertical())
    }

    @Test
    fun `isVertical with horizontal move doesn't work`() {
        assertFalse(Move("e2f2").isVertical())
    }

    // Horizontal

    @Test
    fun `isHorizontal with horizontal move works`() {
        assertTrue(Move("e2f2").isHorizontal())
    }

    @Test
    fun `isHorizontal with vertical move doesn't work`() {
        assertFalse(Move("e2e4").isHorizontal())
    }

    // Straight

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

    // Diagonal

    @Test
    fun `isDiagonal with diagonal move works`() {
        assertTrue(Move("e2g4").isDiagonal())
    }

    @Test
    fun `isDiagonal with straight move doesn't work`() {
        assertFalse(Move("e2f4").isDiagonal())
    }

    // Distances

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

    // toString

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

    // isValidCapture

    @Test
    fun `isValidCapture returns true with valid capture to empty square`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Ng1f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns true with valid capture to occupied square`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    p   " +
                "        " +
                "     N  " +
                "PPPPPPPP" +
                "RNBQKB R"
            )
        )

        val move = Move("Nf3e5")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with invalid capture`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "        " +
                "     N  " +
                "PPPPPPPP" +
                "RNBQKB R"
            )
        )

        val move = Move("Pf2f3")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with valid capture to empty square with wrong capture information`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "ppppp pp" +
                "        " +
                "     p  " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pe2e4").copy(capture = true)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns true with valid promotion`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkb r" +
                "ppppppPp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pg7g8=Q")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertTrue(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture returns false with invalid promotion (invalid pawn position)`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkb r" +
                "pppppp p" +
                "        " +
                "      P " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pg5g6=Q")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    @Test
    fun `isValidCapture return false with invalid promotion (promotion piece not specified)`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkb r" +
                "ppppppPp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pg7g8")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertFalse(move.isValidCapture(piece, sut))
    }

    // getValidatedMove

    @Test
    fun `getValidatedMove returns validated move with the correct move type information`() {
        val sut = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "pppppppp" +
                "        " +
                "        " +
                "        " +
                "        " +
                "PPPPPPPP" +
                "RNBQKBNR"
            )
        )

        val move = Move("Pe2e4").copy(type = MoveType.CASTLE)
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(move.copy(type = MoveType.NORMAL), move.getValidatedMove(piece, Game(sut, emptyList())))
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
                "RNBQKBNR"
            )
        )

        val move = Move("Pf3e4")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertEquals(move.copy(capture = true), move.getValidatedMove(piece, Game(sut, emptyList())))
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
                "RNBQKBNR"
            )
        )

        val move = Move("Pe2e6")
        val piece = sut.getPiece(move.from)

        assertNotNull(piece)
        assertNull(move.getValidatedMove(piece, Game(sut, emptyList())))
    }
}

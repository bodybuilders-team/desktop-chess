package domainTests.moveTests

import domain.board.Board
import domain.board.FIRST_COL
import domain.board.FIRST_ROW
import domain.move.*
import domain.pieces.PieceType
import kotlin.test.*


class MoveExtractionTests {

    // extractMoveInfo

    @Test
    fun `extractMoveInfo extracts information from move correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
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
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
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
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
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
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
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
    fun `extractMoveInfo extracts information from move with optional piece symbol and fromCol correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
                    from = Board.Position(FIRST_COL, 2),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = true,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("2e4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with optional piece symbol and fromRow correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
                    from = Board.Position('e', FIRST_ROW),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = false,
                optionalFromRow = true
            ),
            Move.extractMoveInfo("ee4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with optional piece symbol and fromPos correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
                    from = Board.Position(FIRST_COL, FIRST_ROW),
                    capture = false,
                    to = Board.Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = true,
                optionalFromRow = true
            ),
            Move.extractMoveInfo("e4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with promotion correctly (regardless of move validity)`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
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

    @Test
    fun `extractMoveInfo extracts information from short castle move correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.KING.symbol,
                    from = Board.Position(INITIAL_KING_COL, DEFAULT_CASTLE_TO_ROW),
                    capture = false,
                    to = Board.Position(SHORT_CASTLE_KING_COL, DEFAULT_CASTLE_TO_ROW),
                    promotion = null,
                    type = MoveType.CASTLE
                ),
                optionalFromCol = false,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("O-O")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from long castle move correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.KING.symbol,
                    from = Board.Position(INITIAL_KING_COL, DEFAULT_CASTLE_TO_ROW),
                    capture = false,
                    to = Board.Position(LONG_CASTLE_KING_COL, DEFAULT_CASTLE_TO_ROW),
                    promotion = null,
                    type = MoveType.CASTLE
                ),
                optionalFromCol = false,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("O-O-O")
        )
    }
}
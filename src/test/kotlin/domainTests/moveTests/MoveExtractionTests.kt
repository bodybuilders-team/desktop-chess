package domainTests.moveTests

import domain.board.Board.Position
import domain.board.FIRST_COL
import domain.board.FIRST_ROW
import domain.move.DEFAULT_CASTLE_TO_ROW
import domain.move.INITIAL_KING_COL
import domain.move.IllegalMoveException
import domain.move.LONG_CASTLE_KING_COL
import domain.move.Move
import domain.move.MoveExtraction
import domain.move.MoveType
import domain.move.SHORT_CASTLE_KING_COL
import domain.move.extractMoveInfo
import domain.pieces.PieceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MoveExtractionTests { // [✔]

    // toString [✔]

    @Test
    fun `toString works for normal move`() {
        assertEquals("Pe2e4", Move.extractMoveInfo("Pe2e4").toString())
    }

    @Test
    fun `toString works for move with optional from col`() {
        assertEquals("P2e4", Move.extractMoveInfo("P2e4").toString())
    }

    @Test
    fun `toString works for move with optional from row`() {
        assertEquals("Pee4", Move.extractMoveInfo("Pee4").toString())
    }

    @Test
    fun `toString works for move with optional from position`() {
        assertEquals("Pe4", Move.extractMoveInfo("Pe4").toString())
    }

    @Test
    fun `toString works for capture move`() {
        assertEquals("Pe2xe4", Move.extractMoveInfo("Pe2xe4").toString())
    }

    @Test
    fun `toString works for promotion move`() {
        assertEquals("Pe7e8=Q", Move.extractMoveInfo("Pe7e8=Q").toString())
    }

    @Test
    fun `toString works for short castle`() {
        assertEquals("O-O", Move.extractMoveInfo("O-O").toString())
    }

    @Test
    fun `toString works for long castle`() {
        assertEquals("O-O-O", Move.extractMoveInfo("O-O-O").toString())
    }

    // extractMoveInfo [✔]

    @Test
    fun `extractMoveInfo throws IllegalMoveException with wrongly formatted move`() {
        assertFailsWith<IllegalMoveException> {
            Move.extractMoveInfo("P29222eee")
        }
    }

    @Test
    fun `extractMoveInfo extracts information from normal move correctly`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
                    from = Position('e', 2),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position(FIRST_COL, 2),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position('e', FIRST_ROW),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position(FIRST_COL, FIRST_ROW),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position(FIRST_COL, 2),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position('e', FIRST_ROW),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position(FIRST_COL, FIRST_ROW),
                    capture = false,
                    to = Position('e', 4),
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
    fun `extractMoveInfo extracts information from move with capture correctly (regardless of move validity)`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
                    from = Position('e', 2),
                    capture = true,
                    to = Position('e', 4),
                    promotion = null,
                    type = MoveType.NORMAL
                ),
                optionalFromCol = false,
                optionalFromRow = false
            ),
            Move.extractMoveInfo("Pe2xe4")
        )
    }

    @Test
    fun `extractMoveInfo extracts information from move with promotion correctly (regardless of move validity)`() {
        assertEquals(
            MoveExtraction(
                Move(
                    symbol = PieceType.PAWN.symbol,
                    from = Position('e', 2),
                    capture = false,
                    to = Position('e', 4),
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
                    from = Position(INITIAL_KING_COL, DEFAULT_CASTLE_TO_ROW),
                    capture = false,
                    to = Position(SHORT_CASTLE_KING_COL, DEFAULT_CASTLE_TO_ROW),
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
                    from = Position(INITIAL_KING_COL, DEFAULT_CASTLE_TO_ROW),
                    capture = false,
                    to = Position(LONG_CASTLE_KING_COL, DEFAULT_CASTLE_TO_ROW),
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

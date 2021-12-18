package domainTests.moveTests

import domain.board.Board
import domain.board.FIRST_COL
import domain.board.FIRST_ROW
import domain.move.Move
import domain.move.MoveExtraction
import domain.move.MoveType
import domain.move.extractMoveInfo
import kotlin.test.*


class MoveExtractionTests {

    // extractMoveInfo

    @Test
    fun `extractMoveInfo extracts information from move correctly`() {
        assertEquals(
            MoveExtraction(
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
            MoveExtraction(
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
            MoveExtraction(
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
            MoveExtraction(
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
            MoveExtraction(
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
}
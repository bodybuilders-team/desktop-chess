package domainTests.gameTests

import domain.board.Board
import domain.board.COLS_RANGE
import domain.game.*
import domain.move.*
import kotlin.test.*


class SearchMovesTests { // [✔]

    // searchMoves [✔]

    @Test
    fun `searchMoves returns a list containing the only valid move`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            listOfMoves("Pe2e4").toSet(),
            game.searchMoves(Move.extractMoveInfo("Pe2e4"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromRow`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            listOfMoves("Pe2e4").toSet(),
            game.searchMoves(Move.extractMoveInfo("Pee4"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromCol`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            listOfMoves("Pe2e4").toSet(),
            game.searchMoves(Move.extractMoveInfo("P2e4"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromPos`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            listOfMoves("Pe2e4").toSet(),
            game.searchMoves(Move.extractMoveInfo("Pe4"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the valid moves, if optional toPos`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            listOfMoves("Pe2e3", "Pe2e4").toSet(),
            game.searchMoves(Move.extractMoveInfo("Pe2e4"), optionalToPos = true).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move if the move is a long castle and only long castle is possible`() {
        val game = Game(
            Board(
                "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "R   K   "
            ),
            emptyList()
        )

        assertEquals(
            listOfMoves("O-O-O").toSet(),
            game.searchMoves(Move.extractMoveInfo("O-O-O"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move if the move is a short castle and only short castle is possible`() {
        val game = Game(
            Board(
                "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "    K  R"
            ),
            emptyList()
        )

        assertEquals(
            listOfMoves("O-O").toSet(),
            game.searchMoves(Move.extractMoveInfo("O-O"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns empty list if the move is a short castle and only long castle is possible`() {
        val game = Game(
            Board(
                "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "R   K   "
            ),
            emptyList()
        )

        assertEquals(
            emptyList(),
            game.searchMoves(Move.extractMoveInfo("O-O"), optionalToPos = false)
        )
    }

    @Test
    fun `searchMoves returns empty list if the move is a long castle and only short castle is possible`() {
        val game = Game(
            Board(
                "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "    K  R"
            ),
            emptyList()
        )

        assertEquals(
            listOf(),
            game.searchMoves(Move.extractMoveInfo("O-O-O"), optionalToPos = false)
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move if the move is a long castle and both short and long castles are possible`() {
        val game = Game(
            Board(
                "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "R   K  R"
            ),
            emptyList()
        )

        assertEquals(
            listOfMoves("O-O-O").toSet(),
            game.searchMoves(Move.extractMoveInfo("O-O-O"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move if the move is a short castle and both short and long castles are possible`() {
        val game = Game(
            Board(
                "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "        " +
                        "R   K  R"
            ),
            emptyList()
        )

        assertEquals(
            listOfMoves("O-O").toSet(),
            game.searchMoves(Move.extractMoveInfo("O-O"), optionalToPos = false).toSet()
        )
    }

    @Test
    fun `searchMoves returns empty list, if no valid moves`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            emptyList(),
            game.searchMoves(Move.extractMoveInfo("Pe2e5"), optionalToPos = false)
        )
    }

    @Test
    fun `searchMoves returns a list containing all available moves of the piece type of the army, if optional fromPos and toPos`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            (COLS_RANGE.map { Move("P${it}2${it}3") } + COLS_RANGE.map { Move("P${it}2${it}4") }).toSet(),
            game.searchMoves(Move.extractMoveInfo("Pe4"), optionalToPos = true).toSet())
    }
}
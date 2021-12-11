package domainTests

import domain.board.*
import domain.*
import domain.move.IllegalMoveException
import domain.move.Move
import domain.pieces.Army
import kotlin.test.*

class GameTests {
    
    // makeMove

    @Test
    fun `MakeMove in Game`() {
        var game = Game(Board(), emptyList())

        val movesInString = listOf("Pe2e4", "Pe7e5", "Nb1c3")

        movesInString.forEach { moveInString ->
            game = game.makeMove(Move.validated(moveInString, game))
        }

        assertEquals(
            "rnbqkbnr" +
                    "pppp ppp" +
                    "        " +
                    "    p   " +
                    "    P   " +
                    "  N     " +
                    "PPPP PPP" +
                    "R BQKBNR", game.board.toString()
        )
    }

    @Test
    fun `Promote pawn to queen`() {
        var game = Game(Board(), emptyList())

        val movesInString = listOf("Pf2f4", "Pg7g5", "Pf4xg5", "Pb7b5", "Pg5g6", "Pc7c5", "Pg6g7", "Ng8f6", "Pg7g8=Q")

        movesInString.forEach { moveInString ->
            game = game.makeMove(Move.validated(moveInString, game))
        }

        assertEquals(
            "rnbqkbQr" +
                    "p  ppp p" +
                    "     n  " +
                    " pp     " +
                    "        " +
                    "        " +
                    "PPPPP PP" +
                    "RNBQKBNR", game.board.toString()
        )
    }

    @Test
    fun `Removing piece previously protecting king leaves king in a check and throws IllegalMoveException`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                        "    q   " +
                        "        " +
                        "    B   " +
                        "        " +
                        "    K   " +
                        "        " +
                        "        "
            )
        )

        val game = Game(sut, emptyList())

        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("Be5d6", game))
        }
    }

    @Test
    fun `King moving into check throws`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                        "    q   " +
                        "        " +
                        "        " +
                        "        " +
                        "     K  " +
                        "        " +
                        "        "
            )
        )

        val game = Game(sut, emptyList())

        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("Kf3e3", game))
        }
    }

    @Test
    fun `A king cannot castle to get out of check`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                        "        " +
                        "        " +
                        "    q   " +
                        "        " +
                        "        " +
                        "        " +
                        "R   K   "
            )
        )

        val game = Game(sut, emptyList())

        val army = Army.WHITE
        assertTrue(game.board.isKingInCheck(army))

        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("Ke1c1", game)).board.isKingInCheck(army)
        }
    }
    
    // searchMoves

    @Test
    fun `searchMoves returns a list containing the only valid move`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            setOf(Move("Pe2e4")),
            game.searchMoves(
                Move("Pe2e4"), optionalFromCol = false, optionalFromRow = false, optionalToPos = false
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromRow`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            setOf(Move("Pe2e4")),
            game.searchMoves(
                Move("Pee4"), optionalFromCol = false, optionalFromRow = true, optionalToPos = false
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromCol`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            setOf(Move("Pe2e4")),
            game.searchMoves(
                Move("P2e4"), optionalFromCol = true, optionalFromRow = false, optionalToPos = false
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the only valid move, if optional fromPos`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            setOf(Move("Pe2e4")),
            game.searchMoves(
                Move("Pe4"), optionalFromCol = true, optionalFromRow = true, optionalToPos = false
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns a list containing the valid moves, if optional toPos`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            setOf(Move("Pe2e3"), Move("Pe2e4")),
            game.searchMoves(
                Move("Pe2e4"), optionalFromCol = false, optionalFromRow = false, optionalToPos = true
            ).toSet()
        )
    }

    @Test
    fun `searchMoves returns empty list, if no valid moves`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            emptyList(),
            game.searchMoves(
                Move("Pe2e5"), optionalFromCol = false, optionalFromRow = false, optionalToPos = false
            )
        )
    }

    @Test
    fun `searchMoves returns a list containing all available moves of the piece type of the army, if optional fromPos and toPos`() {
        val game = Game(Board(), emptyList())

        assertEquals(
            (COLS_RANGE.map { Move("P${it}2${it}3") } + COLS_RANGE.map { Move("P${it}2${it}4") }).toSet(),
            game.searchMoves(
                Move("Pe2e4"), optionalFromCol = true, optionalFromRow = true, optionalToPos = true
            ).toSet())
    }
    
    // gameFromMoves
    
    @Test
    fun `gameFromMoves return game with no moves made if no moves are passed`() {
        assertEquals(Board().toString(), gameFromMoves().board.toString())
    }

    @Test
    fun `gameFromMoves returns new game with the board with moves made`() {
        val board = Board(
            getMatrix2DFromString(
                "rnbqkbnr" +
                "p pppppp" +
                "        " +
                " p      " +
                "    P   " +
                "  N     " +
                "PPPP PPP" +
                "R BQKBNR"
            )
        )

        assertEquals(board.toString(), gameFromMoves("Pe2e4", "Pb7b5", "Nb1c3").board.toString())
    }

    @Test
    fun `gameFromMoves returns new game with moves validated`() {
        assertEquals(listOf("Pe2e4", "Pb7b5", "Nb1c3").map { Move(it) }, gameFromMoves("Pe4", "Pb5", "Nc3").moves)
    }

    @Test
    fun `gameFromMoves throws IllegalMoveException if at least one of the moves is invalid`() {
        assertFailsWith<IllegalMoveException>{
            gameFromMoves("Pe4", "Pb5", "Ne3")
        }
    }

    @Test
    fun `gameFromMoves throws IllegalMoveException if multiple moves are invalid`() {
        assertFailsWith<IllegalMoveException>{
            gameFromMoves("Pe4", "Pa2a5", "Ne3", "Ke9")
        }
    }
    
    // gameFromMoves(List<Move>)

    @Test
    fun `gameFromMoves(List-Move-) returns same game as gameFromMoves with list of movesInString`() {
        val gameFromMovesInString = gameFromMoves("Pe2e4", "Pe7e5", "Pa2a4", "Pg7g5")
        val gameFromMoves = gameFromMoves(listOf("Pe2e4", "Pe7e5", "Pa2a4", "Pg7g5").map { Move(it) })
        
        assertEquals(gameFromMovesInString.board, gameFromMoves.board)
        assertEquals(gameFromMovesInString.moves, gameFromMoves.moves)
    }
}
package domainTests.gameTests

import defaultGameResultingInBlackCheck
import defaultGameResultingInCheckMate
import defaultGameResultingInStaleMate
import defaultGameResultingInWhiteCheck
import domain.board.Board
import domain.game.Game
import domain.game.GameState
import domain.game.armyToPlay
import domain.game.gameFromMoves
import domain.game.isKingInCheck
import domain.game.makeMove
import domain.game.makeMoves
import domain.game.state
import domain.move.IllegalMoveException
import domain.move.Move
import domain.move.MoveType
import domain.pieces.Army
import listOfMoves
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GameTests { // [✔]

    // makeMove [✔]

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
                "R BQKBNR",
            game.board.toString()
        )
        assertEquals(listOfMoves("Pe2e4", "Pe7e5", "Nb1c3"), game.moves)
    }

    @Test
    fun `makeMove in the game with a capture move works correctly`() {
        var game = Game(
            Board(
                "rnbqkbnr" +
                    "pppp ppp" +
                    "        " +
                    "        " +
                    "    p   " +
                    "     P  " +
                    "PPPPP PP" +
                    "RNBQKBNR"
            ),
            emptyList()
        )

        game = game.makeMove(Move.validated("Pf3e4", game))

        assertEquals(
            "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "        " +
                "    P   " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR",
            game.board.toString()
        )
        assertEquals(listOf(Move("Pf3xe4")), game.moves)
    }

    @Test
    fun `makeMove in the game with a long castle move works correctly`() {
        var game = Game(
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

        game = game.makeMove(Move.validated("O-O-O", game))

        assertEquals(
            "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "  KR   R",
            game.board.toString()
        )
        assertEquals(listOf(Move("O-O-O")), game.moves)
    }

    @Test
    fun `makeMove in the game with a short castle move works correctly`() {
        var game = Game(
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

        game = game.makeMove(Move.validated("O-O", game))

        assertEquals(
            "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "        " +
                "R    RK ",
            game.board.toString()
        )
        assertEquals(listOf(Move("O-O")), game.moves)
    }

    @Test
    fun `makeMove in the game with an en passant move works correctly`() {
        var game = Game(
            Board(
                "rnbqkbnr" +
                    " ppp ppp" +
                    "        " +
                    "p   pP  " +
                    "        " +
                    "        " +
                    "PPPPP PP" +
                    "RNBQKBNR"
            ),
            listOfMoves("Pf2f4", "Pa7a5", "Pf4f5", "Pe7e5")
        )

        game = game.makeMove(Move.validated("Pf5e6", game))

        assertEquals(
            "rnbqkbnr" +
                " ppp ppp" +
                "    P   " +
                "p       " +
                "        " +
                "        " +
                "PPPPP PP" +
                "RNBQKBNR",
            game.board.toString()
        )
        assertEquals(
            listOfMoves("Pf2f4", "Pa7a5", "Pf4f5", "Pe7e5") + Move("Pf5xe6").copy(type = MoveType.EN_PASSANT),
            game.moves
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
                "RNBQKBNR",
            game.board.toString()
        )
    }

    @Test
    fun `Removing piece previously protecting king leaves king in a check and throws IllegalMoveException`() {
        val sut = Board(
            "        " +
                "    q   " +
                "        " +
                "    B   " +
                "        " +
                "    K   " +
                "        " +
                "        "
        )

        val game = Game(sut, emptyList())

        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("Be5d6", game))
        }
    }

    @Test
    fun `King moving into check throws`() {
        val sut = Board(
            "        " +
                "    q   " +
                "        " +
                "        " +
                "        " +
                "     K  " +
                "        " +
                "        "
        )

        val game = Game(sut, emptyList())

        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("Kf3e3", game))
        }
    }

    @Test
    fun `A king cannot castle to get out of check`() {
        val sut = Board(
            "        " +
                "        " +
                "        " +
                "    q   " +
                "        " +
                "        " +
                "        " +
                "R   K  R"
        )

        val game = Game(sut, emptyList())

        val army = Army.WHITE
        assertTrue(game.board.isKingInCheck(army))

        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("O-O", game)).board.isKingInCheck(army)
        }
        assertFailsWith<IllegalMoveException> {
            game.makeMove(Move.validated("O-O-O", game)).board.isKingInCheck(army)
        }
    }

    // armyToPlay [✔]

    @Test
    fun `armyToPlay returns White when number of moves is even`() {
        assertEquals(Army.WHITE, gameFromMoves().armyToPlay)
    }

    @Test
    fun `armyToPlay returns Black when number of moves is odd`() {
        assertEquals(Army.BLACK, gameFromMoves("Pe2e4").armyToPlay)
    }

    // state [✔]

    @Test
    fun `Game state is NO_CHECK when there's no check, checkmate, stalemate or tie in game`() {
        assertEquals(GameState.NO_CHECK, gameFromMoves("Pe2e4").state)
    }

    @Test
    fun `Game state is CHECK when there's a check in game`() {
        assertEquals(GameState.CHECK, defaultGameResultingInBlackCheck.state)
        assertEquals(GameState.CHECK, defaultGameResultingInWhiteCheck.state)
    }

    @Test
    fun `Game state is CHECKMATE when there's a checkmate in game`() {
        assertEquals(GameState.CHECKMATE, defaultGameResultingInCheckMate.state)
    }

    @Test
    fun `Game state is STALEMATE when there's a stalemate in game`() {
        assertEquals(GameState.STALEMATE, defaultGameResultingInStaleMate.state)
    }

    // makeMoves [✔]
    // Uses makeMove. Guarantee its testing.

    @Test
    fun `makeMoves makes moves correctly`() {
        var game = Game(Board(), emptyList())

        val movesInString = listOf("Pe2e4", "Pe7e5", "Nb1c3")

        game = game.makeMoves(movesInString)

        assertEquals(
            "rnbqkbnr" +
                "pppp ppp" +
                "        " +
                "    p   " +
                "    P   " +
                "  N     " +
                "PPPP PPP" +
                "R BQKBNR",
            game.board.toString()
        )
        assertEquals(listOfMoves("Pe2e4", "Pe7e5", "Nb1c3"), game.moves)
    }

    // gameFromMoves(vararg String) [✔]

    @Test
    fun `gameFromMoves return game with no moves made if no moves are passed`() {
        assertEquals(Board().toString(), gameFromMoves().board.toString())
    }

    @Test
    fun `gameFromMoves returns new game with the board with moves made`() {
        val board = Board(
            "rnbqkbnr" +
                "p pppppp" +
                "        " +
                " p      " +
                "    P   " +
                "  N     " +
                "PPPP PPP" +
                "R BQKBNR"
        )

        assertEquals(board.toString(), gameFromMoves("Pe2e4", "Pb7b5", "Nb1c3").board.toString())
    }

    @Test
    fun `gameFromMoves returns new game with moves validated`() {
        assertEquals(listOfMoves("Pe2e4", "Pb7b5", "Nb1c3"), gameFromMoves("Pe4", "Pb5", "Nc3").moves)
    }

    @Test
    fun `gameFromMoves throws IllegalMoveException if at least one of the moves is invalid`() {
        assertFailsWith<IllegalMoveException> {
            gameFromMoves("Pe4", "Pb5", "Ne3")
        }
    }

    @Test
    fun `gameFromMoves throws IllegalMoveException if multiple moves are invalid`() {
        assertFailsWith<IllegalMoveException> {
            gameFromMoves("Pe4", "Pa2a5", "Ne3", "Ke9")
        }
    }

    // gameFromMoves(List<Move>) [✔]

    @Test
    fun `gameFromMoves(List-Move-) returns same game as gameFromMoves with list of movesInString`() {
        val gameFromMovesInString = gameFromMoves("Pe2e4", "Pe7e5", "Pa2a4", "Pg7g5")
        val gameFromMoves = gameFromMoves(listOfMoves("Pe2e4", "Pe7e5", "Pa2a4", "Pg7g5"))

        assertEquals(gameFromMovesInString.board, gameFromMoves.board)
        assertEquals(gameFromMovesInString.moves, gameFromMoves.moves)
    }
}

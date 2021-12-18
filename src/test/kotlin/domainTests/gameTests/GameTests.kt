package domainTests.gameTests

import domain.board.*
import domain.game.*
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

    // currentTurnArmy

    @Test
    fun `currentTurnArmy returns White when number of moves is even`() {
        assertEquals(Army.WHITE, gameFromMoves().currentTurnArmy)
    }

    @Test
    fun `currentTurnArmy returns Black when number of moves is odd`() {
        assertEquals(Army.BLACK, gameFromMoves("Pe2e4").currentTurnArmy)
    }

    // isWhiteTurn

    @Test
    fun `isWhiteTurn returns true when number of moves is even`() {
        assertTrue(gameFromMoves().isWhiteTurn())
    }

    @Test
    fun `isWhiteTurn returns false when number of moves is odd`() {
        assertFalse(gameFromMoves("Pe2e4").isWhiteTurn())
    }
    
    //getState
    
    //TODO("getState tests")

    // gameFromMoves

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

    // isTiedByFiftyMoveRule

    @Test
    fun `Game is tied by 50 move rule if no pawn move and no capture happens in the last 100 moves (50 by each side)`() {
        val moves = List(25) { listOf("Nb1c3", "Nb8c6", "Nc3b1", "Nc6b8") }.flatten()
        assertTrue(gameFromMoves(moves).isTiedByFiftyMoveRule())
    }

    @Test
    fun `Game is not tied by 50 move rule if a pawn moved and no capture happens in the last 100 moves (50 by each side)`() {
        val moves = List(25) { listOf("Nb1c3", "Nb8c6", "Nc3b1", "Nc6b8") }.flatten()
        assertFalse(gameFromMoves(moves + "Pe4").isTiedByFiftyMoveRule())
    }

    @Test
    fun `Game is not tied by 50 move rule if no pawn moved and a capture happens in the last 100 moves (50 by each side)`() {
        val startingMoves = listOf("Nb1c3", "Nb8c6", "Nc3b5", "Nc6d4", "Nb5xd4")

        val moves = List(24) { listOf("Ng8h6", "Nd4b5", "Nh6g8", "Nb5d4") }.flatten()
        assertFalse(gameFromMoves(startingMoves + moves).isTiedByFiftyMoveRule())
    }

    @Test
    fun `Game is not tied by 50 move rule if a pawn moved and a capture happens in the last 100 moves (50 by each side)`() {
        val startingMoves = listOf("e4", "d5", "d5", "h6")

        val moves = List(24) { listOf("Nb1c3", "Nb8c6", "Nc3b1", "Nc6b8") }.flatten()
        assertFalse(gameFromMoves(startingMoves + moves).isTiedByFiftyMoveRule())
    }
}
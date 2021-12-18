package domainTests.commandTests

import GameStateStub
import domain.*
import domain.game.*
import domain.commands.*
import domain.move.*
import kotlin.test.*

class OpenCommandTests {
    @Test
    fun `Open command throws CommandException if the game name is missing`() {
        val db = GameStateStub()

        assertEquals(
            "Missing game name.",
            assertFailsWith<CommandException> {
                OpenCommand(db).execute(null)
            }.message
        )
    }
    
    @Test
    fun `Open command creates game if it doesn't exist`() {
        val gameName = "test"
        
        val db = GameStateStub()
        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(listOf(), session.game.moves)
    }

    @Test
    fun `Open command opens an existing game if it exists`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val move = Move("Pe2e4")
        db.postMove(gameName, move)

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(listOf(move), session.game.moves)
    }

    @Test
    fun `Open command session state is ENDED if there's a checkmate in board of already existing game`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        // Fool's mate! 🤡 - https://www.chess.com/article/view/fastest-chess-checkmates
        val movesForFoolsMate = gameFromMoves("f3", "e5", "g4", "Qh4").moves

        movesForFoolsMate.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForFoolsMate, session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
    }

    @Test
    fun `Open command session state is ENDED if there's a stalemate in board of already existing game`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        // Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
        val movesForFastestStalemate =
            gameFromMoves(
                "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
                "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
            ).moves

        movesForFastestStalemate.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForFastestStalemate, session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
    }

    @Test
    fun `Open command session state is YOUR_TURN if it's white turn`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val moves = gameFromMoves("e3", "e6").moves

        moves.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(moves, session.game.moves)
        assertEquals(SessionState.YOUR_TURN, session.state)
        assertEquals(GameState.NO_CHECK, session.gameState)
    }

    @Test
    fun `Open command session state is WAITING_FOR_OPPONENT if it's black turn`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val moves = gameFromMoves("e3").moves

        moves.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(moves, session.game.moves)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, session.state)
        assertEquals(GameState.NO_CHECK, session.gameState)
    }

    @Test
    fun `Open command gameState is CHECK if the white King is in Check and it's white turn`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val movesForCheck = gameFromMoves("f3", "e5", "g4", "b6", "e3", "Qh4").moves

        movesForCheck.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForCheck, session.game.moves)
        assertEquals(SessionState.YOUR_TURN, session.state)
        assertEquals(GameState.CHECK, session.gameState)
    }

    @Test
    fun `Open command gameState is NO_CHECK if the white King isn't in Check and it's white turn`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val movesForCheck = gameFromMoves("c3", "d6").moves

        movesForCheck.forEach { db.postMove(gameName, it) }

        val result = OpenCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForCheck, session.game.moves)
        assertEquals(SessionState.YOUR_TURN, session.state)
        assertEquals(GameState.NO_CHECK, session.gameState)
    }
}
package domainTests.commandTests

import storage.GameStorageStub
import defaultGameResultingInCheckMate
import defaultGameResultingInStaleMate
import defaultGameResultingInWhiteCheck
import domain.*
import domain.game.*
import domain.commands.*
import domain.move.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class OpenCommandTests { // [✔]
    @Test
    fun `Open command throws CommandException if the game name is missing`() {
        runBlocking {
            val db = GameStorageStub()

            assertEquals(
                "Missing game name.",
                assertFailsWith<CommandException> {
                    OpenCommand(db).execute(null)
                }.message
            )
        }
    }
    
    @Test
    fun `Open command creates game if it doesn't exist`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(listOf(), session.game.moves)
        }
    }

    @Test
    fun `Open command opens an existing game if it exists`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val move = Move("Pe2e4")
            db.postMove(gameName, move)

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(listOf(move), session.game.moves)
        }
    }

    @Test
    fun `Open command session state is ENDED if there's a checkmate in board of already existing game`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val movesForFoolsMate = defaultGameResultingInCheckMate.moves

            movesForFoolsMate.forEach { db.postMove(gameName, it) }

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForFoolsMate, session.game.moves)
            assertEquals(SessionState.ENDED, session.state)
        }
    }

    @Test
    fun `Open command session state is ENDED if there's a stalemate in board of already existing game`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val movesForFastestStalemate = defaultGameResultingInStaleMate.moves

            movesForFastestStalemate.forEach { db.postMove(gameName, it) }

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForFastestStalemate, session.game.moves)
            assertEquals(SessionState.ENDED, session.state)
        }
    }

    @Test
    fun `Open command session state is YOUR_TURN if it's white turn`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val moves = gameFromMoves("e3", "e6").moves

            moves.forEach { db.postMove(gameName, it) }

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(moves, session.game.moves)
            assertEquals(SessionState.YOUR_TURN, session.state)
            assertEquals(GameState.NO_CHECK, session.game.state)
        }
    }

    @Test
    fun `Open command session state is WAITING_FOR_OPPONENT if it's black turn`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val moves = gameFromMoves("e3").moves

            moves.forEach { db.postMove(gameName, it) }

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(moves, session.game.moves)
            assertEquals(SessionState.WAITING_FOR_OPPONENT, session.state)
            assertEquals(GameState.NO_CHECK, session.game.state)
        }
    }

    @Test
    fun `Open command game state is CHECK if the white King is in Check`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val movesForCheck = defaultGameResultingInWhiteCheck.moves

            movesForCheck.forEach { db.postMove(gameName, it) }

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForCheck, session.game.moves)
            assertEquals(GameState.CHECK, session.game.state)
        }
    }

    @Test
    fun `Open command game state is NO_CHECK if none of the Kings is in Check`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val movesForCheck = gameFromMoves("c3", "d6").moves

            movesForCheck.forEach { db.postMove(gameName, it) }

            val result = OpenCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForCheck, session.game.moves)
            assertEquals(GameState.NO_CHECK, session.game.state)
        }
    }
}
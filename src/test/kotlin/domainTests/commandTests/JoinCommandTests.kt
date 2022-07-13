package domainTests.commandTests

import defaultGameResultingInBlackCheck
import defaultGameResultingInCheckMate
import domain.SessionState
import domain.commands.CommandException
import domain.commands.JoinCommand
import domain.game.GameState
import domain.game.gameFromMoves
import domain.game.state
import domain.move.Move
import kotlinx.coroutines.runBlocking
import storage.GameStorageStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JoinCommandTests { // [✔]
    @Test
    fun `Join command throws CommandException if the game name is missing`() {
        runBlocking {
            val db = GameStorageStub()

            assertEquals(
                "Missing game name.",
                assertFailsWith<CommandException> {
                    JoinCommand(db).execute(null)
                }.message
            )
        }
    }

    @Test
    fun `Join command throws CommandException if joining a game that doesn't exist`() {
        runBlocking {
            val db = GameStorageStub()

            assertEquals(
                "A game with the name \"test\" does not exist: try open command.",
                assertFailsWith<CommandException> {
                    JoinCommand(db).execute("test")
                }.message
            )
        }
    }

    @Test
    fun `Join command joins the player to an existing game if it exists`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val move = Move("Pe2e4")
            db.postMove(gameName, move)

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(listOf(move), session.game.moves)
        }
    }

    @Test
    fun `Join command session state is ENDED if there's a checkmate in board of existing game`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val movesForFoolsMate = defaultGameResultingInCheckMate.moves

            movesForFoolsMate.forEach { db.postMove(gameName, it) }

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForFoolsMate, session.game.moves)
            assertEquals(SessionState.ENDED, session.state)
            assertEquals(GameState.CHECKMATE, session.game.state)
        }
    }

    @Test
    fun `Join command session state is ENDED if there's a stalemate in board of existing game`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            // Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
            val movesForFastestStalemate =
                gameFromMoves(
                    "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
                    "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
                ).moves

            movesForFastestStalemate.forEach { db.postMove(gameName, it) }

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForFastestStalemate, session.game.moves)
            assertEquals(SessionState.ENDED, session.state)
            assertEquals(GameState.STALEMATE, session.game.state)
        }
    }

    @Test
    fun `Join command session state is YOUR_TURN if it's black turn`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val moves = gameFromMoves("e3").moves

            moves.forEach { db.postMove(gameName, it) }

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(moves, session.game.moves)
            assertEquals(SessionState.YOUR_TURN, session.state)
        }
    }

    @Test
    fun `Join command session state is WAITING_FOR_OPPONENT if it's white turn`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val moves = gameFromMoves("e3", "e6").moves

            moves.forEach { db.postMove(gameName, it) }

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(moves, session.game.moves)
            assertEquals(SessionState.WAITING_FOR_OPPONENT, session.state)
            assertEquals(GameState.NO_CHECK, session.game.state)
        }
    }

    @Test
    fun `Join command game state is CHECK if the black King is in Check`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val movesForCheck = defaultGameResultingInBlackCheck.moves

            movesForCheck.forEach { db.postMove(gameName, it) }

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(movesForCheck, session.game.moves)
            assertEquals(GameState.CHECK, session.game.state)
        }
    }

    @Test
    fun `Join command game state is NO_CHECK if none of the Kings is in Check`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val moves = gameFromMoves("c3", "d6", "a3").moves

            moves.forEach { db.postMove(gameName, it) }

            val result = JoinCommand(db).execute(gameName)
            val session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(moves, session.game.moves)
            assertEquals(GameState.NO_CHECK, session.game.state)
        }
    }
}

package domainTests.commandTests

import storage.GameStorageStub
import domain.*
import domain.game.*
import domain.board.*
import domain.commands.*
import domain.move.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class RefreshCommandTests { // [✔]
    @Test
    fun `Refresh command throws CommandException if the session state is LOGGING`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val session = Session("test", SessionState.LOGGING, Game(Board(), emptyList()))

            assertEquals(
                "Can't refresh without a game: try open or join commands.",
                assertFailsWith<CommandException> {
                    RefreshCommand(db, session).invoke()
                }.message
            )
        }
    }

    @Test
    fun `Refresh command throws CommandException if the session state is YOUR_TURN`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val session = Session("test", SessionState.YOUR_TURN, Game(Board(), emptyList()))

            assertEquals(
                "It's your turn: try play.",
                assertFailsWith<CommandException> {
                    RefreshCommand(db, session).invoke()
                }.message
            )
        }
    }

    @Test
    fun `Refresh command throws CommandException if the session state is ENDED`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val session = Session("test", SessionState.ENDED, Game(Board(), emptyList()))

            assertEquals(
                "Game ended. There aren't any new moves.",
                assertFailsWith<CommandException> {
                    RefreshCommand(db, session).invoke()
                }.message
            )
        }
    }

    @Test
    fun `Refresh command returns success`() {
        runBlocking {
            val db = GameStorageStub()
            db.createGame("test")

            var session =
                Session("test", SessionState.WAITING_FOR_OPPONENT, Game(Board(), emptyList()))
            val move = Move("Pe2e4")
            db.postMove("test", move)

            val result = RefreshCommand(db, session).invoke()
            session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(SessionState.YOUR_TURN, session.state)
            assertEquals(listOf(move), session.game.moves)
        }
    }

    @Test
    fun `Refresh command session state is ENDED if the king from the army is in checkmate`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val game = gameFromMoves("f3", "e5", "g4")
            game.moves.forEach { db.postMove(gameName, it) }

            var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, game)

            val move = Move.validated("Qh4", game)
            db.postMove(gameName, move)

            val result = RefreshCommand(db, session).invoke()
            session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(game.moves + move, session.game.moves)
            assertEquals(SessionState.ENDED, session.state)
            assertEquals(GameState.CHECKMATE, session.game.state)
        }
    }

    @Test
    fun `Refresh command session state is ENDED if the king from the army is in stalemate`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val game = gameFromMoves(
                "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
                "Qd3", "Qb8", "Qh7", "Qc8", "Kg6"
            )
            game.moves.forEach { db.postMove(gameName, it) }

            var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, game)

            val move = Move.validated("Qe6", game)
            db.postMove(gameName, move)

            val result = RefreshCommand(db, session).invoke()
            session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(game.moves + move, session.game.moves)
            assertEquals(SessionState.ENDED, session.state)
            assertEquals(GameState.STALEMATE, session.game.state)
        }
    }

    @Test
    fun `Refresh command session state is YOUR_TURN and game state is CHECK if it's your turn to play and your king is in check`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val game = gameFromMoves("c3", "d6", "a3", "e6")
            game.moves.forEach { db.postMove(gameName, it) }

            var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, game)

            val move = Move.validated("Qa4", game)
            db.postMove(gameName, move)

            val result = RefreshCommand(db, session).execute(gameName)
            session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(game.moves + move, session.game.moves)
            assertEquals(SessionState.YOUR_TURN, session.state)
            assertEquals(GameState.CHECK, session.game.state)
        }
    }

    @Test
    fun `Refresh command session state is YOUR_TURN and game state is NO_CHECK if it's your turn to play and your king isn't in check`() {
        runBlocking {
            val gameName = "test"

            val db = GameStorageStub()
            db.createGame(gameName)

            val game = gameFromMoves()

            var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, game)

            val move = Move("Pe2e3")
            db.postMove(gameName, move)

            val result = RefreshCommand(db, session).invoke()
            session = result.getOrThrow()

            assertTrue(result.isSuccess)
            assertEquals(gameName, session.name)
            assertEquals(game.moves + move, session.game.moves)
            assertEquals(SessionState.YOUR_TURN, session.state)
            assertEquals(GameState.NO_CHECK, session.game.state)
        }
    }
}

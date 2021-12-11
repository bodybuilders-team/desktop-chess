package domainTests.commandTests

import GameStateStub
import domain.*
import domain.board.*
import domain.commands.*
import domain.move.*
import domain.pieces.*
import kotlin.test.*

class RefreshCommandTests {
    @Test
    fun `Refresh command throws CommandException if the session state is LOGGING`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertEquals(
            "Can't refresh without a game: try open or join commands.",
            assertFailsWith<CommandException> {
                RefreshCommand(db, session).invoke()
            }.message
        )
    }

    @Test
    fun `Refresh command throws CommandException if the session state is YOUR_TURN`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertEquals(
            "It's your turn: try play.",
            assertFailsWith<CommandException> {
                RefreshCommand(db, session).invoke()
            }.message
        )
    }

    @Test
    fun `Refresh command throws CommandException if the session state is ENDED`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.ENDED, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertEquals(
            "Game ended. There aren't any new moves.",
            assertFailsWith<CommandException> {
                RefreshCommand(db, session).invoke()
            }.message
        )
    }
    
    @Test
    fun `Refresh command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        var session =
            Session("test", SessionState.WAITING_FOR_OPPONENT, Army.BLACK, Game(Board(), emptyList()), Check.NO_CHECK)
        val move = Move("Pe2e4")
        db.postMove("test", move)

        val result = RefreshCommand(db, session).invoke()
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.YOUR_TURN, session.state)
        assertEquals(listOf(move), session.game.moves)
    }

    @Test
    fun `Refresh command session state is ENDED if the king from the army is in checkmate`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("f3", "e5", "g4", "Qh4")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, Army.WHITE, game, Check.NO_CHECK)

        val result = RefreshCommand(db, session).invoke()
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves, session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
        assertEquals(Check.NO_CHECK, session.currentCheck)
    }

    @Test
    fun `Refresh command session state is ENDED if the king from the army is in stalemate`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "2h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
            "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, Army.BLACK, game, Check.NO_CHECK)

        val result = RefreshCommand(db, session).invoke()
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves, session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
        assertEquals(Check.NO_CHECK, session.currentCheck)
    }

    @Test
    fun `Refresh command session state is WAITING_FOR_OPPONENT if it's not your turn to play`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("e3", "e6")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, Army.BLACK, game, Check.NO_CHECK)

        val result = RefreshCommand(db, session).invoke()
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves, session.game.moves)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, session.state)
        assertEquals(Check.NO_CHECK, session.currentCheck)
    }

    @Test
    fun `Refresh command session state is YOUR_TURN and currentCheck is CHECK if it's your turn to play and your king is in check`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("c3", "d6", "a3", "e6", "Qa4")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, Army.BLACK, game, Check.NO_CHECK)

        val result = RefreshCommand(db, session).execute(gameName)
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves, session.game.moves)
        assertEquals(SessionState.YOUR_TURN, session.state)
        assertEquals(Check.CHECK, session.currentCheck)
    }
    
    @Test
    fun `Refresh command session state is YOUR_TURN and currentCheck is NO_CHECK if it's your turn to play and your king isn't in check`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("e3")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, Army.BLACK, game, Check.NO_CHECK)

        val result = RefreshCommand(db, session).invoke()
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves, session.game.moves)
        assertEquals(SessionState.YOUR_TURN, session.state)
        assertEquals(Check.NO_CHECK, session.currentCheck)
    }
}
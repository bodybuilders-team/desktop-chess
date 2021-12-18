package domainTests.commandTests

import GameStateStub
import domain.*
import domain.game.*
import domain.board.*
import domain.commands.*
import domain.move.*
import domain.pieces.*
import kotlin.test.*

class PlayCommandTests {
    @Test
    fun `Play command throws CommandException if the session state is LOGGING`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session(gameName, SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()), GameState.NO_CHECK)

        assertEquals(
            "Can't play without a game: try open or join commands.",
            assertFailsWith<CommandException> {
                PlayCommand(db, session).execute("Pe2e4")
            }.message
        )
    }

    @Test
    fun `Play command throws CommandException if the session state is WAITING_FOR_OPPONENT`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session(gameName, SessionState.WAITING_FOR_OPPONENT, Army.WHITE, Game(Board(), emptyList()), GameState.NO_CHECK)

        assertEquals(
            "Wait for your turn: try refresh command.",
            assertFailsWith<CommandException> {
                PlayCommand(db, session).execute("Pe2e4")
            }.message
        )
    }

    @Test
    fun `Play command throws CommandException if the session state is ENDED`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session(gameName, SessionState.ENDED, Army.WHITE, Game(Board(), emptyList()), GameState.NO_CHECK)

        assertEquals(
            "Game ended. Can't play any more moves.",
            assertFailsWith<CommandException> {
                PlayCommand(db, session).execute("Pe2e4")
            }.message
        )
    }

    @Test
    fun `Play command throws CommandException if the move is missing`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session =
            Session(gameName, SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), GameState.NO_CHECK)

        assertEquals(
            "Missing move.",
            assertFailsWith<CommandException> {
                PlayCommand(db, session).execute(null)
            }.message
        )
    }

    @Test
    fun `Play command throws IllegalArgumentException if it's not the army's turn to play`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session =
            Session(gameName, SessionState.YOUR_TURN, Army.BLACK, Game(Board(), emptyList()), GameState.NO_CHECK)

        assertEquals(
            "It's not this army's turn! Session army is different from the current turn army.",
            assertFailsWith<IllegalArgumentException> {
                PlayCommand(db, session).execute("Pe2e4")
            }.message
        )
    }

    @Test
    fun `Play command session state is ENDED and gameState is CHECK_MATE if the king from the other army is in checkmate after the move`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("f3", "e5", "g4")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.YOUR_TURN, Army.BLACK, game, GameState.NO_CHECK)

        val move = "Qh4"
        val result = PlayCommand(db, session).execute(move)
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves + Move("Qd8h4"), session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
        assertEquals(GameState.CHECKMATE, session.gameState)
    }

    @Test
    fun `Play command session state is ENDED and gameState is STALE_MATE if the king from the other army is in stalemate after the move`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
            "Qd3", "Qb8", "Qh7", "Qc8", "Kg6")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.YOUR_TURN, Army.WHITE, game, GameState.NO_CHECK)

        val move = "Qe6"
        val result = PlayCommand(db, session).execute(move)
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves + Move("Qc8e6"), session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
        assertEquals(GameState.STALEMATE, session.gameState)
    }

    @Test
    fun `Play command session state is WAITING_FOR_OPPONENT and gameState is CHECK if the king from the other army is in check after the move`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("c3", "d6", "a3", "e6")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.YOUR_TURN, Army.WHITE, game, GameState.NO_CHECK)

        val move = "Qa4"
        val result = PlayCommand(db, session).execute(move)
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves + Move("Qd1a4"), session.game.moves)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, session.state)
        assertEquals(GameState.CHECK, session.gameState)
    }

    @Test
    fun `Play command session state is WAITING_FOR_OPPONENT and gameState is NO_CHECK if the king from the other army isn't in checkmate, stalemate or check after the move`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val game = gameFromMoves("c3", "d6", "a3", "e6")
        game.moves.forEach { db.postMove(gameName, it) }

        var session = Session(gameName, SessionState.YOUR_TURN, Army.WHITE, game, GameState.NO_CHECK)

        val move = "e3"
        val result = PlayCommand(db, session).execute(move)
        session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(game.moves + Move("Pe2e3"), session.game.moves)
        assertEquals(GameState.NO_CHECK, session.gameState)
    }
}
package domainTests.commandTests

import domain.*
import GameStateStub
import domain.board.*
import domain.commands.*
import domain.move.Move
import domain.pieces.Army
import kotlin.test.*


class CommandTests {

    // ExitCommand

    @Test
    fun `Exit command returns failure`() {
        assertTrue(ExitCommand().invoke().isFailure)
    }

    // HelpCommand

    @Test
    fun `Help command returns successful result with the same session`() {
        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)
        val result = HelpCommand(session).invoke()

        assertTrue(result.isSuccess)
        assertEquals(session, result.getOrThrow())
    }

    // JoinCommand

    @Test
    fun `Join command joins the player to an existing game if it exists`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val move = Move("Pe2e4")
        db.postMove(gameName, move)

        val result = JoinCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(listOf(move), session.game.moves)
    }

    @Test
    fun `Join command game state is ENDED if there's a checkmate in board of existing game`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        // Fool's mate! 🤡 - https://www.chess.com/article/view/fastest-chess-checkmates
        val movesForFoolsMate = gameFromMoves("f3", "e5", "g4", "Qh4").moves

        movesForFoolsMate.forEach { db.postMove(gameName, it) }

        val result = JoinCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForFoolsMate, session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
    }

    @Test
    fun `Join command game state is ENDED if there's a stalemate in board of existing game`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        // Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
        val movesForFastestStalemate =
            gameFromMoves(
                "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "2h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
                "Qd3", "Qb8", "Qh7", "Qc8", "Kg6", "Qe6"
            ).moves

        movesForFastestStalemate.forEach { db.postMove(gameName, it) }

        val result = JoinCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForFastestStalemate, session.game.moves)
        assertEquals(SessionState.ENDED, session.state)
    }

    @Test
    fun `Join command throws CommandException if joining a game that doesn't exist`() {
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            JoinCommand(db).execute("test")
        }
    }

    @Test
    fun `Join command throws CommandException if the game name is missing`() {
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            JoinCommand(db).execute(null)
        }
    }

    @Test
    fun `Join command currentCheck is CHECK if the black King is in Check and its black turn`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val movesForCheck = gameFromMoves("c3", "d6", "a3", "e6", "Qa4").moves

        movesForCheck.forEach { db.postMove(gameName, it) }

        val result = JoinCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForCheck, session.game.moves)
        assertEquals(Check.CHECK, session.currentCheck)
    }

    @Test
    fun `Join command currentCheck is NO_CHECK if the black King isn't in Check and its black turn`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val movesForCheck = gameFromMoves("c3", "d6", "a3").moves

        movesForCheck.forEach { db.postMove(gameName, it) }

        val result = JoinCommand(db).execute(gameName)
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(gameName, session.name)
        assertEquals(movesForCheck, session.game.moves)
        assertEquals(Check.NO_CHECK, session.currentCheck)
    }

    // MovesCommand

    @Test
    fun `Moves command returns successful result with the same session`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        val result = MovesCommand(db, session).execute(gameName)
        val newSession = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(session, newSession)
    }

    @Test
    fun `Moves command throws CommandException if the session is logging`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertFailsWith<CommandException> { MovesCommand(db, session).execute(gameName) }
    }

    // OpenCommand

    @Test
    fun `Open command creates game if it doesn't exist`() {
        val db = GameStateStub()
        val result = OpenCommand(db).execute("test")
        val session = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals("test", session.name)
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
    fun `Open command game state is ENDED if there's a checkmate in board of already existing game`() {
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
    fun `Open command game state is ENDED if there's a stalemate in board of already existing game`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        // Fastest stalemate known - https://www.chess.com/forum/view/game-showcase/fastest-stalemate-known-in-chess
        val movesForFastestStalemate =
            gameFromMoves(
                "e3", "a5", "Qh5", "Ra6", "Qa5", "h5", "2h4", "Rah6", "Qc7", "f6", "Qd7", "Kf7", "Qb7",
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
    fun `Open command throws CommandException if the game name is missing`() {
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            OpenCommand(db).execute(null)
        }
    }

    @Test
    fun `Open command currentCheck is CHECK if the white King is in Check and its white turn`() {
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
        assertEquals(Check.CHECK, session.currentCheck)
    }

    @Test
    fun `Open command currentCheck is NO_CHECK if the white King isn't in Check and its white turn`() {
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
        assertEquals(Check.NO_CHECK, session.currentCheck)
    }

    // PlayCommand

    @Test
    fun `Play command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        val move = "Pe2e4"
        val result = PlayCommand(db, session).execute(move)
        val newSession = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, newSession.state)
        assertEquals(listOf(Move.validated(move, session.game.board, session.game.moves)), newSession.game.moves)
    }

    @Test
    fun `Play command throws CommandException if the session is logging`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertFailsWith<CommandException> { PlayCommand(db, session).invoke() }
    }

    @Test
    fun `Play command throws CommandException if the move is missing`() {
        val db = GameStateStub()
        val session =
            Session("test", SessionState.YOUR_TURN, Army.BLACK, Game(Board(), emptyList()), Check.NO_CHECK)

        assertFailsWith<CommandException> {
            PlayCommand(db, session).execute(null)
        }
    }

    // RefreshCommand

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
    fun `Refresh command throws CommandException if the session is logging`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.LOGGING, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertFailsWith<CommandException> { RefreshCommand(db, session).invoke() }
    }

    @Test
    fun `Refresh throws CommandException if the session state is ENDED`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.ENDED, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertFailsWith<CommandException> { RefreshCommand(db, session).invoke() }
    }

    @Test
    fun `Refresh command throws CommandException if the session state is YOUR_TURN`() {
        val gameName = "test"

        val db = GameStateStub()
        db.createGame(gameName)

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Game(Board(), emptyList()), Check.NO_CHECK)

        assertFailsWith<CommandException> { RefreshCommand(db, session).invoke() }
    }

    // boardWithMoves

    @Test
    fun `boardWithMoves returns original board if moves list is empty`() {
        val board = Board()
        assertEquals(board.toString(), boardWithMoves(emptyList()).toString())
    }

    @Test
    fun `boardWithMoves returns new board with the moves made`() {
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
        val moves = listOf(Move("Pe2e4"), Move("Pb7b5"), Move("Nb1c3"))

        assertEquals(board.toString(), boardWithMoves(moves).toString())
    }
}

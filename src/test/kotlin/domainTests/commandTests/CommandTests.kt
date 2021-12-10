package domainTests.commandTests

import domain.*
import GameStateStub
import domain.board.*
import domain.commands.*
import domain.move.Move
import domain.pieces.Army
import kotlin.test.*


class CommandTests {

    @Test
    fun `Open command creates game if it doesn't exist (empty list of moves)`(){
        val db = GameStateStub()
        val result = OpenCommand(db).execute("test")
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals("test", chess.name)
        assertEquals(listOf(), chess.moves)
    }

    @Test
    fun `Open command opens an existing game if it exists`(){
        val db = GameStateStub()
        db.createGame("test")

        val move = Move("Pe2e4")
        db.postMove("test", move)

        val result = OpenCommand(db).execute("test")
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals("test", chess.name)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Join command joins the player to an existing game if it exists`(){
        val db = GameStateStub()
        db.createGame("test")

        val move = Move("Pe2e4")
        db.postMove("test", move)

        val result = JoinCommand(db).execute("test")
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals("test", chess.name)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Join command to a game that doesn't exist throws`(){
        val db = GameStateStub()

        assertFailsWith<CommandException> {
            JoinCommand(db).execute("test")
        }
    }

    @Test
    fun `Exit command returns failure`() {
        assertTrue(ExitCommand().invoke().isFailure)
    }

    @Test
    fun `Help command returns success`() {
        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList(), Check.NO_CHECK)
        val result = HelpCommand(session).invoke()

        assertTrue(result.isSuccess)
        assertEquals(session, result.getOrThrow())
    }

    @Test
    fun `Refresh command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.WAITING_FOR_OPPONENT, Army.BLACK, Board(), emptyList(), Check.NO_CHECK)
        val move = Move("Pe2e4")
        db.postMove("test", move)

        val result = RefreshCommand(db, session).invoke()
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.YOUR_TURN, chess.state)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Moves command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        var session = Session("test", SessionState.WAITING_FOR_OPPONENT, Army.WHITE, Board(), emptyList(), Check.NO_CHECK)
        val move = Move("Pe2e4")
        db.postMove("test", move)

        session = RefreshCommand(db, session).invoke().getOrThrow()
        val result = MovesCommand(db, session).invoke()

        assertTrue(result.isSuccess)
        assertEquals(listOf(move), result.getOrThrow().moves)
    }

    @Test
    fun `Play command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList(), Check.NO_CHECK)

        val move = "Pe2e4"
        val result = PlayCommand(db, session).execute(move)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, chess.state)
        assertEquals(listOf(Move.validated(move, session.board, session.moves)), chess.moves)
    }

    @Test
    fun `boardWithMoves works`() {
        val board = Board()
        assertEquals(board.toString(), boardWithMoves(emptyList()).toString())
    }

}

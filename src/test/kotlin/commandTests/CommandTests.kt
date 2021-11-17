package commandTests

import domain.*
import GameStateStub
import domain.commands.*
import domain.pieces.Army
import java.lang.IllegalArgumentException
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

        val move = Move.extractMoveInfo("Pe2e4").move
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

        val move = Move.extractMoveInfo("Pe2e4").move
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

        assertFailsWith<IllegalArgumentException> {
            JoinCommand(db).execute("test")
        }
    }

    @Test
    fun `Exit command returns failure`() {
        assertTrue(ExitCommand().execute(null).isFailure)
    }

    @Test
    fun `Help command returns success`() {
        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList())
        val result = HelpCommand(session).execute(null)

        assertTrue(result.isSuccess)
        assertEquals(session, result.getOrThrow())
    }

    @Test
    fun `Refresh command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.WAITING_FOR_OPPONENT, Army.BLACK, Board(), emptyList())
        val move = Move.extractMoveInfo("Pe2e4").move
        db.postMove("test", move)

        val result = RefreshCommand(db, session).execute(null)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.YOUR_TURN, chess.state)
        assertEquals(listOf(move), chess.moves)
    }

    @Test
    fun `Moves command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.WAITING_FOR_OPPONENT, Army.WHITE, Board(), emptyList())
        val move = Move.extractMoveInfo("Pe2e4").move
        db.postMove("test", move)

        val result = MovesCommand(db, session).execute(null)

        assertTrue(result.isSuccess)
        assertEquals(listOf(move), result.getOrThrow().moves)
    }

    @Test
    fun `Play command returns success`() {
        val db = GameStateStub()
        db.createGame("test")

        val session = Session("test", SessionState.YOUR_TURN, Army.WHITE, Board(), emptyList())

        val move = "Pe2e4"
        val result = PlayCommand(db, session).execute(move)
        val chess = result.getOrThrow()

        assertTrue(result.isSuccess)
        assertEquals(SessionState.WAITING_FOR_OPPONENT, chess.state)
        assertEquals(listOf(Move(move, session.board)), chess.moves)
    }

    @Test
    fun `currentTurnArmy returns White when number of moves is even`() {
        assertEquals(Army.WHITE, currentTurnArmy(emptyList()))
    }

    @Test
    fun `currentTurnArmy returns Black when number of moves is odd`() {
        assertEquals(Army.BLACK, currentTurnArmy(listOf(Move("Pe2e4", Board()))))
    }

    @Test
    fun `isWhiteTurn returns true when number of moves is even`() {
        assertTrue(isWhiteTurn(emptyList()))
    }

    @Test
    fun `isWhiteTurn returns false when number of moves is odd`() {
        assertFalse(isWhiteTurn(listOf(Move("Pe2e4", Board()))))
    }

    @Test
    fun `boardWithMoves works`() {
        val board = Board()
        assertEquals(board.toString(), boardWithMoves(emptyList()).toString())
    }

}

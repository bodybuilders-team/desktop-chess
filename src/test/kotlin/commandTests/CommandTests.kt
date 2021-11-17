package commandTests

import domain.*
import GameStateStub
import domain.commands.*
import java.lang.IllegalArgumentException
import kotlin.test.*

class CommandTests {

    @Test
    fun `Open command creates game if it doesn't exist (empty list of moves)`(){
        val db = GameStateStub()

        val result = OpenCommand(db).execute("test")

        val chess = result.getOrThrow()
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
}
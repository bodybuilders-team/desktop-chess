package commandTests

import domain.*
import GameStateStub
import kotlin.test.*

class GameStateTests {

    @Test
    fun `Create game creates a game with empty list of moves`(){
        val sut = GameStateStub()

        sut.createGame("test")
        assertEquals(listOf(), sut.getAllMoves("test"))
    }

    @Test
    fun `Get game returns name of the game if the game exists`(){
        val sut = GameStateStub()

        sut.createGame("test")
        assertEquals("test", sut.getGame("test"))
    }

    @Test
    fun `Get game returns null if the game doesn't exist`(){
        val sut = GameStateStub()

        assertEquals(null, sut.getGame("test"))
    }

    @Test
    fun `Get all moves returns list of moves of the game`(){
        val sut = GameStateStub()

        sut.createGame("test")
        val moves = listOf(Move.extractMoveInfo("Pe2e4").move,
                            Move.extractMoveInfo("Pe7e5").move,
                            Move.extractMoveInfo("Pf2f4").move)

        sut.postMove("test", moves[0])
        sut.postMove("test", moves[1])
        sut.postMove("test", moves[2])

        assertEquals(moves, sut.getAllMoves("test"))
    }

    @Test
    fun `Post move adds a move to the list of moves`(){
        val sut = GameStateStub()

        sut.createGame("test")

        val move = Move.extractMoveInfo("Pe2e4").move

        sut.postMove("test", move)
        assertEquals(listOf(move), sut.getAllMoves("test"))
    }
}
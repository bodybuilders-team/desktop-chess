package commandTests

import domain.*
import GameStateStub
import kotlin.test.*


class GameStateTests {

    @Test
    fun `createGame creates a game with empty list of moves`() {
        val sut = GameStateStub()

        sut.createGame("test")
        assertEquals(listOf(), sut.getAllMoves("test"))
    }

    @Test
    fun `gameExists returns true if the game exists`() {
        val sut = GameStateStub()

        sut.createGame("test")
        assertTrue(sut.gameExists("test"))
    }

    @Test
    fun `gameExists returns false if the game doesn't exist`() {
        val sut = GameStateStub()

        assertFalse(sut.gameExists("test"))
    }

    @Test
    fun `getAllMoves returns list of moves of the game`() {
        val sut = GameStateStub()

        sut.createGame("test")
        val moves = listOf(
            Move.extractMoveInfo("Pe2e4").move,
            Move.extractMoveInfo("Pe7e5").move,
            Move.extractMoveInfo("Pf2f4").move
        )

        moves.forEach { move -> sut.postMove("test", move) }

        assertEquals(moves, sut.getAllMoves("test"))
    }

    @Test
    fun `postMove adds a move to the list of moves`() {
        val sut = GameStateStub()

        sut.createGame("test")

        val move = Move.extractMoveInfo("Pe2e4").move

        sut.postMove("test", move)
        assertEquals(listOf(move), sut.getAllMoves("test"))
    }
}

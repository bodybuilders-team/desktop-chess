package storageTests

import GameStorageStub
import domain.move.*
import kotlin.test.*


class GameStorageTests {

    // getAllMoves [✔]

    @Test
    fun `getAllMoves returns list of moves of the game`() {
        val gameName = "test"
        
        val sut = GameStorageStub()

        sut.createGame(gameName)
        val moves = listOfMoves("Pe2e4", "Pe7e5", "Pf2f4")

        moves.forEach { move -> sut.postMove(gameName, move) }

        assertEquals(moves, sut.getAllMoves(gameName))
    }

    @Test
    fun `getAllMoves throws IllegalArgumentException if game with that name does not exist`() {
        val gameName = "test"
        
        val sut = GameStorageStub()

        assertEquals(
            "A game with the name \"$gameName\" does not exist.",
            assertFailsWith<IllegalArgumentException> {
                sut.getAllMoves(gameName)
            }.message
        )
    }
    
    // postMove [✔]

    @Test
    fun `postMove adds a move to the list of moves`() {
        val gameName = "test"
        
        val sut = GameStorageStub()

        sut.createGame(gameName)

        val move = Move("Pe2e4")

        sut.postMove(gameName, move)
        assertEquals(listOf(move), sut.getAllMoves(gameName))
    }

    @Test
    fun `postMove throws IllegalArgumentException if game with that name does not exist`() {
        val gameName = "test"

        val sut = GameStorageStub()

        assertEquals(
            "A game with the name \"$gameName\" does not exist.",
            assertFailsWith<IllegalArgumentException> {
                sut.postMove(gameName, Move("Pe2e4"))
            }.message
        )
    }
    
    // createGame [✔]

    @Test
    fun `createGame creates a game with empty list of moves`() {
        val sut = GameStorageStub()

        sut.createGame("test")
        assertEquals(listOf(), sut.getAllMoves("test"))
    }

    @Test
    fun `createGame throws if game with that name already exists`() {
        val gameName = "test"
        
        val sut = GameStorageStub()

        sut.createGame(gameName)
        
        assertEquals(
            "A game with the name \"$gameName\" already exists.",
            assertFailsWith<IllegalArgumentException> {
                sut.createGame(gameName)
            }.message
        )
    }
    
    // gameExists [✔]

    @Test
    fun `gameExists returns true if the game exists`() {
        val sut = GameStorageStub()

        sut.createGame("test")
        assertTrue(sut.gameExists("test"))
    }

    @Test
    fun `gameExists returns false if the game doesn't exist`() {
        val sut = GameStorageStub()

        assertFalse(sut.gameExists("test"))
    }
}

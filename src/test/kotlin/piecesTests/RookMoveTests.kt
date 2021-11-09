package piecesTests

import Board
import Move
import getBoardFromString
import kotlin.test.*

private const val testBoard =
            "        " +
            "        " +
            "        " +
            " p      " +
            "        " +
            "        " +
            " R      " +
            "        "

class RookMoveTests {
    private val board = Board(getBoardFromString(testBoard))

    @Test
    fun `Rook vertical(up) move is valid`() {
        assertTrue(board.checkMove(Move("Rb2b4")))
    }

    @Test
    fun `Rook vertical(down) move is valid`() {
        assertTrue(board.checkMove(Move("Rb2b1")))
    }

    @Test
    fun `Rook horizontal(right) move is valid`() {
        assertTrue(board.checkMove(Move("Rb2f2")))
    }

    @Test
    fun `Rook horizontal(left) move is valid`() {
        assertTrue(board.checkMove(Move("Rb2a2")))
    }

    @Test
    fun `Rook diagonal not valid`() {
        assertFalse(board.checkMove(Move("Rb2c3")))
    }

    @Test
    fun `Rook move with capture is valid`() {
        assertTrue(board.checkMove(Move("Rb2b5")))
    }
}
package piecesTests

import domain.*
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
    private val board = Board(getMatrix2DFromString(testBoard))

    @Test
    fun `Rook vertical(up) move is valid`() {
        assertTrue(board.isValidMove(Move("Rb2b4")))
    }

    @Test
    fun `Rook vertical(down) move is valid`() {
        assertTrue(board.isValidMove(Move("Rb2b1")))
    }

    @Test
    fun `Rook horizontal(right) move is valid`() {
        assertTrue(board.isValidMove(Move("Rb2f2")))
    }

    @Test
    fun `Rook horizontal(left) move is valid`() {
        assertTrue(board.isValidMove(Move("Rb2a2")))
    }

    @Test
    fun `Rook move with capture is valid`() {
        assertTrue(board.isValidMove(Move("Rb2b5")))
    }

    @Test
    fun `Rook diagonal not valid`() {
        assertFalse(board.isValidMove(Move("Rb2c3")))
    }

    @Test
    fun `Rook move to same place is not valid`() {
        assertFalse(board.isValidMove(Move("Rb2b2")))
    }
}

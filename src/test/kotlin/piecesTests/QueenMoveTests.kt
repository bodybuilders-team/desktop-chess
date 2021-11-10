package piecesTests

import Board
import Move
import getMatrix2DFromString
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

private const val testBoard =
            "        " +
            "        " +
            "        " +
            " p      " +
            "        " +
            "        " +
            " Q      " +
            "        "

class QueenMoveTests {
    private val board = Board(getMatrix2DFromString(testBoard))

    @Test
    fun `Queen vertical(up) move is valid`() {
        assertTrue(board.isValidMove(Move("Qb2b4")))
    }

    @Test
    fun `Queen vertical(down) move is valid`() {
        assertTrue(board.isValidMove(Move("Qb2b1")))
    }

    @Test
    fun `Queen horizontal(right) move is valid`() {
        assertTrue(board.isValidMove(Move("Qb2f2")))
    }

    @Test
    fun `Queen horizontal(left) move is valid`() {
        assertTrue(board.isValidMove(Move("Qb2a2")))
    }

    @Test
    fun `Queen move with capture is valid`() {
        assertTrue(board.isValidMove(Move("Qb2b5")))
    }

    @Test
    fun `Queen move(up,right) with capture is valid`() {
        assertTrue(board.isValidMove(Move("Qb2c3")))
    }

    @Test
    fun `Queen move(up,left) is valid`() {
        assertTrue(board.isValidMove(Move("Qb2a3")))
    }

    @Test
    fun `Queen move(down,right) is valid`() {
        assertTrue(board.isValidMove(Move("Qb2c1")))
    }

    @Test
    fun `Queen move(down,left) is valid`() {
        assertTrue(board.isValidMove(Move("Qb2a1")))
    }

    @Test
    fun `Queen move to same place does not work`() {
        assertThrows<IllegalArgumentException> {board.isValidMove(Move("Qb2b2"))}
    }
}

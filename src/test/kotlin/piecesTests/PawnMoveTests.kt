package piecesTests

import Board
import Move
import getMatrix2DFromString
import isValidMove
import kotlin.test.*


//Pawn in position e2
private const val testBoard =
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "   n    " +
            "    P   " +
            "        "


class PawnMoveTests {
    private val board = Board(getMatrix2DFromString(testBoard))

    @Test
    fun `Pawn vertical one move is valid`() {
        assertTrue(board.isValidMove(Move("Pe2e3")))
    }

    @Test
    fun `Pawn vertical double move is valid`() {
        assertTrue(board.isValidMove(Move("Pe2e4")))
    }

    @Test
    fun `Pawn diagonal move with capture is valid`() {
        assertTrue(board.isValidMove(Move("Pe2d3")))
    }

    @Test
    fun `Pawn horizontal move isn't valid`() {
        assertFalse(board.isValidMove(Move("Pe2f2")))
    }

    @Test
    fun `Pawn diagonal move without capture isn't valid`() {
        assertFalse(board.isValidMove(Move("Pe2f3")))
    }

    @Test
    fun `Pawn vertical move backwards isn't valid`() {
        assertFalse(board.isValidMove(Move("Pe2e1")))
    }

    @Test
    fun `Pawn diagonal move backwards isn't valid`() {
        assertFalse(board.isValidMove(Move("Pe2f1")))
    }

    @Test
    fun `Pawn move to same place is not valid`() {
        assertFalse(board.isValidMove(Move("Pe2e2")))
    }
}

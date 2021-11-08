package piecesTests

import Board
import Move
import pieces.Color
import pieces.Pawn
import kotlin.test.*


class PawnMoveTests {
    @Test
    fun `Pawn vertical one move is valid`() {
        assertTrue(Pawn(Color.WHITE).checkMove(Board(), Move("Pe2e3")))
    }

    @Test
    fun `Pawn vertical double move is valid`() {
        assertTrue(Pawn(Color.WHITE).checkMove(Board(), Move("Pe2e4")))
    }

    @Test
    fun `Pawn horizontal move isn't valid`() {
        assertFalse(Pawn(Color.WHITE).checkMove(Board(), Move("Pe2f2")))
    }

    @Test
    fun `Pawn diagonal move without capture isn't valid`() {
        assertFalse(Pawn(Color.WHITE).checkMove(Board(), Move("Pe2f3")))
    }

    @Test
    fun `Pawn vertical move backwards isn't valid`() {
        assertFalse(Pawn(Color.WHITE).checkMove(Board(), Move("Pe2e1")))
    }

    @Test
    fun `Pawn diagonal move without backwards isn't valid`() {
        assertFalse(Pawn(Color.WHITE).checkMove(Board(), Move("Pe2f1")))
    }
}

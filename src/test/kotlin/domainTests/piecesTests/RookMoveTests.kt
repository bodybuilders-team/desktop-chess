package domainTests.piecesTests

import domain.board.*
import kotlin.test.*
import isValidMove


class RookMoveTests {
    private val sut = Board(
        getMatrix2DFromString(
            "        " +
            "        " +
            "        " +
            " p      " +
            "        " +
            "        " +
            " R      " +
            "       K"
        )
    )

    @Test
    fun `Rook vertical(up) move is valid`() {
        assertTrue(sut.isValidMove("Rb2b4"))
    }

    @Test
    fun `Rook vertical(down) move is valid`() {
        assertTrue(sut.isValidMove("Rb2b1"))
    }

    @Test
    fun `Rook horizontal(right) move is valid`() {
        assertTrue(sut.isValidMove("Rb2f2"))
    }

    @Test
    fun `Rook horizontal(left) move is valid`() {
        assertTrue(sut.isValidMove("Rb2a2"))
    }

    @Test
    fun `Rook move with capture is valid`() {
        assertTrue(sut.isValidMove("Rb2b5"))
    }

    @Test
    fun `Rook diagonal not valid`() {
        assertFalse(sut.isValidMove("Rb2c3"))
    }

    @Test
    fun `Rook move to same place is not valid`() {
        assertFalse(sut.isValidMove("Rb2b2"))
    }
}

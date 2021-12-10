package domainTests.moveTests

import domain.board.Board
import domain.move.*
import domain.board.Board.*
import domain.board.getMatrix2DFromString
import java.lang.IllegalArgumentException
import kotlin.test.*

class PathUtilsTests {

    //anyPositionInStraightPath

    @Test
    fun `anyPositionInStraightPath returns true if any position in the straight path matches predicate`() {
        val move = Move("Re2e6")
        assertTrue(move.isStraight())
        assertTrue(anyPositionInStraightPath(move, includeFromPos = false) { pos ->
            pos == Position('e', 3)
        })
    }

    @Test
    fun `anyPositionInStraightPath returns true if multiple positions in the straight path match predicate`() {
        val move = Move("Re2e6")
        assertTrue(move.isStraight())
        assertTrue(anyPositionInStraightPath(move, includeFromPos = false) { pos ->
            pos.col == 'e'
        })
    }

    @Test
    fun `anyPositionInStraightPath returns false if no position in the straight path matches predicate`() {
        val move = Move("Re2e6")
        assertTrue(move.isStraight())
        assertFalse(anyPositionInStraightPath(move, includeFromPos = false) { pos ->
            pos.col == 'g'
        })
    }

    @Test
    fun `anyPositionInStraightPath returns false if only toPos in the straight path matches predicate`() {
        val move = Move("Re2e6")
        assertTrue(move.isStraight())
        assertFalse(anyPositionInStraightPath(move, includeFromPos = false) { pos ->
            pos == Position('e', 6)
        })
    }

    @Test
    fun `anyPositionInStraightPath returns true if only fromPos in the straight path matches predicate and it's included`() {
        val move = Move("Re2e6")
        assertTrue(move.isStraight())
        assertTrue(anyPositionInStraightPath(move, includeFromPos = true) { pos ->
            pos == Position('e', 2)
        })
    }

    @Test
    fun `anyPositionInStraightPath returns false if only fromPos in the straight path matches predicate but it's not included`() {
        val move = Move("Re2e6")
        assertTrue(move.isStraight())
        assertFalse(anyPositionInStraightPath(move, includeFromPos = false) { pos ->
            pos == Position('e', 2)
        })
    }

    @Test
    fun `anyPositionInStraightPath throws if move is not straight`() {
        val move = Move("Re2f6")
        assertFailsWith<IllegalArgumentException> {
            anyPositionInStraightPath(move, includeFromPos = false) { pos ->
                pos == Position('e', 2)
            }
        }
    }

    //anyPositionInDiagonalPath

    @Test
    fun `anyPositionInDiagonalPath returns true if any position in the diagonal path matches predicate`() {
        val move = Move("Be2h5")
        assertTrue(move.isDiagonal())
        assertTrue(anyPositionInDiagonalPath(move, includeFromPos = false) { pos ->
            pos == Position('f', 3)
        })
    }

    @Test
    fun `anyPositionInDiagonalPath returns true if multiple positions in the diagonal path match predicate`() {
        val move = Move("Be2h5")
        assertTrue(move.isDiagonal())
        assertTrue(anyPositionInDiagonalPath(move, includeFromPos = false) { pos ->
            pos.col in listOf('f', 'g')
        })
    }

    @Test
    fun `anyPositionInDiagonalPath returns false if no position in the diagonal path matches predicate`() {
        val move = Move("Be2h5")
        assertTrue(move.isDiagonal())
        assertFalse(anyPositionInDiagonalPath(move, includeFromPos = false) { pos ->
            pos.col == 'c'
        })
    }

    @Test
    fun `anyPositionInDiagonalPath returns false if only toPos in the diagonal path matches predicate`() {
        val move = Move("Be2h5")
        assertTrue(move.isDiagonal())
        assertFalse(anyPositionInDiagonalPath(move, includeFromPos = false) { pos ->
            pos == Position('h', 5)
        })
    }

    @Test
    fun `anyPositionInDiagonalPath returns true if only fromPos in the diagonal path matches predicate and it's included`() {
        val move = Move("Be2h5")
        assertTrue(move.isDiagonal())
        assertTrue(anyPositionInDiagonalPath(move, includeFromPos = true) { pos ->
            pos == Position('e', 2)
        })
    }

    @Test
    fun `anyPositionInDiagonalPath returns false if only fromPos in the diagonal path matches predicate but it's not included`() {
        val move = Move("Be2h5")
        assertTrue(move.isDiagonal())
        assertFalse(anyPositionInDiagonalPath(move, includeFromPos = false) { pos ->
            pos == Position('e', 2)
        })
    }

    @Test
    fun `anyPositionInDiagonalPath throws if move is not diagonal`() {
        val move = Move("Be2f6")
        assertFailsWith<IllegalArgumentException> {
            anyPositionInDiagonalPath(move, includeFromPos = false) { pos ->
                pos == Position('e', 2)
            }
        }
    }
    
    //isStraightPathOccupied

    @Test
    fun `isStraightPathOccupied returns true if the straight path is occupied`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "        " +
                "        " +
                "    b   " +
                "    N   " +
                "        " +
                "    R   " +
                "        "
            )
        )
        
        val move = Move("Re2e5")
        assertTrue(move.isStraight())
        assertTrue(isStraightPathOccupied(sut, move))
    }

    @Test
    fun `isStraightPathOccupied returns false if the straight path isn't occupied`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "        " +
                "        " +
                "    b   " +
                "        " +
                "        " +
                "    R   " +
                "        "
            )
        )

        val move = Move("Re2e5")
        assertTrue(move.isStraight())
        assertFalse(isStraightPathOccupied(sut, move))
    }

    //isDiagonalPathOccupied
    
    @Test
    fun `isDiagonalPathOccupied returns true if the diagonal path is occupied`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "     r  " +
                "        " +
                "   N    " +
                "        " +
                " B      " +
                "        " +
                "        "
            )
        )

        val move = Move("Bb3f7")
        assertTrue(move.isDiagonal())
        assertTrue(isDiagonalPathOccupied(sut, move))
    }

    @Test
    fun `isDiagonalPathOccupied returns false if the diagonal path isn't occupied`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "     r  " +
                "        " +
                "        " +
                "        " +
                " B      " +
                "        " +
                "        "
            )
        )

        val move = Move("Bb3f7")
        assertTrue(move.isDiagonal())
        assertFalse(isDiagonalPathOccupied(sut, move))
    }

    //isValidStraightMove

    @Test
    fun `isValidStraightMove returns true if the move is a valid straight move`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "        " +
                "        " +
                "    b   " +
                "        " +
                "        " +
                "    R   " +
                "        "
            )
        )

        val move = Move("Re2e5")
        assertTrue(isValidStraightMove(sut, move))
    }

    @Test
    fun `isValidStraightMove returns false if the move is not a valid straight move`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "        " +
                "        " +
                "    b   " +
                "    N   " +
                "        " +
                "    R   " +
                "        "
            )
        )

        val move = Move("Re2e5")
        assertFalse(isValidStraightMove(sut, move))
    }

    //isValidDiagonalMove

    @Test
    fun `isValidDiagonalMove returns true if the move is a valid diagonal move`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "     r  " +
                "        " +
                "        " +
                "        " +
                " B      " +
                "        " +
                "        "
            )
        )

        val move = Move("Bb3f7")
        assertTrue(isValidDiagonalMove(sut, move))
    }

    @Test
    fun `isValidDiagonalMove returns false if the move is not a valid diagonal move`() {
        val sut = Board(
            getMatrix2DFromString(
                "        " +
                "     r  " +
                "        " +
                "   N    " +
                "        " +
                " B      " +
                "        " +
                "        "
            )
        )

        val move = Move("Bb3f7")
        assertFalse(isValidDiagonalMove(sut, move))
    }
}
package piecesTests

import Board
import Move
import getBoardFromString
import kotlin.test.*

private const val testBoard =
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "        " +
            "        "

class KnightMoveTests {
    private val board = Board(getBoardFromString(testBoard))
}
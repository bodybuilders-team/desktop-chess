import kotlin.test.*


class PieceTypeTests {
    @Test
    fun `PieceType to string is the the first character of the piece name`() {
        Board.PieceType.values().forEach { println(it.toString()) }
        assertEquals("B", Board.PieceType.BISHOP.toString())
    }
}
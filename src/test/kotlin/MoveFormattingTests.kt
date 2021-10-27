import java.lang.IllegalArgumentException
import kotlin.test.*


class MoveFormattingTests {
    @Test
    fun `correctly written move returns no errors`() {
        Board.Move("Pe2e4")
    }

    @Test
    fun `wrongly written 'from position' throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Board.Move("P92e4")
        }
    }

}
package moveTests

import Move
import java.lang.IllegalArgumentException
import kotlin.test.*


class MoveFormattingTests {
    @Test
    fun `valid simple move returns no errors`() {
        Move("Pe2e4")
    }

    @Test
    fun `valid capture returns no errors`() {
        Move("Pe2xd3")
    }

    @Test
    fun `valid promotion returns no errors`() {
        Move("Pe7e8=Q")
    }

    @Test
    fun `empty throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("")
        }
    }

    @Test
    fun `invalid piece type throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("A92e4")
        }
    }

    @Test
    fun `invalid 'from position' throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("P92e4")
        }
    }

    @Test
    fun `invalid 'capture char' throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("Pe2fe4")
        }
    }

    @Test
    fun `invalid 'to position' throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("Pe2e9")
        }
    }

    @Test
    fun `invalid promotion symbol throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("Pe2e9+Q")
        }
    }

    @Test
    fun `invalid promotion piece throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("Pe2e9=L")
        }
    }

    @Test
    fun `too lengthy throws error`() {
        assertFailsWith<IllegalArgumentException> {
            Move("Pe2e9aaaaaa")
        }
    }

}
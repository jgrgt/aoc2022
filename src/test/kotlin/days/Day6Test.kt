package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day6Test {
    @Test
    fun `it can handle example 1`() {
        val s = "bvwbjplbgvbhsrlpgdmjqwftvncz"
        val actual = Day6Util.findMarker(s)
        assertEquals(5, actual)
    }
    @Test
    fun `it can handle example a`() {
        val s = "aaabcde"
        val actual = Day6Util.findMarker(s)
        assertEquals(6, actual)
    }

    @Test
    fun `it can handle example 2`() {
        val s = "nppdvjthqldpwncqszvftbrmjlhg"
        val actual = Day6Util.findMarker(s)
        assertEquals(6, actual)
    }
}

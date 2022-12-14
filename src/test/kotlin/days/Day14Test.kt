package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day14Test {
    @Test
    fun example() {
        val actual = Day14Game(
            """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9""".lines()
        ).play()
        assertEquals(actual, 24)
    }
}

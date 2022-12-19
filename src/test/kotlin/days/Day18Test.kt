package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day18Test {
    val example = """2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5""".lines()

    @Test
    fun examplePart1() {
        val actual = Day18Game(example).solve()
        assertEquals(64, actual)
    }

    @Test
    fun examplePart2() {
        val actual = Day18Game2(example).solve()
        assertEquals(58, actual)
    }
}

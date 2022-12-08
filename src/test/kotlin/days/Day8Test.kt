package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.MutableMatrix
import util.Point

class Day8Test {
    @Test
    fun `the example works`() {
        val lines = """30373
        25512
        65332
        33549
        35390""".trimIndent().lines()
        val matrix = MutableMatrix.fromSingleDigits(lines) { c ->
            c.digitToInt()
        }
        var border = 0
        var middle = 0
        matrix.forEachPoint { p ->
            if (matrix.isOnEdge(p)) {
                border += 1
            } else {
                if (matrix.isVisible(p)) {
                    middle += 1
                }
            }
        }
        assertEquals(16, border)
        assertEquals(5, middle)
    }

    @Test
    fun `isolating the bug`() {
        val lines = """30373
        25512
        65332
        33549
        35390""".trimIndent().lines()
        val matrix = MutableMatrix.fromSingleDigits(lines) { c ->
            c.digitToInt()
        }
        assertEquals(false, matrix.isVisible(Point(2, 2)))
    }
}

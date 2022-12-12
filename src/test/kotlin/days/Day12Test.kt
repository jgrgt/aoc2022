package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import util.MutableMatrix
import util.Point

class Day12Test {
    val lines = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi""".lines()

    @Test
    fun examplePart2() {
        val heights = MutableMatrix.fromSingleDigits(lines) { c ->
            c
        }
        val oldStart = heights.find('S')
        heights.set(oldStart, 'a')
        val allLowest = heights.findAll('a')
        val lengths = allLowest.map { newStart ->
            heights.set(newStart, 'S')
            val shortestPath = heights.map { _ -> heights.height() * heights.width() }
            val game = Day12Game(heights, shortestPath)
            game.run()
            // clean up
            heights.set(newStart, 'a')
            val s = game.shortestPath()
            s
        }
        assertEquals(29, lengths.min())
    }
}

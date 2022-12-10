package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day10Test {
    val instructions = """addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop""".lines().map { Day10Instruction.parse(it) }

    @Test
    fun `it works for the example 20`() {
        val game = Day10Game(instructions)
        repeat(20) {
            game.tick()
        }
        assertEquals(21, game.x)
    }

    @Test
    fun `it works for the example 60`() {
        val game = Day10Game(instructions)
        repeat(60) {
            game.tick()
        }
        assertEquals(19, game.x)
    }

    @Test
    fun `fullgame example test`() {
        val game = Day10Game(instructions)
        var score = 0
        val scoreTicks = listOf(20, 60, 100, 140, 180, 220)
        val scores = listOf(21, 19, 18, 21, 16, 18)
        repeat(220) { n ->
            val tick = n + 1
            game.tick()
            val i = scoreTicks.indexOf(tick)
            if (scoreTicks.contains(tick)) {
                assertEquals(scores[i], game.x)
                score += tick * game.x
            }
        }
        assertEquals(13140, score)
    }

    @Test
    fun `simple 1`() {
        val g = parse(
            """noop
addx 3
addx -5"""
        )
        g.tick()
        assertEquals(1, g.x)
        g.tick()
        assertEquals(1, g.x)
        g.tick()
        assertEquals(4, g.x)
        g.tick()
        assertEquals(4, g.x)
        g.tick()
        assertEquals(-1, g.x)
    }

    fun parse(s: String): Day10Game {
        return Day10Game(s.lines().map {
            Day10Instruction.parse(it)
        })
    }
}

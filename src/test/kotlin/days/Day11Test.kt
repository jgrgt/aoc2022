package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day11Test {
    val exampleInput = """Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1""".lines()

    @Test
    fun `test case part 2`() {
        val monkeys = exampleInput.windowed(7, 7, true)
            .map { monkeyLines -> Day11Monkey.parse(monkeyLines) }
        val game = Day11Game(monkeys)
        repeat(10000) { game.round() }
        assertEquals(52166, game.monkeys[0].itemsInspected)
        assertEquals(47830, game.monkeys[1].itemsInspected)
        assertEquals(1938, game.monkeys[2].itemsInspected)
        assertEquals(52013, game.monkeys[3].itemsInspected)
        val solutionParts = game.monkeys.map { it.itemsInspected }.sortedDescending().take(2)
        val solutionLongs = solutionParts.map { it.toLong() }
        assertEquals(2713310158, solutionLongs[0] * solutionLongs[1])
    }
}

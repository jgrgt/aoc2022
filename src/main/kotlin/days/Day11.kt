package days

class Day11 : Day(11) {

    override fun partOne(): Any {
        val monkeys = inputList.windowed(7, 7, true)
            .map { monkeyLines -> Day11Monkey.parse(monkeyLines) }
        val game = Day11Game(monkeys)
        repeat(20) { game.round() }
        return game.monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce { x, y -> x * y }
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}

class Day11Game(val monkeys: List<Day11Monkey>) {
    fun round() {
        monkeys.forEach {
            this.turn(it)
        }
    }

    private fun turn(monkey: Day11Monkey) {
        val toGive = monkey.items.map { item ->
            monkey.itemsInspected += 1
            val newItem = monkey.operation.invoke(item) / 3
            val toMonkeyIndex = if (newItem % monkey.divTest == 0) {
                monkey.trueMonkeyIndex
            } else {
                monkey.falseMonkeyIndex
            }
            toMonkeyIndex to newItem
        }

        monkey.items.clear()

        toGive.forEach { (toMonkeyIndex, item) ->
            monkeys[toMonkeyIndex].give(item)
        }
    }
}

data class Day11Monkey(
    val items: MutableList<Int>,
    val operation: (Int) -> Int,
    val divTest: Int,
    val trueMonkeyIndex: Int,
    val falseMonkeyIndex: Int
) {
    fun give(newItem: Int) {
        items.add(newItem)
    }

    var itemsInspected = 0

    companion object {
        fun parse(lines: List<String>): Day11Monkey {
            check(lines[0].startsWith("Monkey ")) { "Unexpected input lines" }
//            check(lines[6].trim().isEmpty()) { "Unexpected end line" }
            val items = lines[1].trim().split(":")[1].trim().split(",").map { it.trim().toInt() }
            val operator = lines[2][23]
            val amountString = lines[2].substring(25)
            val operation = when (operator) {
                '*' -> if (amountString == "old") {
                    { i: Int -> i * i }
                } else {
                    { i: Int -> i * amountString.toInt() }
                }

                '+' -> if (amountString == "old") {
                    { i: Int -> i + i }
                } else {
                    { i: Int -> i + amountString.toInt() }
                }

                else -> throw IllegalArgumentException("unknown operator $operator")
            }
            val divTest = lines[3].substring(21).toInt()
            val trueMonkeyIndex = lines[4].substring(29).toInt()
            val falseMonkeyIndex = lines[5].substring(30).toInt()
            return Day11Monkey(items.toMutableList(), operation, divTest, trueMonkeyIndex, falseMonkeyIndex)
        }
    }

}

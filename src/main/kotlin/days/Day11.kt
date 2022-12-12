package days

import java.lang.IllegalStateException
import kotlin.math.sqrt

class Day11 : Day(11) {

    override fun partOne(): Any {
//        return 0
        val monkeys = inputList.windowed(7, 7, true)
            .map { monkeyLines -> Day11Monkey.parse(monkeyLines) }
        val game = Day11Game(monkeys)
        repeat(20) { game.round() }
        return game.monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce { x, y -> x * y }
    }

    override fun partTwo(): Any {
        val monkeys = inputList.windowed(7, 7, true)
            .map { monkeyLines -> Day11Monkey.parse(monkeyLines) }
        val game = Day11Game(monkeys)
        repeat(10000) { game.round() }
        val parts = game.monkeys.map { it.itemsInspected }.sortedDescending().take(2)
        return parts[0].toLong() * parts[1].toLong()
    }
}

// https://tutorialwing.com/kotlin-program-to-find-all-prime-factors-of-given-number/
fun primeFactors(number: Int): List<Int> {
    // Array that contains all the prime factors of given number .
    val arr: ArrayList<Int> = arrayListOf()


    var n = number

    // At first check for divisibility by 2.add it in arr till it is divisible
    while (n % 2 == 0) {
        arr.add(2)
        n /= 2

    }
    val squareRoot = sqrt(n.toDouble()).toInt()
    // Run loop from 3 to square root of n. Check for divisibility by i. Add i in arr till it is divisible by i.
    for (i in 3..squareRoot step 2) {
        while (n % i == 0) {
            arr.add(i)
            n /= i
        }
    }
    // If n is a prime number greater than 2.
    if (n > 2) {
        arr.add(n)
    }
    return arr
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
            val newItem = monkey.operation.invoke(item) // for part 1 / 3
            val toMonkeyIndex = if (newItem.isDivisibleBy(monkey.divTest)) {
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

sealed interface Day11Number {
    operator fun times(i: Day11Number): Day11Number
    operator fun times(i: Int): Day11Number
    operator fun plus(i: Day11Number): Day11Number
    operator fun plus(i: Int): Day11Number
    fun isDivisibleBy(divTest: Int): Boolean
}

class SimpleFactorialNumber(
//    val number: Int,
    val moduli: Map<Int, Int>
) : Day11Number {

    companion object {
        val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 23)

        fun from(i: Int): SimpleFactorialNumber {
//            val factors = primeFactors(i)
            return SimpleFactorialNumber(
//                i,
                primes.associateWith { prime ->
                    i % prime
                }
            )
        }
    }

    override fun times(i: Day11Number): Day11Number {
        // ((ax + m) * (ax + m) % a)
        // == (ax * ax + 2 * ax * m + m * m) % a == (m * m) % a
        val newModuli = moduli.map { (prime, modulo) ->
            prime to ((modulo * modulo) % prime)
        }.toMap()
        return when (i) {
            is SimpleFactorialNumber -> {
                SimpleFactorialNumber(
//                    this.number * i.number,
                    newModuli
                )
            }
        }
    }

    override fun times(i: Int): Day11Number {
        // ((ax + m) * i) % a == (axi + mi) % a == mi % a
        val newModuli = moduli.map { (prime, modulo) ->
            prime to ((modulo * i) % prime)
        }.toMap()
        return SimpleFactorialNumber(
//            number * i,
            newModuli
        )
    }

    override fun plus(i: Day11Number): Day11Number {
        // added to itself - does not happen in puzzle input?
        throw IllegalStateException()

    }

    override fun plus(i: Int): Day11Number {
        val newModuli = moduli.map { (prime, modulo) ->
            prime to ((modulo + i) % prime)
        }.toMap()
        return SimpleFactorialNumber(
//            number + i,
            newModuli
        )
    }

    override fun isDivisibleBy(divTest: Int): Boolean {
//        val numberModulo = number % divTest
        val fastModulo = moduli[divTest]!!
//        if (numberModulo != fastModulo) {
//            throw IllegalStateException()
//        }
        return fastModulo == 0
    }
}

data class Day11Monkey(
    val items: MutableList<Day11Number>,
    val operation: (Day11Number) -> Day11Number,
    val divTest: Int,
    val trueMonkeyIndex: Int,
    val falseMonkeyIndex: Int
) {
    fun give(newItem: Day11Number) {
        items.add(newItem)
    }

    var itemsInspected = 0

    companion object {
        fun parse(lines: List<String>): Day11Monkey {
            check(lines[0].startsWith("Monkey ")) { "Unexpected input lines" }
//            check(lines[6].trim().isEmpty()) { "Unexpected end line" }
            val items =
                lines[1].trim().split(":")[1].trim().split(",").map { SimpleFactorialNumber.from(it.trim().toInt()) }
            val operator = lines[2][23]
            val amountString = lines[2].substring(25)
            val operation = when (operator) {
                '*' -> if (amountString == "old") {
                    { i: Day11Number -> i * i }
                } else {
                    { i: Day11Number -> i * amountString.toInt() }
                }

                '+' -> if (amountString == "old") {
                    { i: Day11Number -> i + i }
                } else {
                    { i: Day11Number -> i + amountString.toInt() }
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

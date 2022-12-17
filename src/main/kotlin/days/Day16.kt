package days

import util.MutableMatrix
import util.Point

class Day16 : Day(16) {

    override fun partOne(): Any {
        return Game16(inputList).solve(null)
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}

data class ValveOpening(val time: Int, val rate: Int) {
    val pressureRelease = (30 - time) * rate
}

data class D16Attempt(val clock: Int, val position: String, val openings: Map<String, ValveOpening>) {
    fun goto(name: String): D16Attempt {
        return D16Attempt(clock + 1, name, openings)
    }

    fun currentValveIsOpen(): Boolean {
        return openings.containsKey(position)
    }

    fun open(valves: Map<String, Day16Valve>): D16Attempt {
        val rate = valves[position]!!.flowRate
        val newClock = clock + 1
        return D16Attempt(newClock, position, openings + (position to ValveOpening(newClock, rate)))
    }

    fun totalScore(): Int {
        return openings.values.sumOf { it.pressureRelease }
    }

    fun maxPossibleScore(valves: Map<String, Day16Valve>): Int {
        return totalScore() + unOpenedValveOpenings(valves)
    }

    private fun unOpenedValveOpenings(valves: Map<String, Day16Valve>): Int {
        val minutesLeft = 30 - clock - 1 // next minute onwards
        val maxOpenings = minutesLeft / 2 // you need to move between openings
        val allValveNames = valves.keys
        val openedValveNames = openings.keys
        val closedValveNames = allValveNames.subtract(openedValveNames)
        val closedRates =
            valves.filter { closedValveNames.contains(it.key) }.values.map { it.flowRate }.sortedDescending()
        return closedRates.take(maxOpenings).zip((maxOpenings downTo 1)).sumOf { (rate, time) -> rate * (time * 2) }
    }
}

class Game16(lines: List<String>) {
    val valves = lines.map { Day16Valve.from(it) }.associateBy { it.name }

    fun solve(valveNameOrder: List<String>?): Int {
        // Plan: first figure out the most efficient ways to go from 1 (useful) valve to another.
        // Then use that to iterate all efficient possibilities and find the best one

        // First part: pathfinding, depth first, no loops
        val valvesWithRatesAndStartValve = valves.filter { it.value.flowRate > 0 || it.key == "AA" }
        val valvesWithRatesAndStartValueNames =
            valvesWithRatesAndStartValve.keys.toList().sorted() // sorted because easier for me
        val pathLengths = MutableMatrix.from(valvesWithRatesAndStartValve.size, valvesWithRatesAndStartValve.size) { 0 }
        // iterate all combinations
        valvesWithRatesAndStartValueNames.forEachIndexed { indexFrom, valveNameFrom ->
            valvesWithRatesAndStartValueNames.forEachIndexed { indexTo, valveNameTo ->
                if (indexFrom < indexTo) {
                    check(valveNameFrom != valveNameTo)
                    val p = Point(indexFrom, indexTo)
                    val current = pathLengths.get(p)
                    check(current == 0)
                    val length = findShortestLength(valveNameFrom, valveNameTo)
                    pathLengths.set(p, length)
                    pathLengths.set(Point(indexTo, indexFrom), length)
                }
            }
        }

        pathLengths.printSep(", ")

        // now we have the lengths, let's find the best combination
        // We always start at AA, so we can make all combinations of all other valve orders and calculate those
        val valveRates = valvesWithRatesAndStartValueNames.map { valves[it]!!.flowRate }
        return if (valveNameOrder == null) {
            val valveIndexes = 1 until valvesWithRatesAndStartValve.size
            println("Finding permutations for $valveIndexes")
            val permutations = allPermutations(valveIndexes.toSet())
            println("Amount of permutations: $permutations")
            permutations.maxOfOrNull { calculateScore(it, valveRates, pathLengths) }!!
        } else {
            val indexes = valveNameOrder.map { valvesWithRatesAndStartValueNames.indexOf(it) }
            println("Indexes are $indexes")
            calculateScore(indexes, valveRates, pathLengths)
        }
    }

    private fun calculateScore(
        valveOrder: List<Int>,
        valveRates: List<Int>,
        pathLengths: MutableMatrix<Int>
    ): Int {
        var minutesLeft = 30
        var valveIndex = 0
        var score = 0
        valveOrder.forEach { nextValveIndex ->
            val pathLength = pathLengths.get(Point(valveIndex, nextValveIndex))
            minutesLeft -= pathLength
            if (minutesLeft <= 0) {
                return score
            }

            minutesLeft -= 1
            valveIndex = nextValveIndex
            score += valveRates[valveIndex] * minutesLeft

            if (minutesLeft <= 0) {
                return score
            }
        }

        return score
    }

    // https://stackoverflow.com/a/63532094
    fun <T> allPermutations(set: Set<T>): Set<List<T>> {
        if (set.isEmpty()) return emptySet()

        fun <T> _allPermutations(list: List<T>): Set<List<T>> {
            if (list.isEmpty()) return setOf(emptyList())

            val result: MutableSet<List<T>> = mutableSetOf()
            for (i in list.indices) {
                _allPermutations(list - list[i]).forEach { item ->
                    result.add(item + list[i])
                }
            }
            return result
        }

        return _allPermutations(set.toList())
    }

    private fun findShortestLength(
        valveNameFrom: String,
        valveNameTo: String,
        visited: List<String> = emptyList()
    ): Int {
        if (valveNameFrom == valveNameTo) {
            return 0
        }

        val currentValve = valves[valveNameFrom]!!
        // below check not needed because of first check above here
        val nextValveNames = currentValve.next
        if (nextValveNames.contains(valveNameTo)) {
            return 1
        }
        // recursion...
        return (nextValveNames
            .filter { !visited.contains(it) }
            .minOfOrNull { findShortestLength(it, valveNameTo, visited + valveNameFrom) } ?: 10000
                ) + 1
    }
}

data class Day16Valve(val name: String, val flowRate: Int, val next: List<String>) {
    companion object {
        fun from(line: String): Day16Valve {
            val parts = line.trim().split(" ", limit = 10)
            val name = parts[1]
            val rate = parts[4].trim(';').split("=")[1].toInt()
            val nextStrings = parts[9].split(", ")
            return Day16Valve(name, rate, nextStrings)
        }
    }
}

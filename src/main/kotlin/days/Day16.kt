package days

import itertools.combinations
import util.MutableMatrix
import util.Point

class Day16 : Day(16) {

    override fun partOne(): Any {
//        return Game16(inputList).solve()
        return 0
    }

    override fun partTwo(): Any {
        return Game16(inputList).solve2()
    }
}

class Game16(lines: List<String>) {
    val valves = lines.map { Day16Valve.from(it) }.associateBy { it.name }

    fun solve(): Int {
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
        val valveRates = valvesWithRatesAndStartValueNames.map { valves[it]!!.flowRate }
//        val nonZeroValveNames = valves.filter { it.value.flowRate > 0 }
//        val valveNameSplits = generateSplits(nonZeroValveNames)
        val size = valvesWithRatesAndStartValueNames.size
        val actualValveIndices = (1..size).toList()
        return (1 until size)
            .flatMap { actualValveIndices.combinations(it) }
            .map { myValveIndices ->
                val elephantValveIndices = actualValveIndices.minus(myValveIndices.toSet())
                // hacky way to make sure
                val myScore =
                    calculateBestScoreRecursive(path = elephantValveIndices + 0, valveRates, pathLengths, timeLeft = 30)
                val elephantScore =
                    calculateBestScoreRecursive(path = myValveIndices + 0, valveRates, pathLengths, timeLeft = 30)
                myScore + elephantScore
            }.min()
    }
//
//    private fun generateSplits(valves: Map<String, Day16Valve>): List<List<String>> {
//        val originalSet = valves.keys
//        return (1 until valves.size).toList().flatMap {
//            originalSet.combinations(it).toList()
//        }
//    }

    private fun calculateBestScoreRecursive(
        path: List<Int>,
        valveRates: List<Int>,
        pathLengths: MutableMatrix<Int>,
        timeLeft: Int
    ): Int {
        check(path.isNotEmpty())
        if (timeLeft <= 0) {
            return 0
        }
        val currentValve = path.last()
        val currentValveScore = timeLeft * valveRates[currentValve]
        val candidates = pathLengths.items.indices.toSet().minus(path.toSet())
        return currentValveScore + (candidates.maxOfOrNull { nextValve ->
            val distance = pathLengths.get(Point(currentValve, nextValve))
            val newTimeLeft = timeLeft - distance - 1 // 1 for opening the valve
            calculateBestScoreRecursive(path + nextValve, valveRates, pathLengths, newTimeLeft)
        } ?: 0)
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

    fun solve2(): Int {
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
        val valveRates = valvesWithRatesAndStartValueNames.map { valves[it]!!.flowRate }
//        val nonZeroValveNames = valves.filter { it.value.flowRate > 0 }
//        val valveNameSplits = generateSplits(nonZeroValveNames)
        val size = valvesWithRatesAndStartValueNames.size
        val actualValveIndices = (1 until size).toList()
        val m = (1 until size)
            .flatMap { actualValveIndices.combinations(it) }
//        println((valvesWithRatesAndStartValueNames.mapIndexed { i, name -> "$i: $name" }).joinToString(", "))
//        val m = listOf(listOf(1, 2, 6))
            .map { myValveIndices ->
                val elephantValveIndices = actualValveIndices.minus(myValveIndices.toSet())
                // hacky way to make sure
                val myScore =
                    calculateBestScoreRecursive(path = elephantValveIndices + 0, valveRates, pathLengths, timeLeft = 26)
                val elephantScore =
                    calculateBestScoreRecursive(path = myValveIndices + 0, valveRates, pathLengths, timeLeft = 26)
                (myScore + elephantScore) to myValveIndices
            }.maxBy { it.first }

        println("Score ${m.first}: ${m.second.map { valvesWithRatesAndStartValueNames[it] }}")
        return m.first
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

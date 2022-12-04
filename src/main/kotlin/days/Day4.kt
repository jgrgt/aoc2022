package days

import days.Day4Util.toRange

class Day4 : Day(4) {

    override fun partOne(): Any {
        return inputList
            .map { line ->
                val (firstRangeString, secondRangeString) = line.split(",")
                val firstRange = toRange(firstRangeString)
                val secondRange = toRange(secondRangeString)
                if (firstRange.fullyContains(secondRange) || secondRange.fullyContains(firstRange)) {
                    1
                } else {
                    0
                }
            }.sum()

    }

    override fun partTwo(): Any {
        return inputList
            .map { line ->
                val (firstRangeString, secondRangeString) = line.split(",")
                val firstRange = toRange(firstRangeString)
                val secondRange = toRange(secondRangeString)
                if (firstRange.hasOverlap(secondRange)) {
                    1
                } else {
                    0
                }
            }.sum()
    }
}

private fun IntRange.hasOverlap(secondRange: IntRange): Boolean {
    // N^2 complexity, but let's try it
    return this.any { firstRangeElement ->
        secondRange.contains(firstRangeElement)
    }
}

private fun IntRange.fullyContains(secondRange: IntRange): Boolean {
    return this.first <= secondRange.first && secondRange.last <= this.last
}

object Day4Util {
    fun toRange(s: String): IntRange {
        val (from, to) = s.split("-")
        return IntRange(from.toInt(), to.toInt())
    }
}

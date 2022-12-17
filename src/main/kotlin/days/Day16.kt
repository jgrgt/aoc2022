package days

class Day16 : Day(1) {

    override fun partOne(): Any {
        return "TODO"

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

    fun solve(): Int {
        var paths = listOf(D16Attempt(0, "AA", emptyMap()))

        repeat(30) { zeroMinute ->
            paths = paths.flatMap { attempt ->
                val valve = valves[attempt.position]!!
                val next = valve.next
                // we could open the current valve, if unopened or we could go to the next valve
                // just go to next
                val goToNextAttempts = next.map { attempt.goto(it) }
                // open this valve
                val openValveAttempts = if (attempt.currentValveIsOpen()) {
                    emptyList()
                } else if (valve.flowRate == 0) {
                    emptyList()
                } else {
                    listOf(attempt.open(valves))
                }
                val allNextAttempts = goToNextAttempts + openValveAttempts
                // Filter out any attempts with a max possible score lower than any current total score...
                val totalScores = allNextAttempts.map { it.totalScore() }
                val currentMaxpScore = totalScores.max()
                allNextAttempts.filter { it.maxPossibleScore(valves) >= currentMaxpScore }
            }
        }
        // we need to walk over this valve grid and find an optimal path...
        return paths.map { it.totalScore() }.max()
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

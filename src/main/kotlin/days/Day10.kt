package days

class Day10 : Day(10) {

    override fun partOne(): Any {
        val instructions = inputList.map {
            Day10Instruction.parse(it)
        }
        val game = Day10Game(instructions)
        var score = 0
        val scoreTicks = listOf(20, 60, 100, 140, 180, 220)
        repeat(220) { n ->
            val tick = n + 1
            game.tick()
            if (scoreTicks.contains(tick)) {
                score += tick * game.x
            }
        }
        return score
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}

sealed class Day10Instruction {
    companion object {
        fun parse(l: String): Day10Instruction {
            return if (l.startsWith("addx")) {
                val amount = l.split(" ")[1].toInt()
                Addx(amount)
            } else if (l.startsWith("noop")) {
                Noop
            } else {
                throw IllegalArgumentException("Unknown input $l")
            }
        }
    }
}

object Noop : Day10Instruction() {
    override fun toString(): String {
        return "noop"
    }
}
class Addx(val amount: Int) : Day10Instruction() {
    override fun toString(): String {
        return "addx $amount"
    }
}

class Day10Game(instructions: List<Day10Instruction>) {
    //    var clock = 0
    var x = 1
    var ip = 0 // instruction pointer
    var toAdd: Int? = null

    var updatedInstructions = instructions.flatMap {
        when (it) {
            is Addx -> listOf(Noop, it)
            Noop -> listOf(Noop)
        }
    }

    fun tick() {
        if (toAdd != null) {
            x += toAdd!! // single threaded
            toAdd = null
        }
        val instruction = updatedInstructions[ip]
        ip += 1
        when (instruction) {
            is Addx -> {
                toAdd = instruction.amount
            }
            Noop -> {}
        }
    }
}

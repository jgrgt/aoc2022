package days

class Day5 : Day(5) {

    override fun partOne(): Any {
        val stackInput = inputList.subList(0, 8)
        check(stackInput[7].startsWith("[")) { "probably wrong stack" }
        val game = Day5Game.fromStackInput(stackInput)
        inputList.drop(10).forEach { line ->
            val move = Move.parse(line)
            game.execute(move)
        }
        return game.topCrates().joinToString("")

    }

    override fun partTwo(): Any {
        val stackInput = inputList.subList(0, 8)
        check(stackInput[7].startsWith("[")) { "probably wrong stack" }
        val game = Day5Game.fromStackInput(stackInput)
        inputList.drop(10).forEach { line ->
            val move = Move.parse(line)
            game.execute2(move)
        }
        return game.topCrates().joinToString("")
    }
}

typealias Stack = MutableList<Char>

data class Move(val amount: Int, val fromIndex: Int, val toIndex: Int) {
    companion object {
        fun parse(s: String): Move {
            // move 3 from 5 to 2
            val parts = s.split(" ")
            val amount = parts[1].toInt()
            val fromIndex = parts[3].toInt()
            val toIndex = parts[5].toInt()
            return Move(amount, fromIndex, toIndex)
        }
    }
}

class Day5Game(private val stacks: List<Stack>) {
    fun execute(move: Move) {
        val fromStack = stacks[move.fromIndex - 1]
        val toStack = stacks[move.toIndex - 1]
        repeat(move.amount) {
            val v = fromStack.removeLast()
            toStack.add(v)
        }
    }

    fun execute2(move: Move) {
        val fromStack = stacks[move.fromIndex - 1]
        val toStack = stacks[move.toIndex - 1]
        val moved = mutableListOf<Char>()
        repeat(move.amount) {
            val v = fromStack.removeLast()
            moved.add(v)
        }
        toStack.addAll(moved.reversed())
    }

    fun topCrates(): List<Char> {
        return stacks.map { it.last() }
    }

    companion object {
        fun fromStackInput(lines: List<String>): Day5Game {
            // letters are on indexes:
            // [B] [L] [Q] [W] [S] [L] [J] [W] [Z]
            //  1   5   9  13  17  21  25  29  34
            val stacks: List<Stack> = listOf(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
            )
            val indexes = listOf(1, 5, 9, 13, 17, 21, 25, 29, 33)

            lines.forEach { line ->
                indexes.forEachIndexed { stackIndex, charIndex ->
                    val c = line[charIndex]
                    if (c != ' ') {
                        stacks[stackIndex].add(0, c)
                    }
                }
            }
            return Day5Game(stacks)
        }
    }

}

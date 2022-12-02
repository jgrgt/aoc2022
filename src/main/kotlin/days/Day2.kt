package days

enum class Hand {
    ROCK, PAPER, SCISSORS;

    companion object {
        fun from(l: String): Hand {
            return when (l) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                "X" -> ROCK
                "Y" -> PAPER
                "Z" -> SCISSORS
                else -> throw IllegalArgumentException("Unknown hand $l")
            }
        }
    }
}

enum class Instruction {
    LOSE, DRAW, WIN;

    companion object {
        fun from(l: String): Instruction {
            return when (l) {
                "X" -> LOSE
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw IllegalArgumentException("Unknown instruction $l")
            }
        }
    }
}

class Game2(val opponent: Hand, val instruction: Instruction) {
    private fun handScore(h: Hand): Int {
        return when (h) {
            Hand.ROCK -> 1
            Hand.PAPER -> 2
            Hand.SCISSORS -> 3
        }
    }

    fun score(): Int {
        val myHand = when (instruction) {
            Instruction.LOSE -> loseHand(opponent)
            Instruction.DRAW -> drawHand(opponent)
            Instruction.WIN -> winHand(opponent)
        }
        return handScore(myHand) + instructionScore(instruction)
    }

    private fun instructionScore(instruction: Instruction): Int {
        return when(instruction) {
            Instruction.LOSE -> 0
            Instruction.DRAW -> 3
            Instruction.WIN -> 6
        }
    }

    fun loseHand(h: Hand) : Hand{
        return when(h) {
            Hand.ROCK -> Hand.SCISSORS
            Hand.PAPER -> Hand.ROCK
            Hand.SCISSORS -> Hand.PAPER
        }
    }

    fun winHand(h: Hand) : Hand{
        return when(h) {
            Hand.ROCK -> Hand.PAPER
            Hand.PAPER -> Hand.SCISSORS
            Hand.SCISSORS -> Hand.ROCK
        }
    }

    fun drawHand(h: Hand) : Hand{
        return h
    }
}

class Game1(val opponent: Hand, val mine: Hand) {
    val loss = 0
    val draw = 3
    val win = 6

    private fun handScore(h: Hand): Int {
        return when (h) {
            Hand.ROCK -> 1
            Hand.PAPER -> 2
            Hand.SCISSORS -> 3
        }
    }

    fun score(): Int {
        return handScore(mine) + winScore()
    }

    private fun winScore(): Int {
        return when (opponent) {
            Hand.ROCK -> when (mine) {
                Hand.ROCK -> draw
                Hand.PAPER -> win
                Hand.SCISSORS -> loss
            }

            Hand.PAPER -> when (mine) {
                Hand.ROCK -> loss
                Hand.PAPER -> draw
                Hand.SCISSORS -> win
            }

            Hand.SCISSORS -> when (mine) {
                Hand.ROCK -> win
                Hand.PAPER -> loss
                Hand.SCISSORS -> draw
            }
        }
    }
}

class Day2 : Day(2) {
    override fun partOne(): Any {
        return doPartOne(inputList)
    }

    override fun partTwo(): Any {
        return doPartTwo(inputList)
    }

    fun doPartOne(inputList: List<String>): Int {
        return inputList.sumOf { l ->
            val letters = l.split(" ")
            Game1(
                Hand.from(letters[0]),
                Hand.from(letters[1])
            ).score()
        }
    }

    fun doPartTwo(inputList: List<String>): Int {
        return inputList.sumOf { l ->
            val letters = l.split(" ")
            Game2(
                Hand.from(letters[0]),
                Instruction.from(letters[1])
            ).score()
        }
    }
}

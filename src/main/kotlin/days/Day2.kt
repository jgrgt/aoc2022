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

class Game(val opponent: Hand, val mine: Hand) {
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
        return "TODO"
    }

    fun doPartOne(inputList: List<String>): Int {
        return inputList.sumOf { l ->
            val letters = l.split(" ")
            Game(
                Hand.from(letters[0]),
                Hand.from(letters[1])
            ).score()
        }
    }
}

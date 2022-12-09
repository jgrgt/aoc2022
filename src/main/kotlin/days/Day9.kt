package days

import util.Point as Point

class Day9 : Day(9) {

    override fun partOne(): Any {
        val game = Day9Game()
        inputList.forEach { l ->
            val move = Day9Move.parse(l)
            game.move(move)
        }
        return game.tailPositions.size
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT;
}

data class Day9Move(val direction: Direction, val steps: Int) {
    companion object {
        fun parse(l: String): Day9Move {
            val (d, s) = l.trim().split(" ")
            val direction = when (d) {
                "D" -> Direction.DOWN
                "U" -> Direction.UP
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> throw IllegalArgumentException("Unknown direction $d")
            }
            return Day9Move(direction, s.toInt())
        }
    }
}

class Day9Game() {
    var head = Point(0, 0)
    var tail = Point(0, 0)
    var tailPositions = mutableSetOf(tail)

    fun move(move: Day9Move) {
        repeat(move.steps) {
            moveSingle(move.direction)
        }
    }

    private fun moveSingle(direction: Direction) {
        head = when (direction) {
            Direction.UP -> head.up()
            Direction.DOWN -> head.down()
            Direction.LEFT -> head.left()
            Direction.RIGHT -> head.right()
        }
        adjustTail()
        logTail()
    }

    private fun logTail() {
        tailPositions.add(tail)
    }

    private fun adjustTail() {
        when (head - tail) {
            //     2  3  4
            // 16           6
            // 15     X     7
            // 14           8
            //    12 11 10
            Point(0, 0) -> {}// do nothing
            Point(1, 0) -> {}
            Point(0, 1) -> {}
            Point(-1, 0) -> {}
            Point(0, -1) -> {}
            Point(1, 1) -> {}
            Point(-1, 1) -> {}
            Point(1, -1) -> {}
            Point(-1, -1) -> {}

            // Down
            Point(2, -1) -> {
                tail += Point(1, -1)
            }

            Point(2, 0) -> {
                tail = tail.down() // x + 1
            }

            Point(2, 1) -> {
                tail += Point(1, 1)
            }

            // Up
            Point(-2, -1) -> {
                tail += Point(-1, -1)
            }

            Point(-2, 0) -> {
                tail = tail.up() // x - 1
            }

            Point(-2, 1) -> {
                tail += Point(-1, 1)
            }

            // Right
            Point(-1, 2) -> {
                tail += Point(-1, 1)
            }

            Point(0, 2) -> {
                tail = tail.right() // y + 1
            }

            Point(1, 2) -> {
                tail += Point(1, 1)
            }

            // Left
            Point(-1, -2) -> {
                tail += Point(-1, -1)
            }

            Point(0, -2) -> {
                tail = tail.left() // y - 1
            }

            Point(1, -2) -> {
                tail += Point(1, -1)
            }

            else -> {
                throw IllegalStateException("Unexpected head/tail position: $head $tail")
            }
        }
    }
}

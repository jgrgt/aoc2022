package days

import util.MutableMatrix
import util.Point

class Day12 : Day(12) {

    override fun partOne(): Any {
        val heights = MutableMatrix.fromSingleDigits(inputList) { c ->
            c
        }
        val shortestPath = heights.map { _ -> heights.height() * heights.width() }
        val start = heights.find('S')
        shortestPath.set(start, 0)
        val game = Day12Game(heights, shortestPath)
        game.run(start)
        return game.shortestPath()
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}

class Day12Game(val heights: MutableMatrix<Char>, val shortestPath: MutableMatrix<Int>) {
    val start = 'S'
    val end = 'E'
    val heighest = 'z'
    val lowest = 'a'
    fun run(start: Point) {
        val here = heights.get(start)
        val currentPathLength = shortestPath.get(start)
        val moves = start.cross()
        if (here == end) {
            // we reached the destination
            return
        }

        val nextHeight = nextHeight(here)
        val nextPathLength = currentPathLength + 1
        val validMoves = moves.filter { move ->
            var h = heights.getOrDefault(move)
            if (h == end) {
                h = heighest  // E == z
            }
            h != null && h <= nextHeight
        }
        validMoves.forEach { move ->
            val currentBestPathLengthForMove = shortestPath.get(move)
            if (nextPathLength < currentBestPathLengthForMove) {
                shortestPath.set(move, nextPathLength)
            }
        }
        // Go deeper
        runAll(validMoves)
    }

    private fun runAll(validMoves: List<Point>) {
        validMoves.forEach { move -> run(move) }
    }

    private fun nextHeight(here: Char): Char {
        if (here == start) {
            return lowest
        } else if (here == heighest) {
            // this will break for 'E' :(
            return heighest
        }

        return here + 1
    }

    fun shortestPath(): Int {
        val endPoint = heights.find(end)
        return shortestPath.get(endPoint)
    }
}

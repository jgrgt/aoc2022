package days

import util.MutableMatrix
import util.Point

class Day12 : Day(12) {

    override fun partOne(): Any {
        val heights = MutableMatrix.fromSingleDigits(inputList) { c ->
            c
        }
        val shortestPath = heights.map { _ -> heights.height() * heights.width() }
        val game = Day12Game(heights, shortestPath)
        game.run()
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

    fun propagate(start: Point) : List<Point> {
        val here = heights.get(start)
        val currentPathLength = shortestPath.get(start)
        val moves = start.cross()
        if (here == end) {
            // we reached the destination
            return emptyList()
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
        return validMoves
    }

    fun run() {
        val start = heights.find('S')
        shortestPath.set(start, 0)
        run(start)
    }

    fun run(start: Point) {
        val candidates = mutableListOf(start)
        val safety = heights.width() * heights.height()
        var i = 0
        while (candidates.isNotEmpty() && i < safety) {
            i++

            val newCandidates = candidates.flatMap {candidate ->
                propagate(candidate)
            }
            candidates.clear()
            candidates.addAll(newCandidates.toSet())
        }
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

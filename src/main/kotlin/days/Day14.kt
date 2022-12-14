package days

import util.Line
import util.MutableMatrix
import util.Point

class Day14Game2(input: List<String>) {
    val lines = input.flatMap { line ->
        line.split("->").map { it.trim() }.map { pointString ->
            val (x, y) = pointString.split(",").map { it.trim().toInt() }
            Point(x, y)
        }.windowed(2, 1, false).map { pointList ->
            check(pointList.size == 2) { "Expected 2 points" }
            Line(pointList[0], pointList[1])
        }
    }
    val points = lines.flatMap { listOf(it.from, it.to) }
    val maxY = points.maxBy { it.y }.y
    val maxX = points.maxBy { it.x }.x
    val minX = points.minBy { it.x }.x
    val margin = 200
    var start = Point(500, 0)
    var matrix = MutableMatrix.from(maxX + margin, maxY + margin) { _ -> '.' }

    companion object {
        val free = '.'
        val sandChar = 'O'
    }

    fun play(): Int {
        lines.forEach { Day14Util.draw(matrix, it) }
        val bottom = Line(Point(0, maxY + 2), Point(matrix.height() - 1, maxY + 2))
        Day14Util.draw(matrix, bottom)
        val startChar = 'x'
        matrix.set(Point(500, 0), startChar)
        matrix.dropX(minX - margin)
        matrix = matrix.transpose()
        start = matrix.find(startChar)
        val amountDropped = dropSands()
        print()
        return amountDropped
    }

    private fun print() {
        matrix.printSep("") { false }
    }

    private fun dropSands(): Int {
        // create a new sand item
        val safety = matrix.width() * matrix.height()
        var dropped = 0
        var isOverFlowing = false
        while (!isOverFlowing && dropped < safety) {
            dropped++
            isOverFlowing = dropSand()
//            println("Iteration $dropped")
//            print()
        }
        return dropped

    }

    private fun dropSand(): Boolean {
        var sand = nextSandPosition(start) // avoid 'free' conflicts, should be enough room anyway

        while (sand != null && !isAtStartPosition(sand)) {
            val nextSand = nextSandPosition(sand)
            if (nextSand == null) {
                // cannot go further
                matrix.set(sand, sandChar)
                break
            } else {
                sand = nextSand
            }
        }

        return sand == null // ugly, but this will work
    }

    private fun nextSandPosition(sand: Point): Point? {
        val down = sand.down()
        return if (isFree(down)) {
            down
        } else if (isFree(down.left())) {
            down.left()
        } else if (isFree(down.right())) {
            down.right()
        } else {
            null
        }
    }

    private fun canDrop(sand: Point): Boolean {
        return isFree(sand.down()) || isFree(sand.down().left()) || isFree(sand.down().right())
    }

    private fun isFree(p: Point): Boolean {
        return matrix.get(p) == free
    }

    private fun isAtStartPosition(sand: Point): Boolean {
        return sand == start
    }
}

class Day14Game(input: List<String>) {
    val lines = input.flatMap { line ->
        line.split("->").map { it.trim() }.map { pointString ->
            val (x, y) = pointString.split(",").map { it.trim().toInt() }
            Point(x, y)
        }.windowed(2, 1, false).map { pointList ->
            check(pointList.size == 2) { "Expected 2 points" }
            Line(pointList[0], pointList[1])
        }
    }
    val points = lines.flatMap { listOf(it.from, it.to) }
    val maxY = points.maxBy { it.y }.y
    val maxX = points.maxBy { it.x }.x
    val minX = points.minBy { it.x }.x
    val margin = 2
    var start = Point(500, 0)
    var matrix = MutableMatrix.from(maxX + margin, maxY + margin) { _ -> '.' }

    companion object {
        val free = '.'
        val sandChar = 'O'
    }

    fun play(): Int {
        lines.forEach { Day14Util.draw(matrix, it) }
        val startChar = 'x'
        matrix.set(Point(500, 0), startChar)
        matrix.dropX(minX - margin)
        matrix = matrix.transpose()
        start = matrix.find(startChar)
        val amountDropped = dropSands()
        print()
        return amountDropped
    }

    private fun print() {
        matrix.printSep("") { false }
    }

    private fun dropSands(): Int {
        // create a new sand item
        val safety = matrix.width() * matrix.height()
        var dropped = 0
        var isOverFlowing = false
        while (!isOverFlowing && dropped < safety) {
            dropped++
            isOverFlowing = dropSand()
//            print()
        }
        return dropped - 1

    }

    private fun dropSand(): Boolean {
        var sand = start.down() // avoid 'free' conflicts, should be enough room anyway

        while (!isOverFlowing(sand)) {
            val nextSand = nextSandPosition(sand)
            if (nextSand == null) {
                // cannot go further
                matrix.set(sand, sandChar)
                break
            } else {
                sand = nextSand
            }
        }

        return isOverFlowing(sand)
    }

    private fun nextSandPosition(sand: Point): Point? {
        val down = sand.down()
        return if (isFree(down)) {
            down
        } else if (isFree(down.left())) {
            down.left()
        } else if (isFree(down.right())) {
            down.right()
        } else {
            null
        }
    }

    private fun canDrop(sand: Point): Boolean {
        return isFree(sand.down()) || isFree(sand.down().left()) || isFree(sand.down().right())
    }

    private fun isFree(p: Point): Boolean {
        return matrix.get(p) == free
    }

    private fun isOverFlowing(sand: Point): Boolean {
        return matrix.isOnEdge(sand)
    }
}

class Day14 : Day(14) {

    override fun partOne(): Any {
//        return Day14Game(inputList).play()
        return 0
    }

    override fun partTwo(): Any {
        return Day14Game2(inputList).play()
    }
}

object Day14Util {
    fun draw(matrix: MutableMatrix<Char>, l: Line) {
        l.points().forEach { p ->
            matrix.set(p, '#')
        }
    }
}

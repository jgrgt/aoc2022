package days

import util.MutableMatrix
import util.Point

class Day8 : Day(8) {

    override fun partOne(): Any {
        val matrix = MutableMatrix.fromSingleDigits(inputList) { c ->
            c.digitToInt()
        }
        var count = 0
        matrix.forEachPoint { p ->
            if (matrix.isOnEdge(p)) {
                count += 1
            } else {
                if (matrix.isVisible(p)) {
                    count += 1
                }
            }
        }
        return count
    }

    override fun partTwo(): Any {
        val matrix = MutableMatrix.fromSingleDigits(inputList) { c ->
            c.digitToInt()
        }
        var bestScore = 0
        matrix.forEachPoint { p ->
            val score = matrix.viewScore(p)
            if (score > bestScore) {
                bestScore = score
            }
        }
        return bestScore
    }
}

private fun MutableMatrix<Int>.viewScore(p: Point): Int {
    val height = this.get(p)
    val valuesUp = this.valuesList(p) { it.up() }
    val valuesDown = this.valuesList(p) { it.down() }
    val valuesLeft = this.valuesList(p) { it.left() }
    val valuesRight = this.valuesList(p) { it.right() }
    return viewDistance(valuesUp, height) *
            viewDistance(valuesDown, height) *
            viewDistance(valuesLeft, height) *
            viewDistance(valuesRight, height)
}

private fun viewDistance(heights: List<Int>, height: Int): Int {
    var d = heights.takeWhile { h -> h < height }.size
    if (d < heights.size) {
        d += 1 // count the higher tree
    }
    return d
}

fun MutableMatrix<Int>.isVisible(p: Point): Boolean {
    val pointValue = this.get(p)
    val valuesUp = this.valuesList(p) { it.up() }
    val valuesDown = this.valuesList(p) { it.down() }
    val valuesLeft = this.valuesList(p) { it.left() }
    val valuesRight = this.valuesList(p) { it.right() }
    return valuesLeft.all { it < pointValue } ||
            valuesRight.all { it < pointValue } ||
            valuesDown.all { it < pointValue } ||
            valuesUp.all { it < pointValue }
}

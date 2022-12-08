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
        return "TODO"
    }
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

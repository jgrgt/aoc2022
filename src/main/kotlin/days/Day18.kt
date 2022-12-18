package days

class Day18 : Day(18) {

    override fun partOne(): Any {
        return Day18Game(inputList).solve()
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}

class Day18Game(val lines: List<String>) {
    fun solve(): Any {
        val points = lines.map { Point3.from(it) }.toSet()
        return points.sumOf { point ->
            point.sides().count { sidePoint -> !points.contains(sidePoint) }
        }
    }

}

data class Point3(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun from(line: String): Point3 {
            val (x, y, z) = line.trim().split(",").map { it.toInt() }
            return Point3(x, y, z)
        }
    }

    fun sides(): List<Point3> {
        return listOf(
            Point3(x + 1, y, z),
            Point3(x - 1, y, z),
            Point3(x, y + 1, z),
            Point3(x, y - 1, z),
            Point3(x, y, z + 1),
            Point3(x, y, z - 1),
        )
    }
}

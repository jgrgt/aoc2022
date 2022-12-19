package days

class Day18 : Day(18) {

    override fun partOne(): Any {
        return Day18Game(inputList).solve()
    }

    override fun partTwo(): Any {
        return Day18Game2(inputList).solve()
    }
}

class Day18Game(val lines: List<String>) {
    fun solve(): Any {
        val points = lines.map { Point3.from(it) }.toSet()
        return points.sumOf { point ->
            val c = point.sides().count { sidePoint -> !points.contains(sidePoint) }
            println("Count for $point: $c")
            c

        }
    }

}

class Day18Game2(val lines: List<String>) {
    val surface = "X"
    val empty = "."
    val water = "O"
    val floodStart = Point3(0, 0, 0)
    fun solve(): Any {
        val points = lines.map { Point3.from(it) }.toSet()
        val space = MutableSpace.from(points) { p ->
            if (points.contains(p)) {
                surface
            } else {
                empty
            }
        }
        check(!points.contains(floodStart))
        flood(space)
        val emptyRemaining = space.values().count { it == empty }
        println("Empty space points remaining: $emptyRemaining")
        return points.sumOf { point ->
            val c = point.sides().count { sidePoint ->
                val value = space.getOrDefault(sidePoint, water)
                println("Value for $sidePoint is $value")
                value == water
            }
            println("Count for $point: $c")
            c
        }
    }

    private fun flood(space: MutableSpace<String>) {
        var i = 0
        var horizon = setOf(floodStart)
        while (horizon.isNotEmpty() && i < 1_000_000) {
            i++
            horizon = expandFlood(space, horizon)
        }
    }

    private fun expandFlood(space: MutableSpace<String>, horizon: Set<Point3>): Set<Point3> {
        horizon.forEach { p ->
            space.set(p, water)
        }

        return horizon.flatMap { p ->
            p.sides().filter { np ->
                val current = space.getOrDefault(np, water)
                current == empty
            }
        }.toSet()
    }
}

class MutableSpace<T>(private val items: List<List<MutableList<T>>>) {
    companion object {
        fun <T> from(points: Collection<Point3>, init: (Point3) -> T): MutableSpace<T> {
            val maxX = points.maxBy { it.x }.x
            val maxY = points.maxBy { it.y }.y
            val maxZ = points.maxBy { it.z }.z

            val items = List(maxX + 1) { x ->
                List(maxY + 1) { y ->
                    (0..maxZ).map { z ->
                        init.invoke(Point3(x, y, z))
                    }.toMutableList()
                }
            }
            return MutableSpace(items)
        }
    }

    fun get(p: Point3): T {
        return items[p.x][p.y][p.z]
    }

    fun set(p: Point3, value: T) {
        items[p.x][p.y][p.z] = value
    }

    fun getOrDefault(p: Point3, default: T?): T? {
        return if (p.x < 0 || p.x >= items.size) {
            default
        } else if (p.y < 0 || p.y >= items[0].size) {
            default
        } else if (p.z < 0 || p.z >= items[0][0].size) {
            default
        } else {
            get(p)
        }
    }

    fun values(): List<T> {
        return items.flatMap { it.flatten() }
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

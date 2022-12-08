package util

data class MutableMatrix<T>(
    val items: MutableList<MutableList<T>>
) {
    companion object {
        fun <T> fromCommaLines(lines: List<String>, splitter: String = ",", creator: (String) -> T): MutableMatrix<T> {
            return MutableMatrix(
                lines.map { line ->
                    val items = line.split(splitter).map { creator.invoke(it.trim()) }
                    items.toMutableList()
                }.toMutableList()
            )
        }

        fun <T> fromSingleDigits(
            lines: List<String>,
            creator: (Char) -> T
        ): MutableMatrix<T> {
            return MutableMatrix(
                lines.map { line ->
                    val items = line.trim().toCharArray().map { creator.invoke(it) }
                    items.toMutableList()
                }.toMutableList()
            )
        }
    }

    /**
     * Create a NEW matrix, with each element passed through the argument mapper
     */
    fun <R> map(mapper: (T) -> R): MutableMatrix<R> {
        return MutableMatrix(
            items.map { row ->
                row.map(mapper).toMutableList()
            }.toMutableList()
        )
    }

    fun set(p: Point, value: T) {
        items[p.x][p.y] = value
    }

    fun setWrapping(p: Point, value: T) {
        val wrapped = this.wrap(p)
        items[wrapped.x][wrapped.y] = value
    }

    private fun wrap(p: Point): Point {
        val x = p.x % height()
        val y = p.y % width()
        return Point(x, y)
    }

    fun get(p: Point): T {
        return items[p.x][p.y]
    }

    /**
     * Like get, but wraps around...
     */
    fun getWrapping(p: Point): T {
        val wrapped = this.wrap(p)
        return items[wrapped.x][wrapped.y]
    }

    fun getOrDefault(point: Point, default: T? = null): T? {
        return if (point.x < 0 || point.y < 0 || point.x >= items.size || point.y >= items[0].size) {
            default
        } else {
            return items[point.x][point.y]
        }
    }

    /**
     *   y y y
     * x
     * x
     * x
     */
    fun forEachPoint(consumer: (Point) -> Unit) {
        (0 until items.size).forEach { x ->
            (0 until items[0].size).forEach { y ->
                consumer.invoke(Point(x, y))
            }
        }
    }

    fun clone(): MutableMatrix<T> {
        return MutableMatrix(
            items.map {
                it.toCollection(mutableListOf())
            }.toMutableList()
        )
    }

    fun setAll(value: T) {
        forEachPoint { p ->
            set(p, value)
        }
    }

    fun max(): Point {
        return Point(items.size - 1, items[0].size - 1)
    }

    fun contains(point: Point): Boolean {
        return !(point.x < 0 || point.y < 0 || point.x >= items.size || point.y >= items[0].size)
    }

    fun printSep(sep: String, hightlight: (Point) -> Boolean) {
        // Everything after this is in red
        val red = "\u001b[31m"

        // Resets previous color codes
        val reset = "\u001b[0m"
        items.forEachIndexed { x, row ->
            val line = row.mapIndexed { y, value ->
                if (hightlight(Point(x, y))) {
                    red + value + reset
                } else {
                    value.toString()
                }
            }.joinToString(sep)
            println(line)
        }
    }

    fun print(hightlight: (Point) -> Boolean) {
        printSep(", ", hightlight)
    }

    fun printTransformed(mapper: (T) -> String) {
        items.forEach { row ->
            val line = row.joinToString("") { value -> mapper.invoke(value) }
            println(line)
        }
    }

    fun width(): Int {
        return items[0].size
    }

    fun height(): Int {
        return items.size
    }

    /**
     * Calls the consumer with the values in a 3x3 matrix around the argument point.
     */
    fun window(p: Point, consumer: (MutableMatrix<T>) -> Unit) {
        consumer.invoke(
            MutableMatrix(
                mutableListOf(
                    mutableListOf(get(p.up().left()), get(p.up()), get(p.up().right())),
                    mutableListOf(get(p.left()), get(p), get(p.right())),
                    mutableListOf(get(p.down().left()), get(p.down()), get(p.down().right())),
                )
            )
        )
    }

    /**
     * Calls the consumer with the values in a 3x3 matrix around the argument point.
     */
    fun windowOrDefault(p: Point, default: T, consumer: (MutableMatrix<T>) -> Unit) {
        fun g(p: Point) = getOrDefault(p, default)!!
        consumer.invoke(
            MutableMatrix(
                mutableListOf(
                    mutableListOf(g(p.up().left()), g(p.up()), g(p.up().right())),
                    mutableListOf(g(p.left()), g(p), g(p.right())),
                    mutableListOf(g(p.down().left()), g(p.down()), g(p.down().right())),
                )
            )
        )
    }

    fun isOnEdge(p: Point): Boolean {
        return p.x == 0 || p.y == 0 || p.x == items.size - 1 || p.y == items[0].size - 1
    }

    fun isNotOnEdge(p: Point): Boolean {
        return !isOnEdge(p)
    }

    /**
     * Surrounds this matrix with the argument elements. Single surround line. If you want multiple,
     * call this method multiple times.
     */
    fun surround(value: T): MutableMatrix<T> {
        val blankRow = List(width() + 2) { value }
        val newItems = listOf(blankRow) + items.map {
            listOf(value) + it + listOf(value)
        } + listOf(blankRow)
        return MutableMatrix(newItems.map { it.toMutableList() }.toMutableList())
    }

    fun count(check: (T) -> Boolean): Int {
        var count = 0
        forEachPoint { p ->
            if (check(get(p))) {
                count += 1
            }
        }
        return count
    }

    /**
     * String 1 border from the matrix, returns a new matrix
     */
    fun unsurround(): MutableMatrix<T> {
        return MutableMatrix(
            items.slice(1 until items.size - 1)
                .map { row ->
                    row.slice(1 until row.size - 1).toMutableList()
                }
                .toMutableList()
        )
    }

    override fun toString(): String {
        return items.map { row ->
            row.joinToString { it.toString() }
        }.joinToString("\n")
    }
}

data class Point(val x: Int, val y: Int) {

    fun around(): List<Point> {
        return listOf(
            Point(x - 1, y + 1),
            Point(x - 1, y),
            Point(x - 1, y - 1),
            Point(x, y + 1),
            Point(x, y - 1),
            Point(x + 1, y + 1),
            Point(x + 1, y),
            Point(x + 1, y - 1),
        )
    }

    fun up(): Point {
        return Point(x - 1, y)
    }

    fun down(): Point {
        return Point(x + 1, y)
    }

    fun left(): Point {
        return Point(x, y - 1)
    }

    fun right(): Point {
        return Point(x, y + 1)
    }

    fun cross(): List<Point> {
        return listOf(up(), down(), left(), right())
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

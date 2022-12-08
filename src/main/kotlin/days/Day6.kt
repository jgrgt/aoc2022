package days

class Day6 : Day(6) {

    override fun partOne(): Any {
        return Day6Util.findMarker(inputString)

    }

    override fun partTwo(): Any {
        return Day6Util.findMarker2(inputString)
    }
}

object Day6Util {
    fun findMarker(s: String): Int {
        check(s.length >= 4) { throw IllegalArgumentException("String too short!") }
        var m1 = s[0]
        var m2 = s[1]
        var m3 = s[2]

        s.drop(3).forEachIndexed { i, c ->
            if (m1 != m2 && m2 != m3 && m1 != m3 && c != m1 && c != m2 && c != m3) {
                return i + 4
            }
            // shift
            m1 = m2
            m2 = m3
            m3 = c
        }

        throw IllegalArgumentException("No marker found in $s")
    }

    fun findMarker2(s: String): Int {
        check(s.length >= 14) { throw IllegalArgumentException("String too short!") }
        val markers = s.take(13).toMutableList()

        s.drop(13).forEachIndexed { i, c ->
            markers.add(c)
            if (markers.toSet().size == 14) {
                return i + 14
            }
            markers.removeAt(0)
        }

        throw IllegalArgumentException("No marker found in $s")
    }
}

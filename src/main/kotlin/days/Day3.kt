package days

class Day3 : Day(3) {

    override fun partOne(): Any {
        return inputList.map { Day3Util.splitString(it) }
            .map { Day3Util.overlappingChars(it.first, it.second) }
            .sumOf { chars -> chars.sumOf { Day3Util.letterToScore(it) } }
    }

    override fun partTwo(): Any {
        return inputList.windowed(3, 3, false)
            .sumOf { groupList ->
                check(groupList.size == 3) { "Size should be 3! But was ${groupList.size}" }
                val overlap = groupList.map { it.toSet() }.reduce { l, r -> l.intersect(r) }
                check(overlap.size == 1) { "Expected only 1 char in common but got ${overlap.size}" }
                Day3Util.letterToScore(overlap.iterator().next())
            }
    }
}

object Day3Util {
    fun splitString(s: String): Pair<String, String> {
        val subStringLength = s.length / 2
        check(subStringLength * 2 == s.length) { "Expected even length for $s, but was ${s.length}" }
        return s.substring(0, subStringLength) to s.substring(subStringLength)
    }

    fun letterToScore(s: Char): Int {
        // ascii magic...
        return if (s.isLowerCase()) {
            s - 'a' + 1
        } else {
            s - 'A' + 27
        }
    }

    fun overlappingChars(first: String, second: String): List<Char> {
        return first.toSet().intersect(second.toSet()).toList()
    }
}

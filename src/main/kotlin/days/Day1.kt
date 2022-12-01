package days

class Day1 : Day(1) {

    override fun partOne(): Any {
        // reduce -> if empty add 0 / if not empty add to last item
        return inputList
            .map { it.trim().toIntOrNull() }
            .fold(mutableListOf(0)) { l, i ->
                if (i == null) {
                    l.add(0, 0)
                } else {
                    l[0] += i
                }
                l
            }.max()

    }

    override fun partTwo(): Any {
        val weights = inputList
            .map { it.trim().toIntOrNull() }
            .fold(mutableListOf(0)) { l, i ->
                if (i == null) {
                    l.add(0, 0)
                } else {
                    l[0] += i
                }
                l
            }
        weights.sortDescending()
        return weights.take(3).sum()
    }
}

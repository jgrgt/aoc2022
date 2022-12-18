package days

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day16Test {
    val input = """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II""".lines()

    val exampleOrder = listOf("DD", "BB", "JJ", "HH", "EE", "CC")
    val exampleOrderWithStart = listOf("AA", "DD", "BB", "JJ", "HH", "EE", "CC")
    val myOrder2 = listOf("JJ", "BB", "CC")
    val elephantOrder = listOf("DD", "HH", "EE")
    val myIndices2 = listOf(6, 1, 2)
    val elephantIndices = listOf(3, 5, 4)


    @Test
    fun example1() {
        val actual = Game16(input).solve()
        assertEquals(1651, actual)
    }

    @Test
    fun example2() {
        val actual = Game16(input).solve2()
        assertEquals(1707, actual)
    }
}

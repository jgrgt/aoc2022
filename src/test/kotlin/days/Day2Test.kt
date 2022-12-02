package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {
    val underTest = Day2()

    @Test
    fun `it should work for the example`() {
        val actual = underTest.doPartOne(
            listOf(
                "A Y",
                "B X",
                "C Z",
            )
        )
        assertEquals(15, actual)
    }

    @Test
    fun `a game should draw correctly`() {
        val game = Game(Hand.PAPER, Hand.PAPER)
        val actual = game.score()
        assertEquals(2 + 3, actual)
    }
}

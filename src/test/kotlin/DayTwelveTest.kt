import org.junit.Test
import kotlin.test.assertEquals

internal class DayTwelveTest {

    @Test
    fun partOne() {

        assertEquals(10, DayTwelve("/day-twelve-small.txt").partOneResult)
        assertEquals(19, DayTwelve("/day-twelve-medium.txt").partOneResult)
        assertEquals(226, DayTwelve("/day-twelve-large.txt").partOneResult)
    }

    @Test
    fun partTwo() {

        assertEquals(36, DayTwelve("/day-twelve-small.txt").partTwoResult)
        assertEquals(103, DayTwelve("/day-twelve-medium.txt").partTwoResult)
        assertEquals(3509, DayTwelve("/day-twelve-large.txt").partTwoResult)
    }
}
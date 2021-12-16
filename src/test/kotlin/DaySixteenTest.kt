import org.junit.Test
import kotlin.test.assertEquals

internal class DaySixteenTest {

    @Test
    fun partOne() {

        assertEquals(16, DaySixteen("8A004A801A8002F478").partOneResult)
        assertEquals(12, DaySixteen("620080001611562C8802118E34").partOneResult)
        assertEquals(23, DaySixteen("C0015000016115A2E0802F182340").partOneResult)
        assertEquals(31, daySixteen("/day-sixteen.txt").partOneResult)
    }
}
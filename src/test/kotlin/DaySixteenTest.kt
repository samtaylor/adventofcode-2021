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

    @Test
    fun partTwo() {

        assertEquals(3, DaySixteen("C200B40A82").partTwoResult)
        assertEquals(54, DaySixteen("04005AC33890").partTwoResult)
        assertEquals(7, DaySixteen("880086C3E88112").partTwoResult)
        assertEquals(9, DaySixteen("CE00C43D881120").partTwoResult)
        assertEquals(1, DaySixteen("D8005AC2A8F0").partTwoResult)
        assertEquals(0, DaySixteen("F600BC2D8F").partTwoResult)
        assertEquals(0, DaySixteen("9C005AC2F8F0").partTwoResult)
        assertEquals(1, DaySixteen("9C0141080250320F1802104A08").partTwoResult)
    }
}
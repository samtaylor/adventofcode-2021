import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

object DaySeven : Day<Long, Long>("/day-seven.txt") {

    private val input = data.first().split(",").map { it.toLong() }

    override val partOneResult: Long
        get() {

            val median = input.median()

            var totalFuel = 0L
            input.forEach {

                totalFuel += abs(median - it)
            }

            return totalFuel
        }

    override val partTwoResult: Long
        get() {

            val totalFuelRoundingUp = calculateTotalFuel(input.average(roundUp = true))
            val totalFuelRoundingDown = calculateTotalFuel(input.average(roundUp = false))

            return min(totalFuelRoundingUp, totalFuelRoundingDown)
        }

    private fun calculateTotalFuel(average: Long): Long {

        var totalFuel = 0L
        input.forEach {

            val n = abs(average - it) + 1
            val fuel = (n * (n - 1)) / 2
            totalFuel += fuel
        }
        return totalFuel
    }

    private fun List<Long>.average(roundUp: Boolean) = if (roundUp) {

        ceil(average()).toLong()
    } else {

        floor(average()).toLong()
    }

    private fun List<Long>.median() = sorted().let { sortedList ->

        if (sortedList.size % 2 == 0) {

            val lower = sortedList[(sortedList.size / 2) - 1]
            val upper = sortedList[(sortedList.size / 2)]
            (lower + upper) / 2
        } else {

            sortedList[sortedList.size / 2]
        }
    }
}
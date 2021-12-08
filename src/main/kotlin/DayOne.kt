object DayOne : Day<Int, Int>("/day-one.txt") {

    override val partOneResult: Int
    get() = countIncreases(data.map { it.toLong() })

    private fun countIncreases(depths: List<Long>): Int {

        var count = 0
        (1 until depths.size).forEach { index ->

            if (depths[index - 1] < depths[index]) {

                count ++
            }
        }

        return count
    }

    override val partTwoResult: Int
    get() {

        val depths = data.map { it.toLong() }

        val windows = (0 until depths.size - 2).map { index ->

            depths[index] + depths[index + 1] + depths[index + 2]
        }

        return countIncreases(windows)
    }
}
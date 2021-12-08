object DaySix : Day<Long, Long>("/day-six.txt") {

    private const val PRINT_FISH_COUNTS = false

    private const val INITIAL_TIMER_FOR_NEW_FISH = 8
    private const val TIMER_REFRESH_VALUE = 6
    private const val NUMBER_OF_TIMERS = 9

    override val partOneResult: Long
    get() = runFor(data.first().split(",").map { it.toInt() }.toMutableList(), 80)

    override val partTwoResult: Long
    get() = runFor(data.first().split(",").map { it.toInt() }, 256)

    private fun runFor(school: List<Int>, days: Int): Long {

        val timers = MutableList<Long>(NUMBER_OF_TIMERS) { 0 }

        school.forEach { fish ->

            timers[fish] ++
        }

        if (PRINT_FISH_COUNTS) {

            println("Initial State: ")
            timers.forEachIndexed { index, count ->

                println("$count fish have a timer of $index")
            }
        }

        (1 .. days).forEach { day ->

            val fishOnZeroTimer = timers[0]
            (1 until NUMBER_OF_TIMERS).forEach {

                timers[it - 1] = timers[it]
            }

            timers[INITIAL_TIMER_FOR_NEW_FISH] = fishOnZeroTimer
            timers[TIMER_REFRESH_VALUE] += fishOnZeroTimer

            if (PRINT_FISH_COUNTS) {

                println("After $day Days: ")
                timers.forEachIndexed { index, count ->

                    println("$count fish have a timer of $index")
                }
            }
        }

        return timers.sum()
    }
}
object DaySeventeen : Day<Int, Int>("/day-seventeen.txt") {

    private val xRange: IntRange = data[0]
        .removePrefix("target area: ")
        .split(", ").let { xy ->

            xy[0].split("..").let { x ->

                val from = x[0].removePrefix("x=").toInt()
                val to = x[1].toInt()

                from .. to
            }
        }

    private val yRange: IntRange = data[0]
        .removePrefix("target area: ")
        .split(", ").let { xy ->

            xy[1].split("..").let { y ->

                val from = y[0].removePrefix("y=").toInt()
                val to = y[1].toInt()

                from .. to
            }
        }

    override val partOneResult: Int
    get() = calculateResults().keys.maxOrNull() ?: 0

    override val partTwoResult: Int
    get() = calculateResults().let { results ->

        results.keys.sumOf { results[it]?.size ?: 0 }
    }

    private fun calculateResults() = mutableMapOf<Int, MutableList<Pair<Int, Int>>>().also { results ->

        (-xRange.last .. xRange.last).forEach { initialXVelocity ->

            (yRange.first .. -yRange.first).forEach { initialYVelocity ->

                var x = 0
                var y = 0
                var maxY = Integer.MIN_VALUE
                var xVelocity = initialXVelocity
                var yVelocity = initialYVelocity

                do {

                    x += xVelocity
                    y += yVelocity

                    if (xVelocity > 0) { xVelocity -- }
                    else if (xVelocity < 0) { xVelocity ++ }
                    yVelocity --

                    if (maxY < y) { maxY = y }

                } while(!finishedChecking(x, y))

                if (locationIsInTargetArea(x, y)) {

                    results.putIfAbsent(maxY, mutableListOf())
                    results[maxY]?.add(Pair(initialXVelocity, initialYVelocity))
                }
            }
        }
    }

    private fun finishedChecking(x: Int, y: Int) =
        locationIsInTargetArea(x, y) || xHasPassedTargetArea(x) || yHasDroppedBelowTargetArea(y)

    private fun locationIsInTargetArea(x: Int, y: Int) =
        xRange.contains(x) && yRange.contains(y)

    private fun xHasPassedTargetArea(x: Int) =
        x > xRange.last

    private fun yHasDroppedBelowTargetArea(y: Int) =
        y < yRange.first
}
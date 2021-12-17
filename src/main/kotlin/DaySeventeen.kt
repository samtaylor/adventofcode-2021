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
    get() = calculateResults().keys.maxOrNull()!!

    override val partTwoResult: Int
    get() = calculateResults().let { results ->

        results.keys.sumOf { results[it]!!.size }
    }

    private fun calculateResults() = mutableMapOf<Int, MutableList<Pair<Int, Int>>>().also { results ->

        (-xRange.last .. xRange.last).forEach { initialXVelocity ->

            (yRange.first .. -yRange.first).forEach { initialYVelocity ->

                var done = false
                var x = 0
                var y = 0
                var maxY = Integer.MIN_VALUE
                var xVelocity = initialXVelocity
                var yVelocity = initialYVelocity

                if (xVelocity == 23 && yVelocity == -10) {

                    println()
                }

                while (!done) {

                    x += xVelocity
                    y += yVelocity
                    if (xVelocity > 0) xVelocity --
                    else if (xVelocity < 0) xVelocity ++
                    yVelocity --

                    if (maxY < y) maxY = y

                    if (xRange.contains(x) && yRange.contains(y)) {

                        results.putIfAbsent(maxY, mutableListOf())
                        results[maxY]!!.add(Pair(initialXVelocity, initialYVelocity))
                        done = true
                    }

                    if (xHasPassedTargetArea(x) || yHasDroppedBelowTargetArea(y)) {

                        done = true
                    }
                }
            }
        }
    }

    private fun xHasPassedTargetArea(x: Int) = x > xRange.last

    private fun yHasDroppedBelowTargetArea(y: Int) = y < yRange.first
}
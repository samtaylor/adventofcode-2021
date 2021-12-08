object DayTwo : Day<Int, Int>("/day-two.txt") {

    private const val FORWARD = "forward"
    private const val UP = "up"
    private const val DOWN = "down"

    private val directionAndDistanceData =
        data.map { it.split(" ") }.map { DirectionAndDistance(it[0], it[1].toInt()) }


    override val partOneResult: Int
    get() {

        val positionAndDepth = calculatePositionAndDepth(directionAndDistanceData)

        return positionAndDepth.position * positionAndDepth.depth
    }

    override val partTwoResult: Int
    get() {

        val positionAndDepth = calculatePositionAndDepthWhileTrackingAim(directionAndDistanceData)

        return positionAndDepth.position * positionAndDepth.depth
    }

    private fun calculatePositionAndDepth(data: List<DirectionAndDistance>): PositionAndDepth {

        var position = 0
        var depth = 0

        data.forEach {

            when (it.direction) {

                FORWARD -> position += it.distance
                UP      -> depth -= it.distance
                DOWN    -> depth += it.distance
            }
        }

        return PositionAndDepth(position, depth)
    }

    private fun calculatePositionAndDepthWhileTrackingAim(data: List<DirectionAndDistance>): PositionAndDepth {

        var position = 0
        var depth = 0
        var aim = 0

        data.forEach {

            when (it.direction) {

                UP      -> aim -= it.distance
                DOWN    -> aim += it.distance
                FORWARD -> {

                    position += it.distance
                    depth += (aim * it.distance)
                }
            }
        }

        return PositionAndDepth(position, depth)
    }

    data class DirectionAndDistance(val direction: String, val distance: Int)
    data class PositionAndDepth(val position: Int, val depth: Int)
}
class DayTwelve(filename: String = "/day-twelve.txt") : Day<Long, Long>(filename) {

    private val caves: MutableMap<String, MutableList<String>> = mutableMapOf("start" to mutableListOf())

    init {

        parseCaves()
    }

    override val partOneResult: Long
        get() = calculateNumberOfPaths(::canVisitCave).size.toLong()
    override val partTwoResult: Long
        get() = calculateNumberOfPaths(::canVisitCavePart2).size.toLong()

    private fun calculateNumberOfPaths(canVisitCave: (String, String) -> Boolean) =
        mutableListOf<String>().apply {

            caves[START_CAVE]?.forEach { child ->

                generatePath(START_CAVE, child, this, canVisitCave)
            }
        }

    private fun generatePath(
        currentPath: String,
        cave: String,
        foundPaths: MutableList<String>,
        canVisitCave: (String, String) -> Boolean,
    ) {

        val newPath = "$currentPath-$cave"

        if (cave == END_CAVE) {

            foundPaths.add(newPath)
        } else {

            caves[cave]?.filter { canVisitCave(newPath, it) }?.forEach { child ->

                generatePath(newPath, child, foundPaths, canVisitCave)
            }
        }
    }

    private fun canVisitCave(currentPath: String, cave: String): Boolean =
        cave.isLargeCave() || !currentPath.contains(cave)

    private fun canVisitCavePart2(currentPath: String, cave: String): Boolean {

        if (cave == END_CAVE || cave.isLargeCave()) {

            return true
        } else {

            val caveVisitCounts = mutableMapOf<String, Int>().apply {

                currentPath
                    .removePrefix("start-")
                    .split("-")
                    .filter { !it.isLargeCave() }
                    .forEach { visitedCave ->

                        putIfAbsent(visitedCave, 0)
                        this[visitedCave] = this[visitedCave]!! + 1
                    }
            }

            return caveVisitCounts[cave] == null || !caveVisitCounts.values.any { it > 1 }
        }
    }

    private fun parseCaves() {

        data.forEach { line ->

            val nodeAndChild = line.split("-")
            val node = nodeAndChild[0]
            val child = nodeAndChild[1]

            if (node != END_CAVE && !caves.keys.contains(node)) {

                caves[node] = mutableListOf()
            }

            if (child != END_CAVE && !caves.keys.contains(child)) {

                caves[child] = mutableListOf()
            }

            if (child != START_CAVE && caves[node]?.contains(child) == false) {

                caves[node]?.add(child)
            }

            if (node != START_CAVE && caves[child]?.contains(node) == false) {

                caves[child]?.add(node)
            }
        }
    }

    private fun String.isLargeCave() = this == uppercase()

    companion object {

        private const val START_CAVE = "start"
        private const val END_CAVE = "end"
    }
}
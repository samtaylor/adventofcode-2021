import kotlin.math.floor

object DayFifteen : Day<Long, Long>("/day-fifteen.txt") {

    override val partOneResult: Long
        get() {

            val map = data.map { line ->

                line.toCharArray().map { it.digitToInt() }
            }
            val visited = List(map.size) { BooleanArray(map[it].size) { false } }
            val tentativeDistances = List(map.size) { LongArray(map[it].size) { Long.MAX_VALUE } }.also {

                it[0][0] = 0
            }

            checkNode(map, visited, tentativeDistances)
            return tentativeDistances[map[0].size - 1][map.size - 1]
        }

    override val partTwoResult: Long
        get() {

            val tempMap = data.map { line ->

                line.toCharArray().map { it.digitToInt() }
            }

            val map = List(tempMap.size * 5) { y ->

                List(tempMap[0].size * 5) { x ->

                    var tempValue = tempMap[y % tempMap.size][x % tempMap[0].size]
                    val xMultiplier = floor(y / tempMap.size.toDouble()).toInt()
                    val yMultiplier = floor(x / tempMap[0].size.toDouble()).toInt()

                    if (xMultiplier > 0) {

                        tempValue += xMultiplier
                    }
                    if (yMultiplier > 0) {

                        tempValue += yMultiplier
                    }
                    if (tempValue > 9) tempValue -= 9

                    tempValue
                }
            }
            val visited = List(map.size) { BooleanArray(map[it].size) { false } }
            val tentativeDistances = List(map.size) { LongArray(map[it].size) { Long.MAX_VALUE } }.also {

                it[0][0] = 0
            }

            checkNode(map, visited, tentativeDistances)
            return tentativeDistances[map[0].size - 1][map.size - 1]
        }

    private fun checkNode(
        map: List<List<Int>>,
        visited: List<BooleanArray>,
        tentativeDistances: List<LongArray>,
    ) {

        var currentNode: Node? = Node(0, 0)

        while (currentNode != null) {

            val x = currentNode.x
            val y = currentNode.y

            mutableListOf<Node>()
                .also {

                    if (x - 1 >= 0) it.add(Node(x - 1, y))
                    if (y - 1 >= 0) it.add(Node(x, y - 1))
                    if (x + 1 < map[y].size) it.add(Node(x + 1, y))
                    if (y + 1 < map.size) it.add(Node(x, y + 1))
                }
                .filter { !visited[it.y][it.x] }
                .forEach { neighbour ->

                    val updatedDistance = tentativeDistances[y][x] + map[neighbour.y][neighbour.x]
                    if (tentativeDistances[neighbour.y][neighbour.x] > updatedDistance) {

                        tentativeDistances[neighbour.y][neighbour.x] = updatedDistance
                    }
                }

            visited[y][x] = true
            currentNode = unvisitedMinimumNode(visited, tentativeDistances)
        }
    }

    data class Node(val x: Int, val y: Int)

    private fun unvisitedMinimumNode(
        visited: List<BooleanArray>,
        tentativeDistances: List<LongArray>,
    ): Node? {

        var minimumX = -1
        var minimumY = -1
        var minValue = Long.MAX_VALUE

        tentativeDistances.indices.forEach { y ->

            tentativeDistances[y].indices.forEach { x ->

                if (!visited[y][x]) {

                    if (tentativeDistances[y][x] < minValue) {

                        minValue = tentativeDistances[y][x]

                        minimumX = x
                        minimumY = y
                    }
                }
            }
        }

        return if (minimumX != -1) {

            Node(minimumX, minimumY)
        } else {

            null
        }
    }
}
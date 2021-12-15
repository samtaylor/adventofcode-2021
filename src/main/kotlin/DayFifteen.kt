object DayFifteen : Day<Long, Long>("/day-fifteen.txt") {

    private val map = data.map { line ->

        line.toCharArray().map { it.digitToInt() }
    }

    private val visited = List(map.size) { BooleanArray(map[it].size) { false } }
    private val tentativeDistances = List(map.size) { LongArray(map[it].size) { Long.MAX_VALUE } }.also {

        it[0][0] = 0
    }

    override val partOneResult: Long
        get() {

            checkNode()
            return tentativeDistances[map[0].size - 1][map.size - 1]
        }

    override val partTwoResult: Long
        get() = TODO("Not yet implemented")

    private fun checkNode() {

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
            currentNode = unvisitedMinimumNode()
        }
    }

    data class Node(val x: Int, val y: Int)

    private fun unvisitedMinimumNode(): Node? {

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
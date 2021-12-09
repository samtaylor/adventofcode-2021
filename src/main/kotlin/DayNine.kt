import java.awt.Point

object DayNine : Day<Int, Int>("/day-nine.txt") {

    private val input = data.map { it.toCharArray().toList().map { char -> char.digitToInt() } }

    override val partOneResult: Int
    get() = lowPoints()
        .sumOf { input[it.y][it.x] + 1 }

    override val partTwoResult: Int
        get() = lowPoints()
            .map { lowPoint -> basinForPoint(lowPoint) }
            .sortedBy { it.size }
            .reversed()
            .subList(0, 3)
            .map { it.size }
            .reduce { acc, i -> acc * i }

    private fun basinForPoint(point: Point) =
        mutableListOf<Point>().also {

            it.add(point)
            basinSearch(it, point)
        }

    private fun basinSearch(basinPoints: MutableList<Point>, point: Point) {

        neighbouringBasinPoints(point).forEach { neighbour ->

            if (!basinPoints.contains(neighbour)) {

                basinPoints.add(neighbour)
                basinSearch(basinPoints, neighbour)
            }
        }
    }

    private fun lowPoints(): List<Point> =
        mutableListOf<Point>().also { list ->
            input.forEachIndexed { y, lines ->

                lines.forEachIndexed { x, number ->

                    if (isLowestPoint(x, y, number)) {

                        list.add(Point(x, y))
                    }
                }
            }
        }

    private fun neighbouringBasinPoints(point: Point) =
        mutableListOf<Point>().also {

            if (point.y - 1 >= 0 && input[point.y - 1][point.x] != 9) it.add(Point(point.x, point.y - 1))
            if (point.y + 1 < input.size && input[point.y + 1][point.x] != 9) it.add(Point(point.x, point.y + 1))
            if (point.x + 1 < input[0].size && input[point.y][point.x + 1] != 9) it.add(Point(point.x + 1, point.y))
            if (point.x - 1 >= 0 && input[point.y][point.x - 1] != 9) it.add(Point(point.x - 1, point.y))
        }

    private fun isLowestPoint(x: Int, y: Int, number: Int): Boolean {

        val north = if (y - 1 < 0) 10 else input[y - 1][x]
        val south = if (y + 1 == input.size) 10 else input[y + 1][x]
        val east = if (x + 1 == input[0].size) 10 else input[y][x + 1]
        val west = if (x - 1 < 0) 10 else input[y][x - 1]

        return number < north && number < south && number < east && number < west
    }
}
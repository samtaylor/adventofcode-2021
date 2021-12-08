import kotlin.math.max
import kotlin.math.min

object DayFive : Day<Int, Int>("/day-five.txt") {

    private val input: Input = Input(data.map { line ->

        val coords = line.split("->").map { it.trim() }
        val x1y1 = coords[0].split(",").map { it.toInt() }
        val x2y2 = coords[1].split(",").map { it.toInt() }
        LineSegment(x1y1[0], x2y2[0], x1y1[1], x2y2[1])
    })

    private val grid = List(1_000) { MutableList(1_000) { 0 } }

    override val partOneResult: Int
        get() {

            applyHorizontalAndVerticalLines()

            return grid.flatMap { it.filter { number -> number > 1 } }.count()
        }

    override val partTwoResult: Int
        get() {

            applyHorizontalAndVerticalLines()

            applyDiagonalLines()

            return grid.flatMap { it.filter { number -> number > 1 } }.count()
        }

    private fun applyDiagonalLines() {

        input.segments.filter { it.isDiagonal }.forEach { lineSegment ->

            val xIncrement = if (lineSegment.x2 > lineSegment.x1) 1 else -1
            val yIncrement = if (lineSegment.y2 > lineSegment.y1) 1 else -1

            var x = lineSegment.x1
            var y = lineSegment.y1

            var done = false
            while (!done) {

                grid[x][y] ++

                if (x == lineSegment.x2 && y == lineSegment.y2) {

                    done = true
                } else {

                    x += xIncrement
                    y += yIncrement
                }
            }
        }
    }

    private fun applyHorizontalAndVerticalLines() {

        input.segments.filter { !it.isDiagonal }.forEach { lineSegment ->

            val startX = min(lineSegment.x1, lineSegment.x2)
            val endX = max(lineSegment.x1, lineSegment.x2)
            val startY = min(lineSegment.y1, lineSegment.y2)
            val endY = max(lineSegment.y1, lineSegment.y2)

            (startX .. endX).forEach { x ->

                (startY .. endY).forEach { y ->

                    grid[x][y] ++
                }
            }
        }
    }

    data class Input(val segments: List<LineSegment>)
    data class LineSegment(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {

        val isDiagonal: Boolean
        get() = x1 != x2 && y1 != y2
    }
}
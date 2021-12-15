object DayThirteen : Day<Long, Long>("/day-thirteen.txt"){

    private var dotGrid: List<BooleanArray>
    private val folds = mutableListOf<Fold>()

    init {

        val xyList = mutableListOf<List<String>>()

        data.forEach { line ->

            if (line.isNotBlank()) {

                if (line.contains("=")) {

                    val fold = line.substring(line.indexOf("=") - 1).split("=")
                    val direction = if (fold[0] == "x") X else Y
                    val value = fold[1].toInt()
                    folds.add(Fold(direction, value))
                } else {

                    xyList.add(line.split(","))
                }
            }
        }

        val maxX = xyList.maxOf { it[0].toInt() } + 1
        val maxY = xyList.maxOf { it[1].toInt() } + 1

        dotGrid = MutableList(maxY) {

            BooleanArray(maxX) { false }
        }

        xyList.forEach { xy ->

            val x = xy[0].toInt()
            val y = xy[1].toInt()

            dotGrid[y][x] = true
        }
    }

    override val partOneResult: Long
    get() = executeFold(folds.first()).sumOf { row -> row.count { it } }.toLong()

    override val partTwoResult: Long
        get() {

            folds.forEach { fold ->

                dotGrid = executeFold(fold)
            }

            printGrid(dotGrid)

            return 0L
        }

    private fun executeFold(fold: Fold) =
        if (fold.direction == X) {

            foldX(fold.value)
        } else {

            foldY(fold.value)
        }

    private fun foldX(value: Int): List<BooleanArray> {

        val newDotGrid = List(dotGrid.size) { BooleanArray(value) { false } }

        newDotGrid.indices.forEach { row ->

            newDotGrid[row].indices.forEach { column ->

                newDotGrid[row][column] = dotGrid[row][column]
            }
        }

        dotGrid.indices.forEach { row ->

            var newDotGridColumn = newDotGrid[0].size - 1
            (value + 1 until dotGrid[row].size).forEach { column ->

                newDotGrid[row][newDotGridColumn] = if (dotGrid[row][column]) true else newDotGrid[row][newDotGridColumn]
                newDotGridColumn --
            }
        }

        return newDotGrid
    }

    private fun foldY(value: Int): List<BooleanArray> {

        val newDotGrid = List(value) { BooleanArray(dotGrid[0].size) { false } }

        newDotGrid.indices.forEach { row ->

            newDotGrid[row].indices.forEach { column ->

                newDotGrid[row][column] = dotGrid[row][column]
            }
        }

        var newGridRow = newDotGrid.size - 1

        (value + 1 until dotGrid.size).forEach { row ->

            dotGrid[row].indices.forEach { column ->

                newDotGrid[newGridRow][column] = if (dotGrid[row][column]) true else newDotGrid[newGridRow][column]
            }
            newGridRow --
        }

        return newDotGrid
    }

    private fun printGrid(grid: List<BooleanArray>) {

        grid.indices.forEach { row ->

            grid[row].indices.forEach { column ->

                print(if (grid[row][column]) "#" else ".")
            }

            println()
        }

        println()
    }

    data class Fold(val direction: Direction, val value: Int)
    abstract class Direction
    object X : Direction()
    object Y : Direction()
}
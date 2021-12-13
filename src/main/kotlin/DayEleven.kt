object DayEleven : Day<Long, Long>("/day-eleven.txt") {

    private val initialWorldState: List<IntArray> =
        data.map { line -> line.toCharArray().map { char -> char.digitToInt() }.toIntArray() }

    override val partOneResult: Long
        get() {

            val world = List(initialWorldState.size) { row ->

                IntArray(initialWorldState[row].size) { column -> initialWorldState[row][column] }
            }

            var flashCount = 0L

            repeat(100) {

                flashCount += tick(world)
            }

            return flashCount
        }

    override val partTwoResult: Long
        get() {

            val world = List(initialWorldState.size) { row ->

                IntArray(initialWorldState[row].size) { column -> initialWorldState[row][column] }
            }

            val worldSize = world.sumOf { it.count() }.toLong()
            var flashCount: Long
            var tickCount = 0L

            do {

                flashCount = tick(world)
                tickCount ++
            } while(worldSize != flashCount)

            return tickCount

        }


    private fun printWorld(world: List<IntArray>, flashes: List<BooleanArray>? = null) {

        world.indices.forEach { row ->

            world[row].indices.forEach { column ->

                if (world[row][column] > 9) {

                    print("x")
                } else {

                    print(world[row][column])
                }
            }

            if (flashes != null) {

                print("\t\t")

                flashes[row].indices.forEach { column ->

                    print(if (flashes[row][column]) "x" else "o")
                }
            }

            println()
        }

        println()
    }

    private fun increaseOctopusEnergyBy1(world: List<IntArray>) {

        world.indices.forEach { row ->

            world[row].indices.forEach { column ->

                world[row][column] ++
            }
        }
    }

    private fun rippleFlashesIntoNeighbouringOctopuses(world: List<IntArray>): List<BooleanArray> {

        val flashed = List(world.size) { row ->

            BooleanArray(world[row].size) { false }
        }
        var flashesFound: Boolean

        do {

            flashesFound = false
            world.indices.forEach { row ->

                world[row].indices.forEach { column ->

                    if (!flashed[row][column] && world[row][column] > 9) {

                        flashesFound = true
                        flashed[row][column] = true

                        incrementCell(world, row - 1, column - 1)
                        incrementCell(world, row - 1, column)
                        incrementCell(world, row - 1, column + 1)

                        incrementCell(world, row, column - 1)
                        incrementCell(world, row, column + 1)

                        incrementCell(world, row + 1, column - 1)
                        incrementCell(world, row + 1, column)
                        incrementCell(world, row + 1, column + 1)
                    }
                }
            }
        } while (flashesFound)

        return flashed
    }

    private fun incrementCell(world: List<IntArray>, row: Int, column: Int) {

        try {

            world[row][column] ++
        } catch (_ :IndexOutOfBoundsException) {}
    }

    private fun resetFlashedOctopusesByTo0(world: List<IntArray>, flashed: List<BooleanArray>): Long {

        val flashCount = flashed.sumOf { row -> row.count { it } }.toLong()
        world.indices.forEach { row ->

            world[row].indices.forEach { column ->

                if (flashed[row][column]) {

                    world[row][column] = 0
                    flashed[row][column] = false
                }
            }
        }
        return flashCount
    }

    private fun tick(world: List<IntArray>): Long {

        increaseOctopusEnergyBy1(world)
        return resetFlashedOctopusesByTo0(world, rippleFlashesIntoNeighbouringOctopuses(world))
    }
}
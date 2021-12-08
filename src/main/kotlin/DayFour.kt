object DayFour : Day<Int, Int>("/day-four.txt") {

    private val input: Input

    init {

        val numbers = data[0].split(",").map { it.toInt() }
        val boards = mutableListOf<Board>()
        val boardNumbers = mutableListOf<BoardNumber>()

        (2 until data.size).forEach { index ->

            if (data[index] == "" ) {

                boards.add(Board(List(boardNumbers.size) { boardNumbers[it] }))
                boardNumbers.clear()
            } else {

                val temp = data[index].trim().split("\\s+".toRegex())
                boardNumbers.addAll(temp.map { BoardNumber(it.toInt()) })

                if (index == data.size - 1) {

                    boards.add(Board(List(boardNumbers.size) { boardNumbers[it] }))
                }
            }
        }

        input = Input(numbers, boards)
    }

    override val partOneResult: Int
        get() {

            input.numbers.forEach { number ->

                input.boards.forEach { board ->

                    board.markNumber(number)
                }

                input.boards.firstOrNull { it.isWinner }?.let { winningBoard ->

                    return winningBoard.sumOfUnmarkedNumbers * number
                }
            }
            return 0
        }
    override val partTwoResult: Int
        get() {

            var lastWinningBoard: Board? = null
            var lastWinningNumber = -1
            input.numbers.forEach { number ->

                input.boards.forEach { board ->

                    board.markNumber(number)
                }

                input.boards.filter { it.isWinner }.also { println("---> ${it.size}, ${input.boards.size}") }.forEach { winningBoard ->

                    lastWinningBoard = winningBoard
                    lastWinningNumber = number
                }

                input.boards.removeAll(input.boards.filter { it.isWinner })
            }

            return (lastWinningBoard?.sumOfUnmarkedNumbers ?: 0) * lastWinningNumber
        }

    data class Input(val numbers: List<Int>, val boards: MutableList<Board>)
    data class Board(val numbers: List<BoardNumber>) {

        val sumOfUnmarkedNumbers: Int
        get() = numbers.filter { !it.marked }.sumOf { it.number }

        val isWinner: Boolean
        get() {

            // columns
            (0 .. 4).forEach { index ->

                if (numbers[index].marked &&
                        numbers[index + 5].marked &&
                        numbers[index + 10].marked &&
                        numbers[index + 15].marked &&
                        numbers[index + 20].marked) {

                    return true
                }
            }

            // rows
            (0 .. 20 step 5).forEach { index ->

                if (numbers[index].marked &&
                        numbers[index + 1].marked &&
                        numbers[index + 2].marked &&
                        numbers[index + 3].marked &&
                        numbers[index + 4].marked) {

                    return true
                }
            }

            return false
        }

        fun markNumber(number: Int) {

            numbers.firstOrNull { it.number == number }?.marked = true
        }
    }
    data class BoardNumber(val number: Int, var marked: Boolean = false)
}
object DayEight : Day<Int, Int>("/day-eight.txt") {

    private val input =
        data.map {

            it.split(" | ").let { line ->

                Data(
                    line[0].split(" "),
                    line[1].split(" "),
                )
            }
        }

    override val partOneResult: Int
        get() = input.flatMap { datum ->

            datum.outputValues
        }.filter {

            it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7
        }.size

    override val partTwoResult: Int
        get() {

            return input.sumOf { calculateOutput(it) }
        }

    private fun calculateOutput(datum: Data): Int {

        val numbers = MutableList(10) { "" }

        numbers[1] = findNumberOne(datum.uniqueSignalPatterns)
        numbers[4] = findNumberFour(datum.uniqueSignalPatterns)
        numbers[7] = findNumberSeven(datum.uniqueSignalPatterns)
        numbers[8] = findNumberEight(datum.uniqueSignalPatterns)

        numbers[3] = findNumberThree(datum.uniqueSignalPatterns, numbers[7])
        numbers[9] = findNumberNine(datum.uniqueSignalPatterns, numbers[3])
        numbers[0] = findNumberZero(datum.uniqueSignalPatterns, numbers[9], numbers[7])
        numbers[6] = findNumberSix(datum.uniqueSignalPatterns, numbers[9], numbers[0])
        numbers[5] = findNumberFive(datum.uniqueSignalPatterns, numbers[3], numbers[6])
        numbers[2] = findNumberTwo(datum.uniqueSignalPatterns, numbers[3], numbers[5])

        return StringBuilder().apply {

            datum.outputValues.forEach { outputDigit ->

                append(numbers.indexOf(numbers.first { it.exactMatchesString(outputDigit) }))
            }
        }.toString().toInt()
    }

    private fun findNumberOne(uniqueSignalPattern: List<String>) =
        uniqueSignalPattern.first { it.length == 2 }

    private fun findNumberSeven(uniqueSignalPattern: List<String>) =
        uniqueSignalPattern.first { it.length == 3 }

    private fun findNumberFour(uniqueSignalPattern: List<String>) =
        uniqueSignalPattern.first { it.length == 4 }

    private fun findNumberEight(uniqueSignalPattern: List<String>) =
        uniqueSignalPattern.first { it.length == 7 }

    private fun findNumberThree(uniqueSignalPattern: List<String>, numberSeven: String) =
        uniqueSignalPattern.filter { it.length == 5 }.first { it.matchesString(numberSeven) }

    private fun findNumberNine(uniqueSignalPattern: List<String>, numberThree: String) =
        uniqueSignalPattern.filter { it.length == 6 }.first { it.matchesString(numberThree) }

    private fun findNumberZero(uniqueSignalPattern: List<String>, numberNine: String, numberSeven: String) =
        uniqueSignalPattern.filter { it.length == 6 && it != numberNine }.first { it.matchesString(numberSeven) }

    private fun findNumberSix(uniqueSignalPattern: List<String>, numberNine: String, numberZero: String) =
        uniqueSignalPattern.first { it.length == 6 && it != numberNine && it != numberZero }

    private fun findNumberFive(uniqueSignalPattern: List<String>, numberThree: String, numberSix: String) =
        uniqueSignalPattern.first { it.length == 5  && it != numberThree && it.matchesCount(numberSix) == 5 }

    private fun findNumberTwo(uniqueSignalPattern: List<String>, numberThree: String, numberTwo: String) =
        uniqueSignalPattern.first { it.length == 5  && it != numberThree && it != numberTwo }

    private fun String.exactMatchesString(other: String) =
        length == other.length && matchesString(other)

    private fun String.matchesString(other: String) =
        matchesCount(other) == other.length

    private fun String.matchesCount(other: String): Int {

        var matchesCount = 0

        other.toCharArray().forEach { char ->

            if (this.contains(char)) {

                matchesCount ++
            }
        }

        return matchesCount
    }

    data class Data(val uniqueSignalPatterns: List<String>, val outputValues: List<String>)
}
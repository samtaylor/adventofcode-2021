object DayThree : Day<Int, Int>("/day-three.txt") {

    private val charArrayData = data.map { it.toCharArray() }

    override val partOneResult = calculateGammaRate() * calculateEpsilonRateFromGammaRate(calculateGammaRate())
    override val partTwoResult = calculateOxygenGeneratorRating() * calculateCO2ScrubberRating()

    private fun calculateCO2ScrubberRating(): Int {

        var tempData = charArrayData

        var index = 0
        while (tempData.size > 1) {

            var count = 0

            tempData.forEach { value ->

                if (value[index] == '1') count++
            }

            tempData = if (count / tempData.size.toFloat() >= 0.5F) {

                tempData.filter { it[index] == '0' }
            } else {

                tempData.filter { it[index] == '1' }
            }

            index ++
        }

        return Integer.parseInt(String(tempData[0]), 2)
    }

    private fun calculateOxygenGeneratorRating(): Int {

        var tempData = charArrayData

        var index = 0
        while (tempData.size > 1) {

            var count = 0

            tempData.forEach { value ->

                if (value[index] == '1') count++
            }

            tempData = if (count / tempData.size.toFloat() >= 0.5F) {

                tempData.filter { it[index] == '1' }
            } else {

                tempData.filter { it[index] == '0' }
            }

            index ++
        }

        return Integer.parseInt(String(tempData[0]), 2)
    }

    private fun calculateGammaRate(): Int {

        val total = charArrayData.size.toFloat()
        val count = MutableList(charArrayData[0].size) { 0F }

        charArrayData.forEach { value ->

            value.forEachIndexed { index, bit ->

                if (bit == '1') count[index] ++
            }
        }
        val gammaRate = CharArray(count.size) { '0' }
        count.forEachIndexed { index, bitCount ->

            if (bitCount / total > 0.5f) {

                gammaRate[index] = '1'
            }
        }

        return Integer.parseInt(String(gammaRate), 2)
    }

    private fun calculateEpsilonRateFromGammaRate(gammaRate: Int): Int {

        return Integer.toBinaryString(gammaRate).let { binaryGammaRate ->

            val epsilonRate = CharArray(binaryGammaRate.length) { '1' }
            binaryGammaRate.forEachIndexed { index, bit ->

                if (bit == '1') {

                    epsilonRate[index] = '0'
                }
            }

            return@let Integer.parseInt(String(epsilonRate), 2)
        }
    }
}
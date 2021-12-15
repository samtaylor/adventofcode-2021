import kotlin.math.ceil

object DayFourteen : Day<Long, Long>("/day-fourteen.txt") {

    private val polymer: String = data.first()
    private val rules: List<Pair<String, String>> = mutableListOf<Pair<String, String>>().also {

        (2 until data.size).forEach { index ->

            it.add(data[index].split(" -> ").let { rule ->

                Pair(rule[0], rule[1])
            })
        }
    }.toList()

    override val partOneResult: Long
        get() {

            val pairCount = pairCount()
            repeat(10) {

                expandPolymer(pairCount)
            }
            return calculateResult(pairCount)
        }
    override val partTwoResult: Long
        get() {

            val pairCount = pairCount()
            repeat(40) {

                expandPolymer(pairCount)
            }
            return calculateResult(pairCount)
        }

    private fun pairCount() = rules.associate { it.first to 0L }.toMutableMap().also { map ->

        (0 until polymer.length - 1).map {

            val pair = polymer.substring(it, it + 2)
            map[pair] = map[pair]!! + 1
        }
    }

    private fun calculateResult(pairCount: MutableMap<String, Long>): Long {

        val characterCounts = mutableMapOf<Char, Long>()
        pairCount.forEach {

            characterCounts.putIfAbsent(it.key[0], 0)
            characterCounts.putIfAbsent(it.key[1], 0)

            characterCounts[it.key[0]] = characterCounts[it.key[0]]!! + it.value
            characterCounts[it.key[1]] = characterCounts[it.key[1]]!! + it.value
        }

        characterCounts.forEach { (key, count) ->

            characterCounts[key] = ceil(count / 2.0).toLong()
        }

        val min = characterCounts.minOf { it.value }
        val max = characterCounts.maxOf { it.value }

        return max - min
    }

    private fun expandPolymer(pairCount: MutableMap<String, Long>) {

        val newPairs = mutableMapOf<String, Long>()
        val currentPairs = pairCount.filter { it.value > 0 }
        currentPairs.forEach { (pair, count) ->

            generatePairs(pair, count).forEach {

                newPairs.putIfAbsent(it.first, 0)
                newPairs[it.first] = newPairs[it.first]!! + it.second
            }
        }

        pairCount.keys.forEach { pair ->

            pairCount[pair] = newPairs[pair] ?: 0
        }
    }

    private fun generatePairs(pair: String, count: Long): List<Pair<String, Long>> {

        val newChar = rules.first { it.first == pair }.second
        return listOf(Pair("${pair[0]}$newChar", count), Pair("$newChar${pair[1]}", count))
    }
}
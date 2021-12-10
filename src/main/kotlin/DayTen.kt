object DayTen : Day<Long, Long>("/day-ten.txt") {

    private val openingAndClosingChars = mapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
    private val syntaxScores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    private val autocompleteScores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    override val partOneResult: Long
    get() = scanForSyntaxErrors().errorScore

    override val partTwoResult: Long
        get() {

            return scanForAutoComplete().let { scores ->

                scores.sorted().forEachIndexed { index, score ->

                    println("$index = $score")
                }
                scores.sorted()[(scores.size - 1) / 2]
            }
        }

    private fun emptyStack(): Stack = Stack()
    class Stack {

        private val stack = mutableListOf<Char>()

        fun push(item: Char) = stack.add(0, item)

        fun pop() = stack.removeAt(0)

        fun print(openingAndClosingChars: Map<Char, Char>) = stack.map { openingAndClosingChars[it] }.joinToString("")
    }

    private fun scanForSyntaxErrors(): SyntaxErrors {

        val corruptLines = mutableListOf<String>()
        var errorScore = 0L
        data.forEach { line ->
            val stack = emptyStack()

            line.toCharArray().forEach { char ->

                if (openingAndClosingChars.keys.contains(char)) {

                    stack.push(char)
                } else {

                    val expected = stack.pop()
                    if (expected != openingAndClosingChars.filterValues { it == char }.keys.first()) {

                        corruptLines.add(line)
                        errorScore += syntaxScores[char] ?: 0
                    }
                }
            }
        }

        return SyntaxErrors(errorScore, corruptLines)
    }

    private fun scanForAutoComplete() =
        mutableListOf<Long>().also { scores ->

            val corruptLines = scanForSyntaxErrors().corruptLines
            data.filter { line -> !corruptLines.contains(line) }.forEach { line ->

                val stack = emptyStack()
                line.toCharArray().forEach { char ->

                    if (openingAndClosingChars.keys.contains(char)) {

                        stack.push(char)
                    } else {

                        stack.pop()
                    }
                }

                val autocomplete = stack.print(openingAndClosingChars)

                var autocompleteScore = 0L
                autocomplete.toCharArray().forEach { char ->

                    autocompleteScore *= 5
                    autocompleteScore += autocompleteScores[char] ?: 0
                }

                scores.add(autocompleteScore)
            }
        }

    data class SyntaxErrors(val errorScore: Long, val corruptLines: List<String>)
}
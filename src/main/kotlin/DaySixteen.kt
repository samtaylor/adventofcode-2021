import IO.readFile

class DaySixteen(data: String) {

    private val dataAsBinaryString = data.toBinaryString()

    val partOneResult: Long
        get() {

            return mutableListOf<Long>().also { versions ->

                parsePacket(0, versions)
            }.sum()
        }
    val partTwoResult: Long
        get() {

            val stack = Stack<Instruction>()

            parsePacket(0, stack = stack)

            return (stack.pop() as Value).value
        }

    private fun parsePacket(index: Int, versions: MutableList<Long>? = null, stack: Stack<Instruction>? = null): Int {

        var subPacketOffset: Int
        dataAsBinaryString.packageVersion(index).let { packageVersion ->

            dataAsBinaryString.typeId(packageVersion.offset).let { typeId ->

                if (typeId.value == 4L) {

                    val literalValue = dataAsBinaryString.literalValue(typeId.offset)
                    versions?.add(packageVersion.value)
                    stack?.push(Value(literalValue.value))
                    subPacketOffset =  literalValue.offset
                } else {

                    stack?.push(instructionForTypeId(typeId.value))

                    dataAsBinaryString.lengthTypeId(typeId.offset).let { lengthTypeId ->

                        if (lengthTypeId.value == 1L) {

                            val numberOfSubPackets = dataAsBinaryString.numberOfSubPackets(lengthTypeId.offset)
                            subPacketOffset = numberOfSubPackets.offset
                            repeat(numberOfSubPackets.value.toInt()) {

                                subPacketOffset = parsePacket(subPacketOffset, versions, stack)
                            }
                            versions?.add(packageVersion.value)
                        } else {

                            val lengthOfBitsOfSubPackets =
                                dataAsBinaryString.lengthOfBitsOfSubPackets(lengthTypeId.offset)
                            subPacketOffset = lengthOfBitsOfSubPackets.offset
                            var remainingBits = lengthOfBitsOfSubPackets.value
                            while (remainingBits > 0) {

                                val updatedSubPacketOffset = parsePacket(subPacketOffset, versions, stack)
                                remainingBits -= (updatedSubPacketOffset - subPacketOffset)
                                subPacketOffset = updatedSubPacketOffset
                            }
                            versions?.add(packageVersion.value)
                        }
                    }

                    stack?.let { stack ->

                        val values = mutableListOf<Value>()
                        while (stack.peak() is Value) {

                            values.add(0, stack.pop() as Value)
                        }

                        val instruction = stack.pop()
                        executeInstruction(instruction, values, stack)
                    }
                }
            }
        }

        return subPacketOffset
    }

    private fun executeInstruction(
        instruction: Instruction,
        values: MutableList<Value>,
        stack: Stack<Instruction>,
    ) {

        val result: Long = when (instruction) {

            Sum -> values.sumOf { it.value }
            Product -> values.map { it.value }.reduce { acc, value -> acc * value }
            Minimum -> values.minOf { it.value }
            Maximum -> values.maxOf { it.value }
            GreaterThan -> if (values[0].value > values[1].value) 1L else 0L
            LessThan -> if (values[0].value < values[1].value) 1L else 0L
            EqualTo -> if (values[0].value == values[1].value) 1L else 0L
            else -> throw RuntimeException()
        }

        stack.push(Value(result))
    }

    private fun String.toBinaryString() = StringBuilder().also { stringBuilder ->

        this.forEach { char ->

            stringBuilder.append(
                String
                    .format("%4s", Integer.toBinaryString(Integer.parseInt("$char", 16)))
                    .replace(' ', '0')
            )
        }
    }.toString()

    private fun String.packageVersion(index: Int) =
        ValueAndOffset(binaryToLong(substring(index, index + 3)), index + 3)

    private fun String.typeId(index: Int) =
        ValueAndOffset(binaryToLong(substring(index, index + 3)), index + 3)

    private fun String.literalValue(index: Int): ValueAndOffset {

        var currentIndex = index
        return ValueAndOffset(binaryToLong(StringBuilder().also { stringBuilder ->

            var done = false
            while (!done) {

                stringBuilder.append(substring(currentIndex + 1, currentIndex + 5))
                if (this[currentIndex] == '0') {

                    done = true
                }

                currentIndex += 5
            }
        }.toString()), currentIndex)
    }

    private fun String.lengthTypeId(index: Int) =
        ValueAndOffset(binaryToLong(substring(index, index + 1)), index + 1)

    private fun String.lengthOfBitsOfSubPackets(index: Int) =
        ValueAndOffset(binaryToLong(substring(index, index + 15)), index + 15)

    private fun String.numberOfSubPackets(index: Int) =
        ValueAndOffset(binaryToLong(substring(index, index + 11)), index + 11)

    private fun binaryToLong(string: String) = string.toLong(2)

    private fun instructionForTypeId(typeId: Long) = when (typeId.toInt()) {

        0       -> Sum
        1       -> Product
        2       -> Minimum
        3       -> Maximum
        5       -> GreaterThan
        6       -> LessThan
        7       -> EqualTo
        else    -> throw RuntimeException()
    }

    data class ValueAndOffset(val value: Long, val offset: Int)

    abstract class Instruction
    data class Value(val value: Long) : Instruction()
    object Sum : Instruction()
    object Product : Instruction()
    object Minimum : Instruction()
    object Maximum : Instruction()
    object GreaterThan : Instruction()
    object LessThan : Instruction()
    object EqualTo : Instruction()
}

fun daySixteen(filename: String) = DaySixteen(filename.readFile()[0])
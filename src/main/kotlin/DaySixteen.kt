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
        get() = TODO("Not yet implemented")

    private fun parsePacket(index: Int, versions: MutableList<Long>): Int {

        try {

            dataAsBinaryString.packageVersion(index).let { packageVersion ->

                debugPrintln("package version = $packageVersion")
                dataAsBinaryString.typeId(packageVersion.offset).let { typeId ->

                    debugPrintln("type id = $typeId")
                    if (typeId.value == 4L) {

                        val literalValue = dataAsBinaryString.literalValue(typeId.offset)
                        debugPrintln("literal value = $literalValue")
                        versions.add(packageVersion.value)
                        return literalValue.offset
                    } else {

                        dataAsBinaryString.lengthTypeId(typeId.offset).let { lengthTypeId ->

                            debugPrintln("length type id = ${lengthTypeId.value}")
                            if (lengthTypeId.value == 1L) {

                                val numberOfSubPackets = dataAsBinaryString.numberOfSubPackets(lengthTypeId.offset)
                                debugPrintln("number of sub packets = $numberOfSubPackets")
                                var subPacketOffset = numberOfSubPackets.offset
                                repeat(numberOfSubPackets.value.toInt()) {

                                    subPacketOffset = parsePacket(subPacketOffset, versions)
                                }
                                versions.add(packageVersion.value)
                                return subPacketOffset
                            } else {

                                val lengthOfBitsOfSubPackets =
                                    dataAsBinaryString.lengthOfBitsOfSubPackets(lengthTypeId.offset)
                                debugPrintln("length of bits of sub-packets = $lengthOfBitsOfSubPackets")
                                var subPacketOffset = lengthOfBitsOfSubPackets.offset
                                var remainingBits = lengthOfBitsOfSubPackets.value
                                while (remainingBits >= 0) {

                                    val bitsConsumed = parsePacket(subPacketOffset, versions)
                                    remainingBits -= (bitsConsumed - subPacketOffset)
                                    subPacketOffset = bitsConsumed
                                }
                                versions.add(packageVersion.value)
                                return subPacketOffset
                            }
                        }
                    }
                }
            }
        } catch (_: StringIndexOutOfBoundsException) { }

        return Int.MAX_VALUE
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

    data class ValueAndOffset(val value: Long, val offset: Int)
}

fun daySixteen(filename: String) = DaySixteen(filename.readFile()[0])
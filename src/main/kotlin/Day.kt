import IO.readFile

abstract class Day<T, U>(private val filename: String) {

    val data: List<String>
    get() = filename.readFile()

    abstract val partOneResult: T
    abstract val partTwoResult: U
}
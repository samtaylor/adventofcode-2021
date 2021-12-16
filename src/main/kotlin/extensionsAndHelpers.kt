import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

object IO {

    fun String.readFile(): List<String> =
        try {

            mutableListOf<String>().apply {

                val inputStream = javaClass.getResourceAsStream(this@readFile)
                with(BufferedReader(InputStreamReader(inputStream!!))) {

                    var line = readLine()
                    while (line != null) {

                        add(line)
                        line = readLine()
                    }
                }
            }
        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
}

fun <T> emptyStackOf(): Stack<T> = Stack()
class Stack<T> {

    private val stack = mutableListOf<T>()

    val size: Int
    get() = stack.size

    fun push(item: T) = stack.add(0, item)

    fun pop() = stack.removeAt(0)

    fun peak() = stack[0]

    fun contains(item: T) = stack.contains(item)

    fun <U> print(separator: String = ",", map: (T) -> U): String =
        stack.map(map).joinToString(separator)
}

fun debugPrintln(string: String) = println(string)
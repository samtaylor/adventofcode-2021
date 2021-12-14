fun <T> emptyStackOf(): Stack<T> = Stack()
class Stack<T> {

    private val stack = mutableListOf<T>()

    fun push(item: T) = stack.add(0, item)

    fun pop() = stack.removeAt(0)

    fun contains(item: T) = stack.contains(item)

    fun <U> print(separator: String = ",", map: (T) -> U): String =
        stack.map(map).joinToString(separator)
}

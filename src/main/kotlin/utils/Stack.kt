package utils

interface StackIface<T : Any> {
    fun push(element: T)
    fun pop(): T?

    fun peek(): T?

    val count: Int

    val isEmpty: Boolean
        get() = count == 0

}

class Stack<T : Any> : StackIface<T> {
    private val storage = arrayListOf<T>()

    companion object {
        fun <T : Any> create(items: Iterable<T>): Stack<T> {
            val stack = Stack<T>()
            for (item in items) {
                stack.push(item)
            }
            return stack
        }
    }
    override fun push(element: T) {
        storage.add(element)
    }

    override fun pop(): T? {
        if (isEmpty) {
            return null
        }
        return storage.removeAt(count - 1)
    }
   override fun peek(): T? {
        return storage.lastOrNull()
    }

    override val count: Int
        get() = storage.size

    override fun toString() = buildString {
        appendLine("----top----")
        storage.asReversed().forEach {
            appendLine("$it")
        }
        appendLine("-----------")
    }

}

fun <T : Any> stackOf(vararg elements: T): Stack<T> {
    return Stack.create(elements.asList())
}
package utils.tree

class TreeNode<T>(val value: T) {
    private val children: MutableList<TreeNode<T>> = mutableListOf()

    fun add(child: TreeNode<T>) = children.add(child)

    fun forEachDepthFirst(visit: (TreeNode<T>) -> Unit) {
        visit(this)
        children.forEach {
            it.forEachDepthFirst(visit)
        }
    }

    fun forEachLevelOrder(visit: (TreeNode<T>) -> Unit) {
        visit(this)
        val queue = ArrayDeque<TreeNode<T>>()
        children.forEach { queue.addFirst(it) }

        while (queue.isNotEmpty()) {
            val node = queue.removeLast()
            visit(node)
            node.children.forEach { queue.addFirst(it) }
        }
    }

    fun search(value: T): TreeNode<T>? {
        var result: TreeNode<T>? = null

        forEachLevelOrder {
            if (it.value == value) {
                result = it
            }
        }

        return result
    }
}

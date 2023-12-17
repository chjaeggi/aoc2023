interface Graph<T: Any> {

  val allVertices: ArrayList<Vertex<T>>

  fun createVertex(data: T): Vertex<T>
  fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?)
  fun addUndirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
    addDirectedEdge(source, destination, weight)
    addDirectedEdge(destination, source, weight)
  }

  fun add(edge: EdgeType, source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
    when (edge) {
      EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
      EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
    }
  }

  fun edges(source: Vertex<T>): ArrayList<Edge<T>>
  fun weight(source: Vertex<T>, destination: Vertex<T>): Double?

  fun numberOfPaths(source: Vertex<T>, destination: Vertex<T>): Int {
    val numberOfPaths = Ref(0)
    val visited: ArrayList<Vertex<T>> = arrayListOf()
    paths(source, destination, visited, numberOfPaths)
    return numberOfPaths.value
  }

  fun paths(source: Vertex<T>, destination: Vertex<T>, visited: ArrayList<Vertex<T>>, pathCount: Ref<Int>) {
    visited.add(source)
    if (source == destination) {
      pathCount.value += 1
    } else {
      val neighbors = edges(source)
      neighbors.forEach { edge ->
        if (!visited.contains(edge.destination)) {
          paths(edge.destination, destination, visited, pathCount)
        }
      }
    }
    visited.remove(source)
  }

  fun bfsIterative(source: Vertex<T>): ArrayList<Vertex<T>> {
    val queue = ArrayDeque<Vertex<T>>()
    val enqueued = ArrayList<Vertex<T>>()
    val visited = ArrayList<Vertex<T>>()
    queue.addFirst(source)
    enqueued.add(source)
    while (queue.isNotEmpty()) {
      val vertex = queue.removeLast()
      visited.add(vertex)
      val neighborEdges = edges(vertex)
      neighborEdges.forEach {
        if (!enqueued.contains(it.destination)) {
          queue.addFirst(it.destination)
          enqueued.add(it.destination)
        }
      }
    }
    return visited
  }

  fun bfsRecursive(source: Vertex<T>): ArrayList<Vertex<T>> {
    val queue = ArrayDeque<Vertex<T>>()
    val enqueued = mutableSetOf<Vertex<T>>()
    val visited = arrayListOf<Vertex<T>>()

    queue.addFirst(source)
    enqueued.add(source)

    bfsRecursive(queue, enqueued, visited)

    return visited
  }

  private fun bfsRecursive(queue: ArrayDeque<Vertex<T>>, enqueued: MutableSet<Vertex<T>>, visited: ArrayList<Vertex<T>>) {
    if (queue.isEmpty()) return
    val vertex = queue.removeLast()

    visited.add(vertex)

    val neighborEdges = edges(vertex)
    neighborEdges.forEach {
      if (!enqueued.contains(it.destination)) {
        queue.addFirst(it.destination)
        enqueued.add(it.destination)
      }
    }

    bfsRecursive(queue, enqueued, visited)
  }

  fun isDisconnected(): Boolean {
    val firstVertex = allVertices.firstOrNull() ?: return false

    val visited = bfsIterative(firstVertex)
    allVertices.forEach {
      if (!visited.contains(it)) return true
    }

    return false
  }

  fun dfsIterative(source: Vertex<T>): ArrayList<Vertex<T>> {
    val stack = ArrayDeque<Vertex<T>>()
    val visited = arrayListOf<Vertex<T>>()
    val pushed = mutableSetOf<Vertex<T>>()

    stack.addLast(source)
    pushed.add(source)
    visited.add(source)

    outer@ while (stack.isNotEmpty()) {
      val vertex = stack.last()
      val neighbors = edges(vertex)

      if (neighbors.isEmpty()) {
        stack.removeLast()
        continue
      }

      for (i in 0 until neighbors.size) {
        val destination = neighbors[i].destination
        if (destination !in pushed) {
          stack.addLast(destination)
          pushed.add(destination)
          visited.add(destination)
          continue@outer
        }
      }
      stack.removeLast()
    }

    return visited
  }

  fun dfsRecursive(start: Vertex<T>): ArrayList<Vertex<T>> {
    val visited = arrayListOf<Vertex<T>>()
    val pushed = mutableSetOf<Vertex<T>>()

    dfsRecursive(start, visited, pushed)

    return visited
  }

  fun dfsRecursive(
    source: Vertex<T>,
    visited: ArrayList<Vertex<T>>,
    pushed: MutableSet<Vertex<T>>
  ) {
    pushed.add(source)
    visited.add(source)

    val neighbors = edges(source)
    neighbors.forEach {
      if (it.destination !in pushed) {
        dfsRecursive(it.destination, visited, pushed)
      }
    }
  }

  fun hasCycle(source: Vertex<T>): Boolean {
    val pushed = mutableSetOf<Vertex<T>>()
    return hasCycle(source, pushed)
  }

  fun hasCycle(source: Vertex<T>, pushed: MutableSet<Vertex<T>>): Boolean {
    pushed.add(source)

    val neighbors = edges(source)
    neighbors.forEach {
      if (it.destination !in pushed && hasCycle(it.destination, pushed)) {
        return true
      } else if (it.destination in pushed) {
        return true
      }
    }

    pushed.remove(source)
    return false
  }

}

enum class EdgeType {
  DIRECTED,
  UNDIRECTED
}
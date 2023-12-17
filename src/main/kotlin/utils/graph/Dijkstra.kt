package utils.graph

import AdjacencyList
import Edge
import Vertex
import java.util.PriorityQueue
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Dijkstra<T : Any>(private val graph: AdjacencyList<T>) {

    private fun route(
        destination: Vertex<T>,
        paths: HashMap<Vertex<T>, Visit<T>>
    ): ArrayList<Edge<T>> {
        var vertex = destination
        val path = arrayListOf<Edge<T>>()

        loop@ while (true) {
            val visit = paths[vertex] ?: break

            when (visit.type) {
                VisitType.START -> break@loop
                VisitType.EDGE -> visit.edge?.let {
                    path.add(it)
                    vertex = it.source
                }
            }
        }

        return path
    }

    private fun distance(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): Double {
        val path = route(destination, paths)
        return path.sumOf { it.weight ?: 0.0 }
    }

    private fun shortestPath(start: Vertex<T>): HashMap<Vertex<T>, Visit<T>> {
        val paths: HashMap<Vertex<T>, Visit<T>> = HashMap()
        paths[start] = Visit(VisitType.START)

        val distanceComparator = Comparator<Vertex<T>> { first, second ->
            (distance(second, paths) - distance(first, paths)).toInt()
        }

        val priorityQueue = PriorityQueue(distanceComparator)
        priorityQueue.add(start)

        while (priorityQueue.isNotEmpty()) {
            val vertex = priorityQueue.remove()
            val edges = graph.edges(vertex)

            edges.forEach {
                val weight = it.weight ?: return@forEach

                if (paths[it.destination] == null
                    || distance(vertex, paths) + weight < distance(it.destination, paths)
                ) {
                    paths[it.destination] = Visit(VisitType.EDGE, it)
                    priorityQueue.add(it.destination)
                }
            }
        }

        return paths
    }

    fun shortestPath(
        destination: Vertex<T>,
        paths: HashMap<Vertex<T>, Visit<T>>
    ): ArrayList<Edge<T>> {
        return route(destination, paths)
    }

    fun getAllShortestPath(source: Vertex<T>): HashMap<Vertex<T>, ArrayList<Edge<T>>> {
        val paths = HashMap<Vertex<T>, ArrayList<Edge<T>>>()
        val pathsFromSource = shortestPath(source)

        graph.vertices.forEach {
            val path = shortestPath(it, pathsFromSource)
            paths[it] = path
        }

        return paths
    }

}

class Visit<T : Any>(val type: VisitType, val edge: Edge<T>? = null)

enum class VisitType {
    START,
    EDGE
}
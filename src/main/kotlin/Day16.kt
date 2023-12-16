import Orientation.EAST
import Orientation.SOUTH
import Orientation.NORTH
import Orientation.WEST
import utils.execFileByLineIndexed
import utils.numberOfCharsPerLine
import utils.numberOfLinesPerFile

private enum class Orientation {
    NORTH, WEST, SOUTH, EAST
}

private data class Beam(
    val x: Int,
    val y: Int,
    val orientation: Orientation,
)

class Day16 {

    private val _0 = '-'
    private val _45 = '/'
    private val _90 = '|'
    private val _135 = '\\'

    private val width = numberOfCharsPerLine(16)
    private val height = numberOfLinesPerFile(16)

    private val cave = Array(height) { CharArray(width) }

    init {
        execFileByLineIndexed(16) { line, index ->
            line.forEachIndexed { i, v ->
                cave[index][i] = v
            }
        }
    }

    fun solveFirst(): Int {
        val energizedFields =
            Array(height) { Array<MutableList<Orientation>>(width) { mutableListOf() } }
        Beam(0, 0, EAST).run(energizedFields)
        return energizedFields.map { it.count { it.isNotEmpty() } }.sum()
    }

    fun solveSecond(): Int {
        return runBeams()
    }

    private fun Beam.run(energizedFields: Array<Array<MutableList<Orientation>>>) {
        energize(energizedFields)
        val nextBeams =
            getNextBeams().filter { it.inBounds() && !it.hasEnergizedAlready(energizedFields) }
        nextBeams.forEach { it.run(energizedFields) }
    }

    private fun Beam.energize(energizedFields: Array<Array<MutableList<Orientation>>>) {
        energizedFields[y][x].add(orientation)
    }

    private fun Beam.inBounds(): Boolean {
        return !(x < 0 || y < 0 || x > cave[0].lastIndex || y > cave.lastIndex)
    }

    private fun Beam.hasEnergizedAlready(energizedFields: Array<Array<MutableList<Orientation>>>): Boolean {
        return energizedFields[y][x].contains(orientation)
    }

    private fun Beam.getNextBeams(): List<Beam> {
        val currentPosition = cave[y][x]
        val north = Beam(x, y - 1, NORTH)
        val west = Beam(x - 1, y, WEST)
        val south = Beam(x, y + 1, SOUTH)
        val east = Beam(x + 1, y, EAST)
        when (currentPosition) {
            _0 -> {
                return when (orientation) {
                    NORTH, SOUTH -> listOf(west, east)
                    WEST -> listOf(west)
                    EAST -> listOf(east)
                }
            }

            _45 -> {
                return when (orientation) {
                    NORTH -> listOf(east)
                    WEST -> listOf(south)
                    SOUTH -> listOf(west)
                    EAST -> listOf(north)
                }
            }

            _90 -> {
                return when (orientation) {
                    NORTH -> listOf(north)
                    WEST, EAST -> listOf(north, south)
                    SOUTH -> listOf(south)
                }
            }

            _135 -> {
                return when (orientation) {
                    NORTH -> listOf(west)
                    WEST -> listOf(north)
                    SOUTH -> listOf(east)
                    EAST -> listOf(south)
                }
            }

            else -> {
                return when (orientation) {
                    NORTH -> listOf(north)
                    WEST -> listOf(west)
                    SOUTH -> listOf(south)
                    EAST -> listOf(east)
                }
            }
        }
    }

    private fun runBeams(): Int {
        var max = 0

        val runParams = mapOf(
            EAST to listOf(0, -1, 0, height - 1),
            SOUTH to listOf(-1, 0, 0, width),
            NORTH to listOf(-1, height - 1, 0, width),
            WEST to listOf(0, height - 1, 0, height)
        )

        Orientation.entries.forEach { direction ->
            for (i in runParams[direction]!![2]..<runParams[direction]!![3]) {
                val energizedFields =
                    Array(height) { Array<MutableList<Orientation>>(width) { mutableListOf() } }
                val a = if (runParams[direction]!![0] == -1) i else runParams[direction]!![0]
                val b = if (runParams[direction]!![1] == -1) i else runParams[direction]!![1]
                Beam(a, b, direction).run(energizedFields)
                max = maxOf(max, energizedFields.map { it.count { it.isNotEmpty() } }.sum())
            }
        }
        return max
    }
}
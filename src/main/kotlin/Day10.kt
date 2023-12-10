import utils.execFileByLineIndexed
import utils.numberOfCharsPerLine
import utils.numberOfLinesPerFile
import java.lang.IllegalStateException

enum class Direction {
    NORTH, EAST, SOUTH, WEST, NONE
}

class Day10 {

    private val fileName = "./inputs/input10.txt"

    private var startX = -1
    private var startY = -1

    private val oppositeDirections = mapOf(
        Direction.NORTH to Direction.SOUTH,
        Direction.SOUTH to Direction.NORTH,
        Direction.EAST to Direction.WEST,
        Direction.WEST to Direction.EAST,
    )
    private var pipes =
        Array(numberOfLinesPerFile(fileName)) { CharArray(numberOfCharsPerLine(fileName)) }
    private var pipeCount = 1

    // part 2
    private var visited = mutableSetOf<Pair<Int, Int>>()
    private var isInside = false
    private val markUp = "SF|7"
    private var insideCount = 0

    fun solve() {
        execFileByLineIndexed(fileName) { line, index ->
            line.forEachIndexed { i, v ->
                if (v == 'S') {
                    startX = i
                    startY = index
                }
                pipes[index][i] = v
            }

        }
        checkNext(startX, startY)
        println("Solution 1: ${pipeCount / 2}")

        // part 2:
        // this helped me a lot in part two: https://imgur.com/a/ukstWKO
        pipes.forEachIndexed { y, value ->
            isInside = false
            value.forEachIndexed { x, char ->
                if (char in markUp && visited.contains(x to y)) {
                    isInside = !isInside
                } else if (isInside && !visited.contains(x to y)) {
                    insideCount++
                }
            }
        }
        println("Solution 2: $insideCount")
    }

    private fun checkNext(
        x: Int,
        y: Int,
        direction: Direction = Direction.NONE
    ) {
        return if (startX == x && startY == y && pipeCount > 1) {
            return
        } else {
            val next = visitNext(x, y, direction)
            visited.add(next.first)
            pipeCount++
            checkNext(next.first.first, next.first.second, next.second)
        }
    }

    private fun visitNext(
        x: Int,
        y: Int,
        direction: Direction
    ): Pair<Pair<Int, Int>, Direction> {
        val currentPipe = pipes[y][x]

        if (y > 0 && oppositeDirections[direction] != Direction.NORTH) {
            val nextPipe = pipes[y - 1][x]
            if (currentPipe in "S|LJ" && nextPipe in "S|F7") {
                return (x to y - 1) to Direction.NORTH
            }
        }

        if (x < pipes[0].lastIndex && oppositeDirections[direction] != Direction.EAST) {
            val nextPipe = pipes[y][x + 1]
            if (currentPipe in "S-FL" && nextPipe in "S-J7") {
                return (x + 1 to y) to Direction.EAST
            }
        }

        if (y < pipes.lastIndex && oppositeDirections[direction] != Direction.SOUTH) {
            val nextPipe = pipes[y + 1][x]
            if (currentPipe in "S|F7" && nextPipe in "S|JL") {
                return (x to y + 1) to Direction.SOUTH
            }
        }

        if (x > 0 && oppositeDirections[direction] != Direction.WEST) {
            val nextPipe = pipes[y][x - 1]
            if (currentPipe in "S-7J" && nextPipe in "S-FL") {
                return (x - 1 to y) to Direction.WEST
            }
        }

        throw IllegalStateException("not possible")
    }
}
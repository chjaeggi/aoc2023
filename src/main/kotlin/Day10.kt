import utils.execFileByLineIndexed
import utils.numberOfCharsPerLine
import utils.numberOfLinesPerFile
import java.lang.IllegalStateException

enum class Direction {
    NORTH, EAST, SOUTH, WEST, NONE
}

private data class ReachablePoint(
    val x: Int,
    val y: Int,
    val reachedFrom: Direction,
)

class Day10 {

    private val fileName = "./inputs/input10.txt"

    private var startX = -1
    private var startY = -1
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
        checkNext(ReachablePoint(startX, startY, Direction.NONE))
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

    private tailrec fun checkNext(p: ReachablePoint) {
        if (startX == p.x && startY == p.y && pipeCount > 1) {
            return
        } else {
            val next = visitNext(p)
            visited.add(next.x to next.y)
            pipeCount++
            checkNext(next)
        }
    }

    private fun visitNext(p: ReachablePoint): ReachablePoint {
        val currentPipe = pipes[p.y][p.x]

        if (p.y > 0 && p.reachedFrom != Direction.NORTH) {
            val nextPipe = pipes[p.y - 1][p.x]
            if (currentPipe in "S|LJ" && nextPipe in "S|F7") {
                return ReachablePoint(p.x, p.y - 1, Direction.SOUTH)
            }
        }

        if (p.x < pipes[0].lastIndex && p.reachedFrom != Direction.EAST) {
            val nextPipe = pipes[p.y][p.x + 1]
            if (currentPipe in "S-FL" && nextPipe in "S-J7") {
                return ReachablePoint(p.x + 1, p.y, Direction.WEST)
            }
        }

        if (p.y < pipes.lastIndex && p.reachedFrom != Direction.SOUTH) {
            val nextPipe = pipes[p.y + 1][p.x]
            if (currentPipe in "S|F7" && nextPipe in "S|JL") {
                return ReachablePoint(p.x, p.y + 1, Direction.NORTH)
            }
        }

        if (p.x > 0 && p.reachedFrom != Direction.WEST) {
            val nextPipe = pipes[p.y][p.x - 1]
            if (currentPipe in "S-7J" && nextPipe in "S-FL") {
                return ReachablePoint(p.x - 1, p.y, Direction.EAST)
            }
        }

        throw IllegalStateException("not possible")
    }
}
import utils.execFileByLineIndexed
import utils.numberOfCharsPerLine
import utils.numberOfLinesPerFile
import java.lang.IllegalArgumentException

enum class Direction {
    NORTH, EAST, SOUTH, WEST, NONE
}

class Day10 {

    private val fileName = "./inputs/input10.txt"

    private var isFirst = true
    private var currentX = -1
    private var currentY = -1
    private var startX = -1
    private var startY = -1

    private var direction = Direction.NONE
    private val width = numberOfCharsPerLine(fileName)
    private val height = numberOfLinesPerFile(fileName)
    private var pipes = Array(height) { CharArray(width) }
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
                    currentX = i
                    startY = index
                    currentY = index
                }
                pipes[index][i] = v
            }

        }
        traverseNext()
        println("Solution 1: ${pipeCount / 2}")

        // part 2:
        // this helped me a lot in part two: https://imgur.com/a/ukstWKO
        pipes.forEachIndexed { y, value ->
            isInside = false
            value.forEachIndexed { x, char ->
                if (char in markUp && visited.contains(x to y)) {
                    isInside = !isInside
                }
                else if (isInside && !visited.contains(x to y)) {
                    insideCount++
                }
            }
        }
        println("Solution 2: $insideCount")
    }

    private fun traverseNext(): Boolean {
        return if (startX == currentX && startY == currentY && !isFirst) {
            true
        } else {
            isFirst = false
            getNextPipe()
            traverseNext()
        }
    }

    private fun getNextPipe() {
        val allowedDirections = mutableListOf(
            Direction.NORTH,
            Direction.EAST,
            Direction.SOUTH,
            Direction.WEST
        )
        when (direction) {
            Direction.NORTH -> allowedDirections.removeAll {
                it == Direction.SOUTH
            }

            Direction.EAST -> allowedDirections.removeAll {
                it == Direction.WEST
            }

            Direction.SOUTH -> allowedDirections.removeAll {
                it == Direction.NORTH
            }

            Direction.WEST -> allowedDirections.removeAll {
                it == Direction.EAST
            }

            Direction.NONE -> {}
        }
        if (currentX <= 0) {
            allowedDirections.removeAll {
                it == Direction.WEST
            }
        }
        if (currentY <= 0) {
            allowedDirections.removeAll {
                it == Direction.NORTH
            }
        }
        if (currentX >= width - 1) {
            allowedDirections.removeAll {
                it == Direction.EAST
            }
        }
        if (currentY >= height - 1) {
            allowedDirections.removeAll {
                it == Direction.SOUTH
            }
        }
        for (possibleDirection in allowedDirections) {
            val currentPipe = pipes[currentY][currentX]
            when (possibleDirection) {
                Direction.NORTH -> {
                    val nextPipe = pipes[currentY - 1][currentX]
                    if (currentPipe in "S|LJ" && nextPipe in "S|F7") {
                        currentY--
                        direction = Direction.NORTH
                        break
                    }
                }

                Direction.EAST -> {
                    val nextPipe = pipes[currentY][currentX + 1]
                    if (currentPipe in "S-FL" && nextPipe in "S-J7") {
                        currentX++
                        direction = Direction.EAST
                        break
                    }
                }

                Direction.SOUTH -> {
                    val nextPipe = pipes[currentY + 1][currentX]
                    if (currentPipe in "S|F7" && nextPipe in "S|JL") {
                        currentY++
                        direction = Direction.SOUTH
                        break
                    }
                }

                Direction.WEST -> {
                    val nextPipe = pipes[currentY][currentX - 1]
                    if (currentPipe in "S-7J" && nextPipe in "S-FL") {
                        currentX--
                        direction = Direction.WEST
                        break
                    }
                }

                else -> {
                    throw IllegalArgumentException("Not possible")
                }
            }
        }
        visited.add(Pair(currentX, currentY))
        pipeCount++
    }
}
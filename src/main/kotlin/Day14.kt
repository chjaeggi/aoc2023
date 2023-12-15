import utils.*
import java.util.*

class Day14 {
    private val fileName = "./inputs/input14.txt"
    fun solveFirst(): Int {

        val height = numberOfLinesPerFile(fileName)
        val width = numberOfCharsPerLine(fileName)
        val tiltingRockLayout =
            Array(height) { CharArray(width) }

        execFileByLine(fileName) {
            execFileByLineIndexed(fileName) { line, index ->
                line.forEachIndexed { i, v ->
                    tiltingRockLayout[index][i] = v
                }
            }
        }

        var awayFromSouth = height + 1
        return tilt(tiltingRockLayout).map {
            awayFromSouth--
            it.count { it == 'O' } * awayFromSouth
        }.sum()
    }

    fun solveSecond(): Int {

        val height = numberOfLinesPerFile(fileName)
        val width = numberOfCharsPerLine(fileName)
        val tiltingRockLayout =
            Array(height) { CharArray(width) }

        execFileByLine(fileName) {
            execFileByLineIndexed(fileName) { line, index ->
                line.forEachIndexed { i, v ->
                    tiltingRockLayout[index][i] = v
                }
            }
        }

        var awayFromSouth = height + 1
        return tilt(tiltingRockLayout, 1000000000).map {
            awayFromSouth--
            it.count { it == 'O' } * awayFromSouth
        }.sum()
    }

    private fun tilt(
        tiltingRockLayout: Array<CharArray>,
        numberOfCycles: Int = 0
    ): Array<CharArray> {
        var tiltedRockLayout = tiltingRockLayout.copyOf()

        if (numberOfCycles <= 0) {
            tiltedRockLayout.transpose().forEachIndexed { lineNo, chars ->
                val roundsUntilSquare = getRoundsUntilSquare(chars)
                var current = 0
                roundsUntilSquare.forEach { entry ->
                    val numberOfRounds = entry.value
                    val squareIndex = entry.key
                    for (i in current..<current + numberOfRounds) {
                        tiltedRockLayout[lineNo][i] = 'O'
                    }
                    current += numberOfRounds
                    if (squareIndex <= tiltedRockLayout[lineNo].lastIndex) {
                        for (i in current..<squareIndex) {
                            tiltedRockLayout[lineNo][i] = '.'
                        }
                        current = squareIndex + 1
                        tiltedRockLayout[lineNo][squareIndex] = '#'

                    } else {
                        for (i in current..tiltedRockLayout[lineNo].lastIndex) {
                            tiltedRockLayout[lineNo][i] = '.'
                        }
                    }
                }
            }
            return tiltedRockLayout.transpose()

        } else {
            val actualPreambleAndCycles = getPreambleAndCycles(numberOfCycles, tiltedRockLayout)
            tiltedRockLayout = tiltingRockLayout.copyOf()
            val first = actualPreambleAndCycles.first
            val cycle = actualPreambleAndCycles.second
            repeat(((numberOfCycles - first) % cycle) + first) {
                tiltedRockLayout = executeCycle(tiltedRockLayout)
            }
            return tiltedRockLayout
        }
    }

    private fun getPreambleAndCycles(
        numberOfCycles: Int,
        tiltedRockLayout: Array<CharArray>
    ): Pair<Int, Int> {
        var tiltedRockLayout1 = tiltedRockLayout
        val hashes = mutableListOf<Int>()
        var firstFound = false
        var first = 0
        var cycleFound = false
        var cycle = 0
        run repeatBlock@{
            repeat(numberOfCycles) {
                if (!firstFound) {
                    if (!hashes.contains(tiltedRockLayout1.contentDeepHashCode())) {
                        hashes.add(tiltedRockLayout1.contentDeepHashCode())
                    } else {
                        first = it
                        firstFound = true
                        hashes.clear()
                    }
                }
                if (!cycleFound && firstFound) {
                    if (!hashes.contains(tiltedRockLayout1.contentDeepHashCode())) {
                        hashes.add(tiltedRockLayout1.contentDeepHashCode())
                    } else {
                        cycle = it - first
                        cycleFound = true
                        return@repeatBlock
                    }
                }
                tiltedRockLayout1 = executeCycle(tiltedRockLayout1)
            }
        }
        return Pair(first,cycle)
    }

    private fun executeCycle(tiltedRockLayout: Array<CharArray>): Array<CharArray> {
        // NORTH -----------------------------------------------
        var tiltedRockLayout1 = tiltedRockLayout
        tiltedRockLayout1 = tiltedRockLayout1.transpose()
        tiltedRockLayout1.forEachIndexed { lineNo, chars ->
            val roundsUntilSquare = getRoundsUntilSquare(chars)
            var current = 0
            roundsUntilSquare.forEach { entry ->
                val numberOfRounds = entry.value
                val squareIndex = entry.key
                for (i in current..<current + numberOfRounds) {
                    tiltedRockLayout1[lineNo][i] = 'O'
                }
                current += numberOfRounds
                if (squareIndex <= tiltedRockLayout1[lineNo].lastIndex) {
                    for (i in current..<squareIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                    current = squareIndex + 1
                    tiltedRockLayout1[lineNo][squareIndex] = '#'

                } else {
                    for (i in current..tiltedRockLayout1[lineNo].lastIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                }
            }
        }
        tiltedRockLayout1 = tiltedRockLayout1.transpose()
        // END NORTH -------------------------------------------

        // WEST ------------------------------------------------
        tiltedRockLayout1.forEachIndexed { lineNo, chars ->
            val roundsUntilSquare = getRoundsUntilSquare(chars)
            var current = 0
            roundsUntilSquare.forEach { entry ->
                val numberOfRounds = entry.value
                val squareIndex = entry.key
                for (i in current..<current + numberOfRounds) {
                    tiltedRockLayout1[lineNo][i] = 'O'
                }
                current += numberOfRounds
                if (squareIndex <= tiltedRockLayout1[lineNo].lastIndex) {
                    for (i in current..<squareIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                    current = squareIndex + 1
                    tiltedRockLayout1[lineNo][squareIndex] = '#'

                } else {
                    for (i in current..tiltedRockLayout1[lineNo].lastIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                }
            }
        }
        // END WEST --------------------------------------------

        // SOUTH -----------------------------------------------
        tiltedRockLayout1 = tiltedRockLayout1.reversedArray()
        tiltedRockLayout1 = tiltedRockLayout1.transpose()
        tiltedRockLayout1.forEachIndexed { lineNo, chars ->
            val roundsUntilSquare = getRoundsUntilSquare(chars)
            var current = 0
            roundsUntilSquare.forEach { entry ->
                val numberOfRounds = entry.value
                val squareIndex = entry.key
                for (i in current..<current + numberOfRounds) {
                    tiltedRockLayout1[lineNo][i] = 'O'
                }
                current += numberOfRounds
                if (squareIndex <= tiltedRockLayout1[lineNo].lastIndex) {
                    for (i in current..<squareIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                    current = squareIndex + 1
                    tiltedRockLayout1[lineNo][squareIndex] = '#'

                } else {
                    for (i in current..tiltedRockLayout1[lineNo].lastIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                }
            }
        }
        tiltedRockLayout1 = tiltedRockLayout1.transpose()
        tiltedRockLayout1 = tiltedRockLayout1.reversedArray()
        // END SOUTH -------------------------------------------

        // EAST ------------------------------------------------
        tiltedRockLayout1.forEachIndexed { lineNo, chars ->
            val roundsUntilSquare = getRoundsUntilSquare(chars.reversedArray())
            var current = 0
            roundsUntilSquare.forEach { entry ->
                val numberOfRounds = entry.value
                val squareIndex = entry.key
                for (i in current..<current + numberOfRounds) {
                    tiltedRockLayout1[lineNo][i] = 'O'
                }
                current += numberOfRounds
                if (squareIndex <= tiltedRockLayout1[lineNo].lastIndex) {
                    for (i in current..<squareIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                    current = squareIndex + 1
                    tiltedRockLayout1[lineNo][squareIndex] = '#'

                } else {
                    for (i in current..tiltedRockLayout1[lineNo].lastIndex) {
                        tiltedRockLayout1[lineNo][i] = '.'
                    }
                }
            }
            tiltedRockLayout1[lineNo] = tiltedRockLayout1[lineNo].reversedArray()
        }
        // END EAST --------------------------------------------
        return tiltedRockLayout1
    }

    private fun getRoundsUntilSquare(line: CharArray): Map<Int, Int> {
        var roundCount = 0
        val roundsUntilSquare = mutableMapOf<Int, Int>()
        for ((idx, char) in line.withIndex()) {
            if (char == 'O') {
                roundCount++
                continue
            }
            if (char == '.') {
                continue
            }
            if (char == '#') {
                roundsUntilSquare[idx] = roundCount
                roundCount = 0
            }
        }
        roundsUntilSquare[line.size] = roundCount

        return roundsUntilSquare
    }
}
import utils.execFileByLine

class Day3() {
    fun solveFirst() {
        data class Coords(val range: IntRange, val line: Int)

        fun array2dOfChar(sizeOuter: Int, sizeInner: Int): Array<CharArray> =
            Array(sizeOuter) { CharArray(sizeInner) }

        var line = 0
        var parts = 0
        val numbers = "[0-9]+".toRegex()
        val numbersByLine = mutableMapOf<Coords, Int>()
        val twoD = array2dOfChar(140, 140)
        execFileByLine("inputs/input3.txt") {
            numbers.findAll(it).forEach {
                numbersByLine[Coords(it.range, line)] = it.value.toInt()
            }
            var j = 0
            it.forEach {
                twoD[line][j] = it
                j++
            }
            line++
        }
        loop@ for ((key, value) in numbersByLine) {
            val current = key.line
            val top = if (key.line == 0) 0 else key.line - 1
            val bottom = if (key.line == twoD.size - 1) twoD.size - 1 else key.line + 1
            val left = if (key.range.first == 0) 0 else key.range.first - 1
            val right =
                if (key.range.last == twoD[0].size - 1) twoD[0].size - 1 else key.range.last + 1

            if (twoD[current][left] != '.' && !twoD[current][left].isDigit()) {
                parts += value
                continue@loop
            }
            if (twoD[current][right] != '.' && !twoD[current][right].isDigit()) {
                parts += value
                continue@loop
            }
            for (i in left..right) {
                if (twoD[top][i] != '.' && !twoD[top][i].isDigit()) {
                    parts += value
                    continue@loop
                }
            }
            for (i in left..right) {
                if (twoD[bottom][i] != '.' && !twoD[bottom][i].isDigit()) {
                    parts += value
                    continue@loop
                }
            }
        }

        println(parts)
    }

    fun solveSecond() {

        data class Coords(
            val range: IntRange,
            val line: Int,
            var starPosition: Pair<Int, Int> = Pair(-1, -1)
        )

        fun array2dOfChar(sizeOuter: Int, sizeInner: Int): Array<CharArray> =
            Array(sizeOuter) { CharArray(sizeInner) }

        var line = 0
        var parts = 0
        val numbers = "[0-9]+".toRegex()
        val numbersByLine = mutableMapOf<Coords, Int>()
        val twoD = array2dOfChar(140, 140)
        execFileByLine("inputs/input3.txt") {
            numbers.findAll(it).forEach {
                numbersByLine[Coords(it.range, line)] = it.value.toInt()
            }
            var j = 0
            it.forEach {
                twoD[line][j] = it
                j++
            }
            line++
        }
        loop@ for ((key, value) in numbersByLine) {
            val current = key.line
            val top = if (key.line == 0) 0 else key.line - 1
            val bottom = if (key.line == twoD.size - 1) twoD.size - 1 else key.line + 1
            val left = if (key.range.first == 0) 0 else key.range.first - 1
            val right =
                if (key.range.last == twoD[0].size - 1) twoD[0].size - 1 else key.range.last + 1

            if (twoD[current][left] == '*') {
                key.starPosition = Pair(left, current)
                continue@loop
            }
            if (twoD[current][right] == '*') {
                key.starPosition = Pair(right, current)
                continue@loop
            }
            for (i in left..right) {
                if (twoD[top][i] == '*') {
                    key.starPosition = Pair(i, top)
                    continue@loop
                }
            }
            for (i in left..right) {
                if (twoD[bottom][i] == '*') {
                    key.starPosition = Pair(i, bottom)
                    continue@loop
                }
            }
        }

        val filtered = numbersByLine.filterNot {
            it.key.starPosition == Pair(-1, -1)
        }

        val gearMap = mutableMapOf<Pair<Int, Int>, Int>()
        for (item in filtered) {
            if (item.key.starPosition !in gearMap) {
                gearMap[item.key.starPosition] = item.value
            } else {
                parts += gearMap[item.key.starPosition]!! * item.value
            }
        }
        println(parts)
    }
}
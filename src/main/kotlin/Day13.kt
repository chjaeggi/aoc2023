import utils.Stack
import utils.execFileByLineIndexed
import utils.numberOfLinesPerFile
import utils.stackOf

class Day13 {

    fun solveFirst(): Int {
        // could have used kotlin.collections.ArrayDeque instead of my own Stack implementation :/
        // with:
        // Stack.push = addLast()
        // Stack.pop = removeLast()
        val (patternRows, patternCols) = createInput()

        var rowSolution = 0
        var colSolution = 0

        patternRows.forEach { rowPattern ->
            var stackRow = Stack<String>()
            var searchIndexRow = -1
            var possibleFindRow: Int? = null
            var isPushingRow = true
            for (p in rowPattern) {
                searchIndexRow++
                if (stackRow.peek() == p) {
                    stackRow.pop()
                    if (possibleFindRow == null) {
                        possibleFindRow = searchIndexRow
                    }
                    isPushingRow = false
                    if (stackRow.isEmpty) {
                        searchIndexRow = possibleFindRow
                        break
                    }
                } else {
                    if (!isPushingRow) {
                        stackRow = stackOf()
                        possibleFindRow = null
                    }
                    stackRow.push(p)
                    isPushingRow = true
                }
            }
            if (!isPushingRow) {
                rowSolution += (possibleFindRow ?: searchIndexRow) * 100
            }
        }

        patternCols.forEach { patternCol ->
            var stackCol = Stack<String>()
            var searchIndexCol = -1
            var possibleFindCol: Int? = null
            var isPushingCol = true
            for (p in patternCol) {
                searchIndexCol++
                if (stackCol.peek() == p) {
                    stackCol.pop()
                    if (possibleFindCol == null) {
                        possibleFindCol = searchIndexCol
                    }
                    isPushingCol = false
                    if (stackCol.isEmpty) {
                        searchIndexCol = possibleFindCol
                        break
                    }
                } else {
                    if (!isPushingCol) {
                        stackCol = stackOf()
                        possibleFindCol = null
                    }
                    stackCol.push(p)
                    isPushingCol = true
                }
            }
            if (!isPushingCol) {
                colSolution += possibleFindCol ?: searchIndexCol
            }
        }
        return rowSolution + colSolution
    }

    fun solveSecond(): Int {
        val (patternRows, patternCols) = createInput()
        var rowSolution = 0
        var colSolution = 0

        patternRows.forEach { rowPattern ->
            rowSolution += findMirror(rowPattern) * 100
        }

        patternCols.forEach { colPattern ->
            colSolution += findMirror(colPattern)
        }

        return rowSolution + colSolution
    }

    private fun findMirror(pattern: List<String>): Int {
        val maxNumber = pattern.size / 2 + 1

        // from top to bottom
        for (i in 1..maxNumber) {
            val arr1 = pattern.take(i)
            val endIndex = if (2 * i >= pattern.size - 1) {
                pattern.size - 1
            } else {
                2 * i
            }
            val arr2 = pattern.subList(i, endIndex).reversed()
            if (diffBy(arr1, arr2) == 1) {
                return i
            }
        }

        for (i in 1..maxNumber) {
            val arr1 = pattern.reversed().take(i)
            val endIndex = if (2 * i >= pattern.size - 1) {
                pattern.size - 1
            } else {
                2 * i
            }
            val arr2 = pattern.reversed().subList(i, endIndex).reversed()
            if (diffBy(arr1, arr2) == 1) {
                return (pattern.size - i)
            }
        }
        return 0
    }

    private fun diffBy(arr1: List<String>, arr2: List<String>): Int {
        var res = 0
        for (i in 0..arr2.lastIndex) {
            res += unCommonChars(arr1[i], arr2[i])
        }
        return res
    }

    private fun unCommonChars(s1: String, s2: String): Int {
        var count = 0
        for (i in s1.indices) {
            count += if (s1[i] == s2[i]) 1 else 0
        }
        return s1.length - count
    }
}

private fun createInput(): Pair<List<List<String>>, List<List<String>>> {
    val patternRows = mutableListOf<List<String>>()
    val patternCols = mutableListOf<List<String>>()
    val pattern = mutableListOf<String>()
    execFileByLineIndexed(13) { it, index ->
        if (it == "") {
            patternRows.add(pattern.toList())
            pattern.clear()
        } else {
            pattern.add(it)
        }
        if (index >= (numberOfLinesPerFile(13) - 1)) {
            patternRows.add(pattern.toList())
            pattern.clear()
        }
    }
    for (p in patternRows) {
        val tempList = mutableListOf<String>()
        for (letterIndex in 0..p[0].lastIndex) {
            val temp = mutableListOf<Char>()
            for (word in p) {
                temp.add(word[letterIndex])
            }
            tempList.add(String(temp.toCharArray()))
        }
        patternCols.add(tempList)
    }
    return Pair(patternRows, patternCols)
}
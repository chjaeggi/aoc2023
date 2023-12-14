import utils.Stack
import utils.execFileByLineIndexed
import utils.numberOfLinesPerFile
import utils.stackOf

class Day13 {

    fun solveFirst() {
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
        println(rowSolution + colSolution)
    }

    fun solveSecond() {
        // TODO
    }
}

private fun createInput(): Pair<MutableList<List<String>>, MutableList<List<String>>> {
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
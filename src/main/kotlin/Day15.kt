import utils.execFileByLine

private enum class Operation(value: String) {
    ADD("="), REMOVE("-")
}

private data class Lens(
    val label: String,
    val focalLength: Int,
)

private data class LensOperation(
    val lens: Lens,
    val operation: Operation,
    val boxNo: Int
)

class Day15 {

    private val boxes = mutableMapOf<Int,ArrayDeque<Lens>>()

    fun solveFirst(): Int {
        var res = -1
        execFileByLine(15) {
            res = it.split(",").sumOf { it.elfHash() }
        }
        return res
    }

    fun solveSecond(): Int {
        repeat(256) {
            boxes[it] = ArrayDeque()
        }
        val lensOperations = mutableListOf<LensOperation>()
        execFileByLine(15) {
            val res = it.split(",")
            res.forEach {
                val lensLabel = it.substringBefore("=").substringBefore("-")
                val focalLength = if (it.contains("=")) it.substringAfter("=").toInt() else -1
                val operation = if (it.contains("-")) Operation.REMOVE else Operation.ADD
                val boxNo = lensLabel.elfHash()
                lensOperations.add(LensOperation(Lens(lensLabel, focalLength), operation, boxNo))
            }
            lensOperations.forEach {
                if (it.operation == Operation.ADD) {
                    addLens(it.boxNo, it.lens)
                } else {
                    removeLens(it.boxNo, it.lens)
                }
            }
        }
        return boxes.map {
            if (it.value.isNotEmpty()) {
                focusingPowerPerBox(it.key)
            } else {
                0
            }
        }.sum()
    }

    private fun String.elfHash(): Int {
        var current = 0
        forEach {
            current += it.code
            current *= 17
            current %= 256
        }
        return current
    }

    private fun removeLens(boxNo: Int, lens: Lens) {
        for ((index, element) in boxes[boxNo]!!.withIndex()) {
            if (element.label == lens.label) {
                boxes[boxNo]!!.removeAt(index)
                break
            }
        }
    }

    private fun addLens(boxNo: Int, lens: Lens) {
        for ((index, element) in boxes[boxNo]!!.withIndex()) {
            if (element.label == lens.label) {
                boxes[boxNo]!![index] = lens
                return
            }
        }
        boxes[boxNo]!!.addLast(lens)
    }

    private fun focusingPowerPerBox(boxNo: Int): Int {
        var power = 0
        for ((index, element) in boxes[boxNo]!!.withIndex()) {
            power += (boxNo + 1) * (index + 1) * element.focalLength
        }
        return power
    }

}
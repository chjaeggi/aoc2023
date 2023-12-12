import utils.execFileByLine

class Day12 {
    fun solveFirst() {
        var sum = 0
        execFileByLine(12) {
            val record = it.substringBefore(" ")
            val defectives = it.substringAfter(" ").split(",").map { it.toInt() }
            val nOfQ = record.count { it == '?' }
            val indicesOfQ = record.withIndex().filter { (_, char) -> char == '?' }.map { it.index }
            val possibleSequences = mutableListOf<String>()

            for (i in 0..<(1 shl nOfQ)) {
                val permutation = i.toString(radix = 2)
                    .padStart(nOfQ, '0')
                    .replace('0', '.')
                    .replace('1', '#')
                    .toMutableList()
                val rec = record.toCharArray()
                indicesOfQ.forEach {
                    rec[it] = permutation[0]
                    permutation.removeAt(0)
                }
                possibleSequences.add(String(rec))
            }
            sum += possibleSequences.count {getGroupSizes(it) == defectives }
        }
        println(sum)
    }

    private fun getGroupSizes(inputString: String): List<Int> {
        val regex = Regex("(#+)|\\.")
        val matches = regex.findAll(inputString)
        val groupSizes = mutableListOf<Int>()
        for (match in matches) {
            val value = match.value
            if (value.contains("#")) {
                groupSizes.add(value.length)
            }
        }
        return groupSizes
    }

}
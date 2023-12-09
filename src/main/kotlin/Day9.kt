import utils.execFileByLine

class Day9 {

    fun solveFirst() = solve(true)
    fun solveSecond() = solve(false)

    private fun solve(isFirstPartOfQuiz: Boolean = true): Int {
        var sum = 0
        execFileByLine("./inputs/input9.txt") { line ->
            var done = false

            val diffSequences = mutableListOf<List<Int>>()
            var nextSequence = line.split(" ").map { it.toInt() }
            diffSequences.add(nextSequence)

            while (!done) {
                nextSequence = nextSequence.zipWithNext { a, b -> b - a }
                diffSequences.add(nextSequence)
                if (nextSequence.all { it == 0 }) {
                    diffSequences.forEachIndexed { idx, value ->
                        sum += if (isFirstPartOfQuiz) {
                            value.last()
                        } else {
                            if (idx % 2 == 0) value.first() else value.first() * -1
                        }
                    }
                    done = true
                }
            }
        }
        return sum
    }
}
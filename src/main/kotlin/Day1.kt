fun day1() {
    var sum = 0
    val numWords = "one|two|three|four|five|six|seven|eight|nine|[0-9]".toRegex()
    val numWordsRev = "eno|owt|eerht|ruof|evif|xis|neves|thgie|enin|[0-9]".toRegex()
    val wordsToDigit = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )
    val wordsToDigitRev = mapOf(
        "eno" to 1,
        "owt" to 2,
        "eerht" to 3,
        "ruof" to 4,
        "evif" to 5,
        "xis" to 6,
        "neves" to 7,
        "thgie" to 8,
        "enin" to 9,
    )
    execFileByLine("inputs/input1.txt") {
        val first = numWords.findAll(it).first().value
        val last = numWordsRev.findAll(it.reversed()).first().value

        val ten = if (first.length > 1) {
            wordsToDigit[first]!! * 10
        } else {
            first.toCharArray().first().digitToInt() * 10
        }
        val one = if (last.length > 1) {
            wordsToDigitRev[last]!!
        } else {
            last.toCharArray().first().digitToInt()
        }
        sum += (ten + one)
    }
    println(sum)
}
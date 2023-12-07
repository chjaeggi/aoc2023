class Day4() {
    fun solveSecond() {
        val copies = mutableMapOf<Int, Int>()
        for (i in 1..202) {
            copies[i] = 1
        }
        execFileByLine("./src/main/kotlin/input4.txt") {
            val arr = it.split(":")
            val cardNo = arr.first().split("Card").last()
                .filterNot { it.isWhitespace() }.toInt()
            val winningNumbersString = arr.last().split("|").first()
            val myNumbersString = arr.last().split("|").last()
            val winningNumbers =
                winningNumbersString.split("\\s+".toRegex()).filterNot { it.isEmpty() }
                    .map { it.toInt() }
            val myNumbers = myNumbersString.split("\\s+".toRegex()).filterNot { it.isEmpty() }
                .map { it.toInt() }

            var accordance = 0
            for (number in myNumbers) {
                if (number in winningNumbers) {
                    accordance++
                }
            }
            repeat(copies[cardNo]!!) {
                for (i in (cardNo + 1)..<(cardNo + 1 + accordance)) {
                    copies[i] = copies[i]!! + 1
                }
            }
        }
        println(copies.values.sum())
    }

    fun solveFirst() {
        var sum = 0
        execFileByLine("./src/main/kotlin/input4.txt") {
            val arr = it.split(":")
            val winningNumbersString = arr.last().split("|").first()
            val myNumbersString = arr.last().split("|").last()
            val winningNumbers =
                winningNumbersString.split("\\s+".toRegex()).filterNot { it.isEmpty() }
                    .map { it.toInt() }
            val myNumbers = myNumbersString.split("\\s+".toRegex()).filterNot { it.isEmpty() }
                .map { it.toInt() }

            var accordance = 0
            for (number in myNumbers) {
                if (number in winningNumbers) {
                    accordance++
                }
            }
            sum += Math.pow(2.0, accordance - 1.0).toInt()
        }
        println(sum)
    }
}
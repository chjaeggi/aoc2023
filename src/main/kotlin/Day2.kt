fun day2() {
    var sum = 0

    execFileByLine("inputs/input2.txt") {
        var minRed = -1
        var minGreen = -1
        var minBlue = -1

        val arr = it.split(":")
        val gameNumber = arr.first().split("Game ").last().toInt()
        val combos = arr.last().split(";")
        for (combo in combos) {
            val redIndex = combo.indexOf("red") - 3
            val greenIndex = combo.indexOf("green") - 3
            val blueIndex = combo.indexOf("blue") - 3

            if (redIndex >= 0) {
                val red = combo.substring(redIndex, redIndex + 2)
                    .filter { !it.isWhitespace() }.toInt()
                if (red > minRed) {
                    minRed = red
                }
            }
            if (greenIndex >= 0) {
                val green = combo.substring(greenIndex, greenIndex + 2)
                    .filter { !it.isWhitespace() }.toInt()
                if (green > minGreen) {
                    minGreen = green
                }
            }
            if (blueIndex >= 0) {
                val blue = combo.substring(blueIndex, blueIndex + 2)
                    .filter { !it.isWhitespace() }.toInt()
                if (blue > minBlue) {
                    minBlue = blue
                }
            }
        }
        sum += minRed * minGreen * minBlue
    }
    println(sum)
}
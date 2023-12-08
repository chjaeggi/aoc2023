package utils

fun List<Int>.lcm(): Long {
    var result = this[0].toLong()
    for (i in 1 until this.size) {
        result = findLCM(result, this[i].toLong())
    }
    return result
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

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

// sum of the cartesian products of the polygon
// ("ring buffered" as the last will have a cross product of the first)
fun shoelaceArea(vertices: List<Point2D>): Double {
    val last = vertices.lastIndex
    var area = 0.0
    for (i in 0..<last) {
        // sum up all the cross products until the last
        area += vertices[i].x * vertices[i + 1].y - vertices[i + 1].x * vertices[i].y
    }
    // and now to the "overflow" back to the first and add the accumulated area
    return Math.abs(area + vertices[last].x * vertices[0].y - vertices[0].x * vertices[last].y) / 2.0
}
package utils

fun Array<CharArray>.transpose(): Array<CharArray> {
    val width = this.size
    val height = this[0].size
    val arrayNew = Array(height) { CharArray(width) }
    for (x in 0 until width) {
        for (y in 0 until height) {
            arrayNew[y][x] = this[x][y]
        }
    }
    return arrayNew
}
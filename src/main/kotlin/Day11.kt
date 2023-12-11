import utils.execFileByLineIndexed
import utils.numberOfCharsPerLine
import utils.numberOfLinesPerFile
import utils.transpose
import kotlin.math.abs


data class GalaxyPoint(
    val x: Int,
    val y: Int
)

data class GalaxyMap(
    val map: Map<GalaxyPoint, List<GalaxyPoint>>,
    val galaxyFreeRows: List<Int>,
    val galaxyFreeCols: List<Int>,
)

class Day11 {

    fun solve() {
        val galaxies = createGalaxyMap()
        println(solve(galaxies))
        println(solve(galaxies, 1000000))
    }

    private fun createGalaxyMap(): GalaxyMap {
        val fileName = "./inputs/input11.txt"
        val originalGalaxies =
            Array(numberOfLinesPerFile(fileName)) { CharArray(numberOfCharsPerLine(fileName)) }
        val emptyRows = mutableListOf<Int>()
        val emptyCols = mutableListOf<Int>()

        execFileByLineIndexed(fileName) { line, index ->
            line.forEachIndexed { i, v ->
                originalGalaxies[index][i] = v
            }
            if (line.all { it == '.' }) {
                emptyRows.add(index)
            }
        }
        val galaxiesTransposed = originalGalaxies.transpose()
        galaxiesTransposed.forEachIndexed { index, chars ->
            if (chars.all { it == '.' }) {
                emptyCols.add(index)
            }
        }

        val galaxyCoords = originalGalaxies.flatMapIndexed { y, chars ->
            chars.mapIndexed { x, c ->
                if (c == '#') {
                    GalaxyPoint(y, x)
                } else {
                    null
                }
            }
        }.filterNotNull()

        val galaxies = mutableMapOf<GalaxyPoint, List<GalaxyPoint>>()
        galaxyCoords.mapIndexed { index, point ->
            if (index < galaxyCoords.lastIndex) {
                galaxies[point] = galaxyCoords.subList(index + 1, galaxyCoords.lastIndex + 1)
            }
        }

        return GalaxyMap(galaxies, emptyRows, emptyCols)
    }

    private fun solve(galaxies: GalaxyMap, voidMultiplier: Long = 2): Long {
        return galaxies.map.flatMap { galaxy ->
            val source = galaxy.key
            galaxy.value.map { dest ->
                val maxY = maxOf(dest.y, source.y)
                val minY = minOf(dest.y, source.y)
                val maxX = maxOf(dest.x, source.x)
                val minX = minOf(dest.x, source.x)
                val sumY =
                    (galaxies.galaxyFreeCols.count { it in minY..<maxY } * (voidMultiplier - 1))
                val sumX =
                    (galaxies.galaxyFreeRows.count { it in minX..<maxX } * (voidMultiplier - 1))
                abs(dest.y - source.y) + sumY + abs(dest.x - source.x) + sumX
            }
        }.sum()
    }
}
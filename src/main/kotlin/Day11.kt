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

        // TODO replace forEachIndexed calls with mapIndexed/flatMapIndexed Coni heeeelp and save
        // their results directly into the according variables (make them immutable):
        // - galaxies
        // - emptyRows
        // - emptyCols

        val fileName = "./inputs/input11.txt"
        val originalGalaxies =
            Array(numberOfLinesPerFile(fileName)) { CharArray(numberOfCharsPerLine(fileName)) }

        val galaxies = mutableMapOf<GalaxyPoint, List<GalaxyPoint>>()
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

        originalGalaxies.transpose().forEachIndexed { index, chars ->
            if (chars.all { it == '.' }) {
                emptyCols.add(index)
            }
        }

        val galaxyCoords = mutableListOf<GalaxyPoint>()
        originalGalaxies.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxyCoords.add(GalaxyPoint(y, x))
                }
            }
        }

        galaxyCoords.forEachIndexed { index, point ->
            if (index < galaxyCoords.lastIndex) {
                galaxies[point] = galaxyCoords.subList(index + 1, galaxyCoords.lastIndex + 1)
            }
        }

        return GalaxyMap(galaxies, emptyRows, emptyCols)
    }

    private fun solve(galaxies: GalaxyMap, voidMultiplier: Long = 2): Long {
        val ySkip = galaxies.galaxyFreeCols
        val xSkip = galaxies.galaxyFreeRows

        return galaxies.map.flatMap { galaxy ->
            val source = galaxy.key
            galaxy.value.map { dest ->
                val maxY = maxOf(dest.y, source.y)
                val minY = minOf(dest.y, source.y)
                val maxX = maxOf(dest.x, source.x)
                val minX = minOf(dest.x, source.x)
                val sumY = ySkip.count { it in minY..<maxY } * (voidMultiplier - 1)
                val sumX = xSkip.count { it in minX..<maxX } * (voidMultiplier - 1)
                abs(dest.y - source.y) + sumY + abs(dest.x - source.x) + sumX
            }
        }.sum()
    }
}
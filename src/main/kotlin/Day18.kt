import utils.Point2D
import utils.execFileByLine
import utils.shoelaceArea

private data class MoveInstruction(
    val orientation: Char,
    val steps: Long,
)

class Day18 {

    fun solveFirst(): Long {
        val trenchEdges = mutableListOf<Point2D>()
        var currentPosition = Point2D(0, 0)
        var trenchLength = 0L

        execFileByLine(18) { line ->
            val orientation = line.substringBefore(" ").toCharArray().first()
            val steps = line.substringAfter(" ").substringBefore(" ").toLong()
            val instruction = MoveInstruction(orientation, steps)
            val (start, end) = instruction.to2dPoints(currentPosition)
            trenchEdges.add(start)
            trenchEdges.add(end)
            currentPosition = end
            trenchLength += steps
        }

        // to account for one of the outer sides (e.g. top and right side) of the loop
        // we divide by two and increase by one (corner)
        return trenchLength / 2 + 1 + shoelaceArea(trenchEdges).toLong()
    }

    fun solveSecond(): Long {
        val trenchEdges = mutableListOf<Point2D>()
        var currentPosition = Point2D(0, 0)
        var trenchLength = 0L

        execFileByLine(18) { line ->
            val hexString = line.split(" ")
                .last()
                .removePrefix("(")
                .removeSuffix(")")
                .removePrefix("#")

            val steps = hexString.take(5).toLong(16)
            val orientationNumber = hexString.takeLast(1).toInt()
            val orientation = when (orientationNumber) {
                0 -> 'R'
                1 -> 'D'
                2 -> 'L'
                3 -> 'U'
                else -> {
                    throw java.lang.IllegalArgumentException("invalid direction")
                }
            }
            val instruction = MoveInstruction(orientation, steps)
            val (start, end) = instruction.to2dPoints(currentPosition)
            trenchEdges.add(start)
            trenchEdges.add(end)
            currentPosition = end
            trenchLength += steps
        }

        // same again, see solution part 1
        return trenchLength / 2 + 1 + shoelaceArea(trenchEdges).toLong()
    }

    private fun MoveInstruction.to2dPoints(start: Point2D): Pair<Point2D, Point2D> {
        return when (this.orientation) {
            'D' -> Point2D(start.x, start.y - 1) to Point2D(start.x, start.y - steps)
            'R' -> Point2D(start.x + 1, start.y) to Point2D(start.x + steps, start.y)
            'L' -> Point2D(start.x - 1, start.y) to Point2D(start.x - steps, start.y)
            'U' -> Point2D(start.x, start.y + 1) to Point2D(start.x, start.y + steps)
            else -> throw IllegalArgumentException("invalid direction")

        }
    }
}
private data class Race(
    val allowedTime: Long,
    val record: Long
)

class Day6 {

    fun solveFirst(): Int {
        val testInput = listOf(
            Race(7, 9),
            Race(15, 40),
            Race(30, 200),
        )
        val realInput = listOf(
            Race(51, 377),
            Race(69, 1171),
            Race(98, 1224),
            Race(78, 1505),
        )
        return testInput.numberOfWaysToBeatRecord()
    }

    fun solveSecond(): Int {
        val testInput = listOf(
            Race(71530, 940200),
        )
        val realInput = listOf(
            Race(51699878, 377117112241505),
        )
        return testInput.numberOfWaysToBeatRecord()
    }

    private fun List<Race>.numberOfWaysToBeatRecord() =
        map { race ->
            (1..race.allowedTime).count { chargeTime ->
                // b/c chargeTime == velocity
                (race.allowedTime - chargeTime) * chargeTime > race.record
            }
        }.reduce { acc, i ->
            acc * i
        }
}
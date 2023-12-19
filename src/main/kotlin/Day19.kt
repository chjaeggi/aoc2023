import Status.REJECTED

private enum class Status {
    REJECTED, ACCEPTED
}
private data class Workflow(
    val name: String,
    val conditions: List<Condition>
)

private data class Part(
    val xmasMap : Map<Char, Int> =  mutableMapOf(),
    val status: Status = REJECTED
)

private data class Condition(
    val part: Part,
    val operator: String,
    val boundary: Int,
    val nextWorkFlow: String
)
class Day19 {

    fun solveFirst() {

    }

}
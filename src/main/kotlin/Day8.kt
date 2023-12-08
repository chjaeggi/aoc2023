import kotlinx.coroutines.*
import utils.execFileByLineIndexed
import utils.lcm

class Day8 {

    private val instructions = mutableListOf<Char>()
    private val nodes = mutableMapOf<String, Pair<String, String>>()

    fun solveFirst(): Int {
        parseInput(instructions, nodes)
        return findNumberOfSteps()
    }

    fun solveSecond(): Long {
        parseInput(instructions, nodes)
        return findNumberOfStepsParallel()
    }

    private fun parseInput(
        instructions: MutableList<Char>,
        nodes: MutableMap<String, Pair<String, String>>
    ) {
        execFileByLineIndexed("inputs/input8.txt") { value, index ->
            if (index == 0) {
                instructions.addAll(value.toCharArray().toMutableList())
            } else {
                if (value.isNotEmpty()) {
                    val split = value.split(" = ")
                    val k = split.first()
                    val v = split.last()
                        .removeRange(9..9)
                        .removeRange(0..0)
                        .split(", ")
                    val left = v.first()
                    val right = v.last()

                    nodes[k] = Pair(left, right)
                }
            }
        }
    }

    private fun findNumberOfSteps(): Int {
        var current = "AAA"
        var instructionCounter = -1
        var steps = 0

        while (current != "ZZZ") {
            steps++
            instructionCounter++
            if (instructionCounter > instructions.lastIndex) {
                instructionCounter = 0
            }
            current = if (instructions[instructionCounter] == 'L') {
                nodes[current]!!.first
            } else {
                nodes[current]!!.second
            }
        }
        return steps
    }

    private fun findNumberOfStepsParallel(): Long {
        val current = nodes.filter { it.key.endsWith("A") }.toMutableMap()
        var hasFoundAll = false
        var instructionCounter = -1
        var steps = 0
        val stepsPerNode = mutableListOf<Int>()

        while (!hasFoundAll) {
            steps++
            instructionCounter++
            if (instructionCounter > instructions.lastIndex) {
                instructionCounter = 0
            }
            val nextNodes = current.waitForAll(instructions[instructionCounter])
            if (nextNodes.removeFinishedNodes()) {
                stepsPerNode.add(steps)
            }
            if (nextNodes.isEmpty()) {
                hasFoundAll = true
                continue
            }
            current.replaceWithNext(nextNodes)
        }
        return stepsPerNode.lcm()
    }

    // manipulate next nodes to consider from base input
    private fun MutableMap<String, Pair<String, String>>.replaceWithNext(
        nextNodes: MutableList<String>
    ) {
        this.clear()
        nextNodes.forEach {
            this[it] = nodes[it]!!
        }
    }

    // manipulate list and return whether elements have been removed
    private fun MutableList<String>.removeFinishedNodes(): Boolean {
        val toBeRemoved = mutableListOf<String>()
        for (res in this) {
            if (res.endsWith("Z")) {
                toBeRemoved.add(res)
            }
        }
        this.removeAll(toBeRemoved)
        return toBeRemoved.isNotEmpty()
    }

    // check for each node to be travelled in parallel and return only when all have been processed
    private fun MutableMap<String, Pair<String, String>>.waitForAll(instruction: Char) =
        map { entry ->
            if (instruction == 'L') {
                entry.value.first
            } else {
                entry.value.second
            }
        }.toMutableList()
}
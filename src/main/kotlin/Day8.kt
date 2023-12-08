import kotlinx.coroutines.*
import utils.execFileByLineIndexed
import utils.lcm

class Day8 {

    private val instructions = mutableListOf<Char>()
    private val nodes = mutableMapOf<String, Pair<String, String>>()

    fun solveFirst() : Int {
        parseInput(instructions, nodes)
        return findNumberOfSteps(nodes)
    }

    suspend fun solveSecond(): Long {
        parseInput(instructions, nodes)
        return findNumberOfStepsParallel(nodes)
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

    private fun findNumberOfSteps(
        nodes: Map<String, Pair<String, String>>
    ): Int {
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

    private suspend fun findNumberOfStepsParallel(
        nodes: Map<String, Pair<String, String>>
    ): Long {
        val current = nodes.filter { it.key.endsWith("A") }.toMutableMap()
        var hasFoundAll = false
        var instructionCounter = -1
        var steps = 0
        val finalStepsPerNode = mutableListOf<Int>()

        while (!hasFoundAll) {
            steps++
            instructionCounter++
            if (instructionCounter > instructions.lastIndex) {
                instructionCounter = 0
            }
            val resultsPerStep = getNextNodeForEachCurrent(current, instructionCounter)
            val finishedNodes = getFinishedNodes(resultsPerStep, finalStepsPerNode, steps)
            resultsPerStep.removeAll(finishedNodes)

            if (resultsPerStep.isEmpty()) {
                hasFoundAll = true
                continue
            }
            setNextNode(current, resultsPerStep, nodes)
        }
        return finalStepsPerNode.lcm()
    }

    private fun setNextNode(
        current: MutableMap<String, Pair<String, String>>,
        resultsPerStep: MutableList<String>,
        nodes: Map<String, Pair<String, String>>
    ) {
        current.clear()
        resultsPerStep.forEach {
            current[it] = nodes[it]!!
        }
    }

    private fun getFinishedNodes(
        resultsPerStep: MutableList<String>,
        finalStepsPerNode: MutableList<Int>,
        steps: Int
    ): MutableList<String> {
        val toBeRemoved = mutableListOf<String>()
        for (res in resultsPerStep) {
            if (res.endsWith("Z")) {
                finalStepsPerNode.add(steps)
                toBeRemoved.add(res)
            }
        }
        return toBeRemoved
    }

    private suspend fun getNextNodeForEachCurrent(
        current: MutableMap<String, Pair<String, String>>,
        instructionCounter: Int
    ) = coroutineScope {
        current.map { entry ->
            async {
                if (instructions[instructionCounter] == 'L') {
                    entry.value.first
                } else {
                    entry.value.second
                }
            }
        }.awaitAll()
    }.toMutableList()
}
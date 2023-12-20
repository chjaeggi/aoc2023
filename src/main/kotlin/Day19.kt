import Operator.GREATER_THAN
import Operator.SMALLER_THAN
import Status.*
import utils.execFileByLine

private enum class Status {
    REJECTED, ACCEPTED, UNEVALUATED
}

private fun String.toStatus(): Status = when (this) {
    "A" -> ACCEPTED
    "R" -> REJECTED
    else -> UNEVALUATED
}

private fun Status.toStatusString(): String = when (this) {
    ACCEPTED, REJECTED -> this.name.first().toString()
    else -> this.toString()
}


enum class Operator {
    GREATER_THAN, SMALLER_THAN
}

private fun Char.toOperator(): Operator = if (this == '<') SMALLER_THAN else GREATER_THAN


private data class Workflow(
    val name: String,
    val rules: List<Rule>,
)

private data class Part(
    val xmas: Map<Char, Int>,
    var status: Status = UNEVALUATED
)

private data class Rule(
    val xmasField: Char?,
    val operator: Operator?,
    val limit: Int?,
    val nextWorkflowName: String
)

class Day19 {

    var solution = 0L

    fun solveFirst(): Int {
        val (workflows, parts) = parseInput()
        val startWorkflow = workflows["in"]

        parts.forEach {
            it.runWorkflow(startWorkflow!!, workflows)
        }

        return parts.filter { it.status == ACCEPTED }.sumOf {
            it.xmas.values.sum()
        }
    }

    fun solveSecond(): Long {
        val (workflows) = parseInput()
        val xmasRanges = mapOf(
            'x' to 1..4001,
            'm' to 1..4001,
            'a' to 1..4001,
            's' to 1..4001,
        )

        runRangeForWorkflow(xmasRanges, workflows["in"]!!, workflows)
        println(solution)
        return 0L
    }

    private fun runRangeForWorkflow(
        xmasRanges: Map<Char, IntRange>,
        workflow: Workflow,
        allWorkflows: Map<String, Workflow>,
        ) {

        val rangesQueue = ArrayDeque<Pair<String,Map<Char, IntRange>>>()
        val inverseMap: Map<Char, IntRange>?
        for ((index, rule) in workflow.rules.withIndex()) {
            rangesQueue.add(rule.nextWorkflowName to rule.adjustedRange(xmasRanges))
            inverseMap = rangesInversed()
        }
//        for (rule in rules) {
//            if (rule.nextWorkflowName == "A") {
//                println(xmasRanges)
//                var product = 1L
//                xmasRanges.values.forEach {
//                    product *= it.last.toLong() - it.first.toLong()
//                }
//                println(product)
//                solution += product
//                continue
//            }
//            if (rule.nextWorkflowName == "R") {
//                println("0")
//                continue
//            }
//            val ifRange = rule.adjustRange(xmasRanges)
//            println("running worflow: ${this.name} with $ifRange for rule $rule now")
//            allWorkflows[rule.nextWorkflowName]!!.runForRange(ifRange, allWorkflows)
//        }
    }

    private fun rangesInversed(map: Map<Char, IntRange>): Map<Char, IntRange> {
        return map
    }


    private fun Rule.adjustedRange(map: Map<Char, IntRange>): Map<Char, IntRange> {
        if (operator == null) {
            return map
        }
        val rest = "xmas".filterNot { it == xmasField }
        if (operator == SMALLER_THAN) {
            return buildMap {
                put(xmasField!!, map[xmasField]!!.first..<limit!!)
                rest.forEach {
                    put(it, map[it]!!)
                }
            }
        } else if (operator == GREATER_THAN) {
            return buildMap {
                put(xmasField!!, limit!!..<map[xmasField]!!.last)
                rest.forEach {
                    put(it, map[it]!!)
                }
            }
        }
        throw IllegalStateException("not possible")
    }

    private fun Part.runWorkflow(current: Workflow, workflows: Map<String, Workflow>) {
        for (rule in current.rules) {
            if (this.status == UNEVALUATED && evaluateRule(rule)) {
                val next = rule.nextWorkflowName
                if (next.toStatus() == REJECTED || next.toStatus() == ACCEPTED) {
                    status = next.toStatus()
                    break
                } else {
                    runWorkflow(workflows[next]!!, workflows)
                }
            }
        }
    }

    private fun Part.evaluateRule(rule: Rule): Boolean {
        if (rule.xmasField == null || rule.limit == null || rule.operator == null) {
            return true
        }
        if (rule.operator == GREATER_THAN) {
            if (xmas[rule.xmasField]!! > rule.limit) {
                return true
            }
        } else {
            if (xmas[rule.xmasField]!! < rule.limit) {
                return true
            }
        }
        return false
    }


    private fun parseInput(): Pair<Map<String, Workflow>, List<Part>> {
        var isWorkflowLine = true
        val workflows = mutableMapOf<String, Workflow>()
        val parts = mutableListOf<Part>()

        execFileByLine(19) {
            if (it.isBlank()) {
                isWorkflowLine = false
            } else {
                if (isWorkflowLine) {
                    val name = it.substringBefore("{")
                    val instructions = it.substringAfter("{")
                        .removeSuffix("}")
                        .split(",")
                    val rules = instructions.flatMap {
                        if (it.contains(":")) {
                            it.split(",").map {
                                Rule(
                                    it[0],
                                    it[1].toOperator(),
                                    it.substring(2, it.indexOfFirst { it == ':' }).toInt(),
                                    it.substringAfter(":")
                                )
                            }
                        } else {
                            listOf(Rule(null, null, null, it))
                        }
                    }
                    workflows[name] = Workflow(name, rules)
                } else {
                    val groups = it.removeSuffix("}").removePrefix("{").split(',')
                    val xValue = groups[0].substringAfter("=").toInt()
                    val mValue = groups[1].substringAfter("=").toInt()
                    val aValue = groups[2].substringAfter("=").toInt()
                    val sValue = groups[3].substringAfter("=").toInt()

                    parts.add(
                        Part(
                            xmas = mapOf(
                                'x' to xValue,
                                'm' to mValue,
                                'a' to aValue,
                                's' to sValue
                            )
                        )
                    )
                }
            }
        }
        return workflows to parts
    }
}
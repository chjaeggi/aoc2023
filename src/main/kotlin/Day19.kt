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
        val startWorkflow = workflows["in"]
        val xmasRanges = mutableMapOf(
            'x' to 1..4001,
            'm' to 1..4001,
            'a' to 1..4001,
            's' to 1..4001,
        )

        startWorkflow!!.runForRange(xmasRanges, workflows)
        println(solution)
        return 0L
    }

    private fun Workflow.runForRange(
        xmasRanges: MutableMap<Char, IntRange>,
        workflows: Map<String, Workflow>
    ) {
        for (rule in rules) {
            rule.adjustRange(xmasRanges)
            if (rule.nextWorkflowName == "A") {
                println(xmasRanges)
                var product = 1L
                xmasRanges.values.forEach {
                    product *= it.last.toLong() - it.first.toLong()
                }
                println(product)
                solution += product
                return
            }
            if (rule.nextWorkflowName == "R") {
                return
            }
            workflows[rule.nextWorkflowName]!!.runForRange(xmasRanges, workflows)
        }
    }


    private fun Rule.adjustRange(map: MutableMap<Char, IntRange>) {
        if (operator == SMALLER_THAN) {
            map[xmasField!!] =
                map[xmasField]!!.first..<limit!!
        } else if (operator == GREATER_THAN) {
            map[xmasField!!] =
                limit!!..<map[xmasField]!!.last
        }
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
import Operator.GREATER_THAN
import Status.UNEVALUATED
import utils.execFileByLine


/*
px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
 */
private enum class Status {
    REJECTED, ACCEPTED, UNEVALUATED
}

private enum class Operator {
    GREATER_THAN, SMALLER_THAN
}

private data class Workflow(
    val name: String,
    val conditions: Map<String, Condition>,
)

private data class Part(
    val xmas: Map<Char, Int>,
    var status: Status = UNEVALUATED
)

private data class Condition(
    val operator: Operator?,
    val boundary: Int?,
    val nextWorkflow: Workflow
)

class Day19 {

    fun solveFirst(): Int {

        val workflows = mutableMapOf<String, Workflow>()
        val parts = mutableListOf<Part>()

        var isWorkflowLine = true
        execFileByLine(19) {
            if (it.isBlank() || it.isEmpty()) {
                isWorkflowLine = false
            } else {
                val name = it.substringBefore("{")
                val instructions = it.substringAfter("{").removeSuffix("}").split(",")
                val conditions = instructions.map {
                    
                }
            }
        }


        val startWorkflow = workflows["in"]

        // parts.forEach { runWorkflows() }

        return parts.filter { it.status == Status.ACCEPTED }.sumOf {
            it.xmas.values.sum()
        }
    }

    private fun Part.runWorkflow(workflow: Workflow) {

    }

    private fun nextWorkflowName(workflow: Workflow): String {

//        if (workflow.conditions[workflow.currentCondition].operator == GREATER_THAN) {
//
//        } else {
//
//        }
        return ""
    }

}
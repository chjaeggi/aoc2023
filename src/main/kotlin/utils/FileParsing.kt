package utils

import java.io.File

fun execFileByLine(fileName: String, f: (str: String) -> Unit) =
    File(fileName).forEachLine { f(it) }

fun execFileByLineIndexed(fileName: String, f: (str: String, index: Int) -> Unit) {
    val arr = mutableListOf<String>()
    File(fileName).forEachLine{
        arr.add(it)
    }
    arr.forEachIndexed { index, value ->
        f(value, index)
    }
}
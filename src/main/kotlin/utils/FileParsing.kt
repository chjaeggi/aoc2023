package utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader


fun execFileByLine(fileName: String, f: (str: String) -> Unit) =
    File(fileName).forEachLine { f(it) }

fun execFileByLineIndexed(fileName: String, f: (str: String, index: Int) -> Unit) {
    val arr = mutableListOf<String>()
    File(fileName).forEachLine {
        arr.add(it)
    }
    arr.forEachIndexed { index, value ->
        f(value, index)
    }
}

fun numberOfLinesPerFile(fileName: String): Int {
    val reader = BufferedReader(FileReader(fileName))
    var lines = 0
    while (reader.readLine() != null) lines++
    reader.close()
    return lines
}

fun numberOfCharsPerLine(fileName: String): Int {
    val reader = BufferedReader(FileReader(fileName))
    val line = reader.readLine()
    reader.close()
    return line.length
}
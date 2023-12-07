import java.io.File

fun execFileByLine(fileName: String, f: (str: String) -> Unit) =
    File(fileName).forEachLine { f(it) }
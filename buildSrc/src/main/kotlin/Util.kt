import org.gradle.api.Project
import java.io.File
import java.util.LinkedList
import kotlin.streams.asSequence

data class CmdResult(val exitCode: Int, val output: String?)

fun Project.cmd(vararg args: String, dir: File = rootProject.projectDir, printOut: Boolean = false): CmdResult {
    val process = ProcessBuilder()
        .command(*args)
        .redirectErrorStream(true)
        .directory(dir)
        .start()
    val output = process.inputStream.bufferedReader().use { reader ->
        reader.lines().asSequence()
            .onEach {
                if (printOut) {
                    logger.lifecycle(it)
                } else {
                    logger.debug(it)
                }
            }
            .toCollection(LinkedList())
            .joinToString(separator = "\n")
    }
    val exit = process.waitFor()
    return CmdResult(exit, output)
}

fun ensureSuccess(cmd: CmdResult): String? {
    val (exit, output) = cmd
    if (exit != 0) {
        error("Failed to run command, exit code is $exit")
    }
    return output
}

fun String.applyReplacements(replacements: Map<String, String>): String {
    var result = this
    for ((key, value) in replacements) {
        result = result.replace("\${$key}", value)
    }
    return result
}

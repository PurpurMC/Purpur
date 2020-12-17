import org.gradle.api.Project
import java.io.File
import java.util.LinkedList
import kotlin.streams.asSequence

data class CmdResult(val exitCode: Int, val output: String?)

fun Project.cmd(
    vararg args: String,
    dir: File = rootProject.projectDir,
    printOut: Boolean = false
): CmdResult {
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

fun ensureSuccess(
    cmd: CmdResult,
    errorHandler: CmdResult.() -> Unit = {}
): String? {
    val (exit, output) = cmd
    if (exit != 0) {
        errorHandler(cmd)
        error("Failed to run command, exit code is $exit")
    }
    return output
}

fun Project.gitCmd(
    vararg args: String,
    dir: File = rootProject.projectDir,
    printOut: Boolean = false
): CmdResult =
    cmd("git", *args, dir = dir, printOut = printOut)

fun Project.bashCmd(
    vararg args: String,
    dir: File = rootProject.projectDir,
    printOut: Boolean = false
): CmdResult =
    cmd("bash", "-c", *args, dir = dir, printOut = printOut)

internal fun String.applyReplacements(replacements: Map<String, String>): String {
    var result = this
    for ((key, value) in replacements) {
        result = result.replace("\${$key}", value)
    }
    return result
}

private fun Project.gitSigningEnabled(repo: File): Boolean =
    gitCmd("config", "commit.gpgsign", dir = repo).output?.toBoolean() == true

internal fun Project.temporarilyDisableGitSigning(repo: File): Boolean {
    val isCurrentlyEnabled = gitSigningEnabled(repo)
    if (isCurrentlyEnabled) {
        gitCmd("config", "commit.gpgsign", "false", dir = repo)
    }
    return isCurrentlyEnabled
}

internal fun Project.reEnableGitSigning(repo: File) {
    gitCmd("config", "commit.gpgsign", "true", dir = repo)
}

fun Project.gitHash(repo: File): String =
    gitCmd("rev-parse", "HEAD", dir = repo).output ?: ""

val jenkins = System.getenv("JOB_NAME") != null

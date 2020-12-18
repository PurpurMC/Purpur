package task

import ensureSuccess
import gitCmd
import org.gradle.api.Project
import org.gradle.api.Task
import taskGroup
import toothpick
import upstreamDir

internal fun Project.createUpstreamCommitTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("upstreamCommit") {
    receiver(this)
    group = taskGroup
    doLast {
        val oldRev = ensureSuccess(gitCmd("ls-tree", "HEAD", toothpick.upstream))
            ?.substringAfter("commit ")?.substringBefore("\t")
        val gitChangelog =
            ensureSuccess(gitCmd("log", "--oneline", "$oldRev...HEAD", printOut = true, dir = upstreamDir)) {
                logger.lifecycle("No upstream changes to commit?")
            }
        val commitMessage = """
                    |Updated Upstream (${toothpick.upstream})
                    |
                    |Upstream has released updates that appear to apply and compile correctly
                    |
                    |${toothpick.upstream} Changes:
                    |$gitChangelog
                """.trimMargin()
        ensureSuccess(gitCmd("commit", "-m", commitMessage, printOut = true))
    }
}

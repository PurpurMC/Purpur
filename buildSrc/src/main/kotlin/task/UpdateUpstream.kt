package task

import ensureSuccess
import gitCmd
import org.gradle.api.Project
import org.gradle.api.Task
import rootProjectDir
import taskGroup
import toothpick
import upstreamDir

internal fun Project.createUpdateUpstreamTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("updateUpstream") {
    receiver(this)
    group = taskGroup
    doLast {
        ensureSuccess(gitCmd("fetch", dir = upstreamDir, printOut = true))
        ensureSuccess(gitCmd("reset", "--hard", toothpick.upstreamBranch, dir = upstreamDir, printOut = true))
        ensureSuccess(gitCmd("add", toothpick.upstream, dir = rootProjectDir, printOut = true))
        ensureSuccess(gitCmd("submodule", "update", "--init", "--recursive", dir = upstreamDir, printOut = true))
    }
}

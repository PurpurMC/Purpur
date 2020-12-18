package task

import gitCmd
import org.gradle.api.Project
import org.gradle.api.Task
import taskGroup
import upstreamDir

internal fun Project.createInitGitSubmodulesTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("initGitSubmodules") {
    receiver(this)
    group = taskGroup
    onlyIf { !upstreamDir.resolve(".git").exists() }
    doLast {
        val exit = gitCmd("submodule", "update", "--init", "--recursive", printOut = true).exitCode
        if (exit != 0) {
            error("Failed to checkout git submodules: git exited with code $exit")
        }
    }
}

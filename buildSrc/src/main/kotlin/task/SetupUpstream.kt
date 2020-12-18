package task

import bashCmd
import gitHash
import lastUpstream
import org.gradle.api.Project
import org.gradle.api.Task
import taskGroup
import toothpick
import upstreamDir

internal fun Project.createSetupUpstreamTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("setupUpstream") {
    receiver(this)
    group = taskGroup
    doLast {
        // this whole thing assumes we're forking either paper or a byof project atm
        val result = bashCmd(
            "./${toothpick.upstreamLowercase} patch",
            dir = upstreamDir,
            printOut = true
        )
        if (result.exitCode != 0) {
            error("Failed to apply upstream patches: script exited with code ${result.exitCode}")
        }
        lastUpstream.writeText(gitHash(upstreamDir))
    }
}

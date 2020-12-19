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
        val setupUpstreamCommand = if (upstreamDir.resolve(toothpick.upstreamLowercase).exists()) {
            "./${toothpick.upstreamLowercase} patch"
        } else if (
            upstreamDir.resolve("build.gradle.kts").exists()
            && upstreamDir.resolve("subprojects/server.gradle.kts").exists()
            && upstreamDir.resolve("subprojects/api.gradle.kts").exists()
        ) {
            "./gradlew applyPatches"
        } else {
            error("Don't know how to setup upstream!")
        }
        val result = bashCmd(setupUpstreamCommand, dir = upstreamDir, printOut = true)
        if (result.exitCode != 0) {
            error("Failed to apply upstream patches: script exited with code ${result.exitCode}")
        }
        lastUpstream.writeText(gitHash(upstreamDir))
    }
}

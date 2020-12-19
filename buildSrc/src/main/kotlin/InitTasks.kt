import org.gradle.api.Project
import task.createApplyPatchesTask
import task.createImportMCDevTask
import task.createInitGitSubmodulesTask
import task.createPaperclipTask
import task.createRebuildPatchesTask
import task.createSetupUpstreamTask
import task.createUpdateUpstreamTask
import task.createUpstreamCommitTask

@Suppress("UNUSED_VARIABLE")
internal fun Project.initToothpickTasks() {
    if (project.hasProperty("fast")) {
        gradle.taskGraph.whenReady {
            gradle.taskGraph.allTasks.filter {
                it.name.contains("test", ignoreCase = true) || it.name.contains("javadoc", ignoreCase = true)
            }.forEach {
                it.onlyIf { false }
            }
        }
    }

    tasks.getByName("build") {
        doFirst {
            val readyToBuild =
                upstreamDir.resolve(".git").exists()
                        && toothpick.subprojects.values.all { it.projectDir.exists() && it.baseDir.exists() }
            if (!readyToBuild) {
                error("Workspace has not been setup. Try running `./gradlew applyPatches` first")
            }
        }
    }

    val initGitSubmodules = createInitGitSubmodulesTask()

    val setupUpstream = createSetupUpstreamTask {
        dependsOn(initGitSubmodules)
    }

    val importMCDev = createImportMCDevTask {
        mustRunAfter(setupUpstream)
    }

    val paperclip = createPaperclipTask {
        dependsOn(tasks.getByName("build"))
        dependsOn(subprojects.map { it.tasks.getByName("build") })
    }

    val applyPatches = createApplyPatchesTask {
        // If Paper has not been setup yet or if we modified the submodule (i.e. upstream update), patch
        if (!lastUpstream.exists()
            || !upstreamDir.resolve(".git").exists()
            || lastUpstream.readText() != gitHash(upstreamDir)
        ) {
            dependsOn(setupUpstream)
        }
        mustRunAfter(setupUpstream)
        dependsOn(importMCDev)
    }

    val rebuildPatches = createRebuildPatchesTask()

    val updateUpstream = createUpdateUpstreamTask {
        finalizedBy(setupUpstream)
    }

    val upstreamCommit = createUpstreamCommitTask()
}

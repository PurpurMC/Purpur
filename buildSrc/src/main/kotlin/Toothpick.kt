import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import task.createApplyPatchesTask
import task.createImportMCDevTask
import task.createInitGitSubmodulesTask
import task.createPaperclipTask
import task.createRebuildPatchesTask
import task.createSetupUpstreamTask
import task.createUpdateUpstreamTask
import task.createUpstreamCommitTask

@Suppress("UNUSED_VARIABLE")
class Toothpick : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<ToothpickExtension>("toothpick", project.objects)
        project.configureSubprojects()
        project.initToothpickTasks()
    }

    private fun Project.initToothpickTasks() {
        if (project.hasProperty("fast")) {
            gradle.taskGraph.whenReady {
                gradle.taskGraph.allTasks.filter {
                    it.name == "test" || it.name.contains("javadoc", ignoreCase = true)
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

        val setupUpstream = createSetupUpstreamTask() {
            dependsOn(initGitSubmodules)
        }

        val importMCDev = createImportMCDevTask() {
            mustRunAfter(setupUpstream)
        }

        val paperclip = createPaperclipTask() {
            dependsOn(tasks.getByName("build"))
            dependsOn(subprojects.map { it.tasks.getByName("build") })
        }

        val applyPatches = createApplyPatchesTask() {
            // If Paper has not been setup yet or if we modified the submodule (i.e. upstream update), patch
            afterEvaluate {
                if (!lastUpstream.exists()
                    || !upstreamDir.resolve(".git").exists()
                    || lastUpstream.readText() != gitHash(upstreamDir)
                ) {
                    dependsOn(setupUpstream)
                }
            }
            mustRunAfter(setupUpstream)
            dependsOn(importMCDev)
        }

        val rebuildPatches = createRebuildPatchesTask()

        val updateUpstream = createUpdateUpstreamTask() {
            finalizedBy(setupUpstream)
        }

        val upstreamCommit = createUpstreamCommitTask()
    }
}

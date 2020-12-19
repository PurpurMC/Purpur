package task

import ensureSuccess
import gitCmd
import org.gradle.api.Project
import org.gradle.api.Task
import reEnableGitSigning
import taskGroup
import temporarilyDisableGitSigning
import toothpick
import java.nio.file.Files

internal fun Project.createApplyPatchesTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("applyPatches") {
    receiver(this)
    group = taskGroup
    doLast {
        for ((name, subproject) in toothpick.subprojects) {
            val (sourceRepo, projectDir, patchesDir) = subproject

            // Reset or initialize subproject
            logger.lifecycle(">>> Resetting subproject $name")
            if (projectDir.exists()) {
                ensureSuccess(gitCmd("fetch", "origin", dir = projectDir))
                ensureSuccess(gitCmd("reset", "--hard", "origin/master", dir = projectDir))
            } else {
                ensureSuccess(gitCmd("clone", sourceRepo.absolutePath, projectDir.absolutePath))
            }
            logger.lifecycle(">>> Done resetting subproject $name")

            // Apply patches
            val patchPaths = Files.newDirectoryStream(patchesDir.toPath())
                .map { it.toFile() }
                .filter { it.name.endsWith(".patch") }
                .sorted()
                .takeIf { it.isNotEmpty() } ?: continue
            val patches = patchPaths.map { it.absolutePath }.toTypedArray()

            val wasGitSigningEnabled = temporarilyDisableGitSigning(projectDir)

            logger.lifecycle(">>> Applying patches to $name")

            val gitCommand = arrayListOf("am", "--3way", "--ignore-whitespace", *patches)
            ensureSuccess(gitCmd(*gitCommand.toTypedArray(), dir = projectDir, printOut = true)) {
                if (wasGitSigningEnabled) reEnableGitSigning(projectDir)
            }

            if (wasGitSigningEnabled) reEnableGitSigning(projectDir)
            logger.lifecycle(">>> Done applying patches to $name")
        }
    }
}

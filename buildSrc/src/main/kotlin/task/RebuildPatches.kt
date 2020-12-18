package task

import ensureSuccess
import gitCmd
import org.gradle.api.Project
import org.gradle.api.Task
import taskGroup
import toothpick

@Suppress("UNUSED_VARIABLE")
internal fun Project.createRebuildPatchesTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("rebuildPatches") {
    receiver(this)
    group = taskGroup
    doLast {
        for ((name, subproject) in toothpick.subprojects) {
            val (sourceRepo, projectDir, patchesDir) = subproject

            if (!patchesDir.exists()) {
                patchesDir.mkdirs()
            }

            logger.lifecycle(">>> Rebuilding patches for $name")

            // Nuke old patches
            patchesDir.listFiles()
                ?.filter { it.name.endsWith(".patch") }
                ?.forEach { it.delete() }

            // And generate new
            ensureSuccess(
                gitCmd(
                    "format-patch",
                    "--no-stat", "--zero-commit", "--full-index", "--no-signature", "-N",
                    "-o", patchesDir.absolutePath, "origin/master",
                    dir = projectDir,
                    printOut = true
                )
            )

            logger.lifecycle(">>> Done rebuilding patches for $name")
        }
    }
}

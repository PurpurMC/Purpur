package task

import cmd
import ensureSuccess
import jenkins
import org.gradle.api.Project
import org.gradle.api.Task
import rootProjectDir
import taskGroup
import toothpick

internal fun Project.createPaperclipTask(
    receiver: Task.() -> Unit = {}
): Task = tasks.create("paperclip") {
    receiver(this)
    group = taskGroup
    doLast {
        val workDir = toothpick.paperDir.resolve("work")
        val paperclipDir = workDir.resolve("Paperclip")
        val vanillaJarPath =
            workDir.resolve("Minecraft/${toothpick.minecraftVersion}/${toothpick.minecraftVersion}.jar").absolutePath
        val patchedJarPath = inputs.files.singleFile.absolutePath
        logger.lifecycle(">>> Building paperclip")
        val paperclipCmd = arrayListOf(
            "mvn", "clean", "package",
            "-Dmcver=${toothpick.minecraftVersion}",
            "-Dpaperjar=$patchedJarPath",
            "-Dvanillajar=$vanillaJarPath"
        )
        if (jenkins) paperclipCmd.add("-Dstyle.color=never")
        ensureSuccess(cmd(*paperclipCmd.toTypedArray(), dir = paperclipDir, printOut = true))
        val paperClip = paperclipDir.resolve("assembly/target/paperclip-${toothpick.minecraftVersion}.jar")
        val destination = rootProjectDir.resolve(toothpick.paperclipName)
        paperClip.copyTo(destination, overwrite = true)
        logger.lifecycle(">>> ${toothpick.paperclipName} saved to root project directory")
    }
}

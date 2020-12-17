import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import java.nio.file.Files

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

        val initGitSubmodules by tasks.registering {
            group = taskGroup
            onlyIf { !upstreamDir.resolve(".git").exists() }
            doLast {
                val exit = gitCmd("submodule", "update", "--init", "--recursive", printOut = true).exitCode
                if (exit != 0) {
                    error("Failed to checkout git submodules: git exited with code $exit")
                }
            }
        }

        val setupUpstream by tasks.registering {
            group = taskGroup
            dependsOn(initGitSubmodules)
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

        val importMCDev by tasks.registering {
            group = internalTaskGroup
            mustRunAfter(setupUpstream)
            val upstreamServer = toothpick.serverProject.baseDir
            val importLog = arrayListOf("Extra mc-dev imports")

            fun importNMS(className: String) {
                logger.lifecycle("Importing n.m.s.$className")
                importLog.add("Imported n.m.s.$className")
                val source = toothpick.paperWorkDir.resolve("spigot/net/minecraft/server/$className.java")
                if (!source.exists()) error("Missing NMS: $className")
                val target = upstreamServer.resolve("src/main/java/net/minecraft/server/$className.java")
                source.copyTo(target)
            }

            fun importLibrary(import: LibraryImport) {
                val (group, lib, prefix, file) = import
                logger.lifecycle("Importing $group.$lib $prefix/$file")
                importLog.add("Imported $group.$lib $prefix/$file")
                val source = toothpick.paperWorkDir.resolve("libraries/$group/$lib/$prefix/$file.java")
                if (!source.exists()) error("Missing Base: $lib $prefix/$file")
                val targetDir = upstreamServer.resolve("src/main/java/$prefix")
                val target = targetDir.resolve("$file.java")
                targetDir.mkdirs()
                source.copyTo(target)
            }

            doLast {
                logger.lifecycle(">>> Importing mc-dev")
                val lastCommitIsMCDev = gitCmd(
                    "log", "-1", "--oneline",
                    dir = upstreamServer
                ).output?.contains("Extra mc-dev imports") == true
                if (lastCommitIsMCDev) {
                    ensureSuccess(
                        gitCmd(
                            "reset", "--hard", "origin/master",
                            dir = upstreamServer,
                            printOut = true
                        )
                    )
                }

                (toothpick.serverProject.patchesDir.listFiles() ?: error("No patches in server?")).asSequence()
                    .flatMap { it.readLines().asSequence() }
                    .filter { it.startsWith("+++ b/src/main/java/net/minecraft/server/") }
                    .distinct()
                    .map { it.substringAfter("/server/").substringBefore(".java") }
                    .filter { !upstreamServer.resolve("src/main/java/net/minecraft/server/$it.java").exists() }
                    .map { toothpick.paperWorkDir.resolve("spigot/net/minecraft/server/$it.java") }
                    .filter {
                        val exists = it.exists()
                        if (!it.exists()) logger.lifecycle("NMS ${it.nameWithoutExtension} is either missing, or is a new file added through a patch")
                        exists
                    }
                    .map { it.nameWithoutExtension }
                    .forEach(::importNMS)

                // Imports from MCDevImports.kt
                nmsImports.forEach(::importNMS)
                libraryImports.forEach(::importLibrary)

                val add = gitCmd("add", ".", "-A", dir = upstreamServer).exitCode == 0
                val commit = gitCmd("commit", "-m", importLog.joinToString("\n"), dir = upstreamServer).exitCode == 0
                if (!add || !commit) {
                    logger.lifecycle(">>> Didn't import any extra files")
                }
                logger.lifecycle(">>> Done importing mc-dev")
            }
        }

        val paperclip by tasks.registering {
            group = taskGroup
            dependsOn(tasks.getByName("build"))
            dependsOn(subprojects.map { it.tasks.getByName("build") })
            doLast {
                val workDir = toothpick.paperDir.resolve("work")
                val paperclipDir = workDir.resolve("Paperclip")
                val vanillaJarPath =
                    workDir.resolve("Minecraft/${toothpick.minecraftVersion}/${toothpick.minecraftVersion}.jar").absolutePath
                val patchedJarPath = toothpick.serverProject.projectDir.resolve(
                    "build/libs/${toothpick.forkNameLowercase}-server-$version-all.jar"
                ).absolutePath
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
                val destination = rootProjectDir.resolve("${toothpick.forkNameLowercase}-paperclip.jar")
                paperClip.copyTo(destination, overwrite = true)
                logger.lifecycle(">>> ${toothpick.forkNameLowercase}-paperclip.jar saved to root project directory")
            }
        }

        val applyPatches by tasks.registering {
            group = taskGroup
            // If Paper has not been setup yet or if we modified the submodule (i.e. upstream update), patch
            if (!lastUpstream.exists()
                || !upstreamDir.resolve(".git").exists()
                || lastUpstream.readText() != gitHash(upstreamDir)
            ) {
                dependsOn(setupUpstream)
            }
            mustRunAfter(setupUpstream)
            dependsOn(importMCDev)
            doLast {
                for ((name, subproject) in toothpick.subprojects) {
                    val (sourceRepo, projectDir, patchesDir) = subproject

                    // Reset or initialize subproject
                    logger.lifecycle(">>> Resetting subproject $name")
                    if (projectDir.exists()) {
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

        val rebuildPatches by tasks.registering {
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

        val updateUpstream by tasks.registering {
            group = taskGroup
            doLast {
                ensureSuccess(gitCmd("fetch", dir = upstreamDir, printOut = true))
                ensureSuccess(gitCmd("reset", "--hard", "origin/master", dir = upstreamDir, printOut = true))
                ensureSuccess(gitCmd("add", toothpick.upstream, dir = rootProjectDir, printOut = true))
                ensureSuccess(gitCmd("submodule", "update", "--recursive", dir = upstreamDir, printOut = true))
            }
            finalizedBy(setupUpstream)
        }
    }
}

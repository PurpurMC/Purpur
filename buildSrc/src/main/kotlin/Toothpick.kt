import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Suppress("UNUSED_VARIABLE")
class Toothpick : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<ToothpickExtension>("toothpick", project.objects)
        project.initToothpickTasks()
    }

    private fun Project.initToothpickTasks() {
        tasks.getting(DefaultTask::class) {
            if (!projectDir.resolve("Paper/.git").exists()
                || toothpick.subprojects.values.any { !it.projectDir.exists() }
            ) {
                error("Workspace has not been setup. Try running `./gradlew applyPatches` first")
            }
            finalizedBy(tasks.getting(PublishToMavenLocal::class))
        }

        val initGitSubmodules by tasks.registering {
            group = taskGroup
            onlyIf { !projectDir.resolve("Paper/.git").exists() }
            doLast {
                val exit = cmd("git", "submodule", "update", "--init", "--recursive", printOut = true).exitCode
                if (exit != 0) {
                    error("Failed to checkout git submodules: git exited with code $exit")
                }
            }
        }

        val setupPaper by tasks.registering {
            group = taskGroup
            dependsOn(initGitSubmodules)
            doLast {
                val paperDir = rootProject.projectDir.resolve("Paper")
                val result = cmd("bash", "-c", "./paper patch", dir = paperDir, printOut = true)
                if (result.exitCode != 0) {
                    error("Failed to apply Paper patches: script exited with code ${result.exitCode}")
                }
            }
        }

        val importMCDev by tasks.registering {
            group = internalTaskGroup
            mustRunAfter(setupPaper)
            val paperDir = rootProject.projectPath.resolve("Paper")
            val paperServer = paperDir.resolve("Paper-Server")
            val importLog = arrayListOf("Extra mc-dev imports")

            fun importNMS(className: String) {
                logger.lifecycle("Importing n.m.s.$className")
                importLog.add("Imported n.m.s.$className")
                val source =
                    paperDir.resolve("work/Minecraft/${toothpick.minecraftVersion}/spigot/net/minecraft/server/$className.java")
                if (!Files.exists(source)) error("Missing NMS: $className")
                val target = paperServer.resolve("src/main/java/net/minecraft/server/$className.java")
                Files.copy(source, target)
            }

            fun importLibrary(import: LibraryImport) {
                val (group, lib, prefix, file) = import
                logger.lifecycle("Importing $group.$lib $prefix/$file")
                importLog.add("Imported $group.$lib $prefix/$file")
                val source =
                    paperDir.resolve("work/Minecraft/${toothpick.minecraftVersion}/libraries/$group/$lib/$prefix/$file.java")
                if (!Files.exists(source)) error("Missing Base: $lib $prefix/$file")
                val targetDir = paperServer.resolve("src/main/java/$prefix")
                val target = targetDir.resolve("$file.java")
                Files.createDirectories(targetDir)
                Files.copy(source, target)
            }

            doLast {
                logger.lifecycle(">>> Importing mc-dev")
                if (cmd(
                        "git", "log", "-1", "--oneline",
                        dir = paperServer.toFile()
                    ).output?.contains("Extra mc-dev imports") == true
                ) {
                    ensureSuccess(
                        cmd(
                            "git", "reset", "--hard", "origin/master",
                            dir = paperServer.toFile(),
                            printOut = true
                        )
                    )
                }

                (toothpick.serverProject.patchesDir.listFiles() ?: error("No patches in server?")).asSequence()
                    .flatMap { it.readLines().asSequence() }
                    .filter { it.startsWith("+++ b/src/main/java/net/minecraft/server/") }
                    .distinct()
                    .map { it.substringAfter("/server/").substringBefore(".java") }
                    .filter { !Files.exists(paperServer.resolve("src/main/java/net/minecraft/server/$it.java")) }
                    .map { paperDir.resolve("work/Minecraft/${toothpick.minecraftVersion}/spigot/net/minecraft/server/$it.java") }
                    .filter {
                        val exists = Files.exists(it)
                        if (!exists) logger.lifecycle("NMS ${it.toFile().nameWithoutExtension} is either missing, or is a new file added through a patch")
                        exists
                    }
                    .map { it.toFile().nameWithoutExtension }
                    .forEach(::importNMS)

                nmsImports.forEach(::importNMS)
                libraryImports.forEach(::importLibrary)

                Files.list(paperServer).filter { file -> file.toFile().name == "nms-patches" }.forEach { path ->
                    Files.deleteIfExists(path) // todo: why do we need this?
                }
                ensureSuccess(
                    cmd(
                        "git", "add", ".", "-A",
                        dir = paperServer.toFile()
                    )
                )
                ensureSuccess(
                    cmd(
                        "git", "commit", "-m", importLog.joinToString("\n"),
                        dir = paperServer.toFile()
                    )
                )
                logger.lifecycle(">>> Done importing mc-dev")
            }
        }

        val paperclip by tasks.registering {
            group = taskGroup
            dependsOn(tasks.getByName("build"))
            dependsOn(subprojects.map { it.tasks.getByName("build") })
            doLast {
                val workDir = rootProject.projectPath.resolve("Paper/work")
                val paperclipDir = workDir.resolve("Paperclip")
                val vanillaJarPath =
                    workDir.resolve("Minecraft/${toothpick.minecraftVersion}/${toothpick.minecraftVersion}.jar")
                        .toFile().absolutePath
                val patchedJarPath = toothpick.serverProject.projectDir.resolve(
                    "build/libs/${toothpick.forkNameLowercase}-server-$version-all.jar"
                ).absolutePath
                logger.lifecycle(">>> Building paperclip")
                ensureSuccess(
                    cmd(
                        "mvn", "clean", "package",
                        "-Dmcver=${toothpick.minecraftVersion}",
                        "-Dpaperjar=$patchedJarPath",
                        "-Dvanillajar=$vanillaJarPath",
                        dir = paperclipDir.toFile(), printOut = true
                    )
                )
                val paperClip = paperclipDir.resolve("assembly/target/paperclip-${toothpick.minecraftVersion}.jar")
                val destination =
                    rootProject.projectPath.resolve("${toothpick.forkNameLowercase}-paperclip.jar")
                Files.copy(paperClip, destination, StandardCopyOption.REPLACE_EXISTING)
                logger.lifecycle(">>> ${toothpick.forkNameLowercase}-paperclip.jar saved to root project directory")
            }
        }

        val applyPatches by tasks.registering {
            group = taskGroup
            // If Paper has not been setup yet or if we modified the submodule (i.e. upstream update), patch
            if (!projectDir.resolve("Paper/.git").exists()
                || cmd("git", "diff-index", "--quiet", "HEAD", "Paper").exitCode != 0
            ) {
                dependsOn(setupPaper)
            }
            mustRunAfter(setupPaper)
            dependsOn(importMCDev)
            doLast {
                for ((name, subproject) in toothpick.subprojects) {
                    val (sourceRepo, projectDir, patchesDir) = subproject

                    // Reset or initialize subproject
                    logger.lifecycle(">>> Resetting subproject $name")
                    if (projectDir.exists()) {
                        ensureSuccess(cmd("git", "reset", "--hard", "origin/master", dir = projectDir))
                    } else {
                        ensureSuccess(cmd("git", "clone", sourceRepo.absolutePath, projectDir.absolutePath))
                    }
                    logger.lifecycle(">>> Done resetting subproject $name")

                    // Apply patches
                    val patches = patchesDir.listFiles()
                        ?.sortedArray()
                        ?.filter { it.name.endsWith(".patch") }
                        ?.takeIf { it.isNotEmpty() } ?: continue

                    logger.lifecycle(">>> Applying patches to $name")
                    val gitCommand = arrayListOf("git", "am", "--3way", "--ignore-whitespace")
                    gitCommand.addAll(patches.map { it.absolutePath })
                    ensureSuccess(cmd(*gitCommand.toTypedArray(), dir = projectDir, printOut = true))
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
                        cmd(
                            "git", "format-patch",
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
                val paperDir = rootProject.projectDir.resolve("Paper")
                ensureSuccess(cmd("git", "fetch", dir = paperDir, printOut = true))
                ensureSuccess(cmd("git", "reset", "--hard", "origin/master", dir = paperDir, printOut = true))
                ensureSuccess(cmd("git", "add", "Paper", dir = rootProject.projectDir, printOut = true))
            }
            finalizedBy(setupPaper)
        }
    }
}

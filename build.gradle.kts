import io.papermc.paperweight.util.Git

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("io.papermc.paperweight.patcher") version "1.0.0-SNAPSHOT"
}

repositories {
    mavenCentral()
    maven("https://wav.jfrog.io/artifactory/repo/") {
        content {
            onlyForConfigurations("paperclip")
        }
    }
    maven("https://maven.quiltmc.org/repository/release/") {
        content {
            onlyForConfigurations("remapper")
        }
    }
}

dependencies {
    remapper("org.quiltmc:tiny-remapper:0.4.1")
    paperclip("io.papermc:paperclip:2.0.0-SNAPSHOT@jar")
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(16)
    }

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://ci.emc.gs/nexus/content/groups/aikar/")
        maven("https://repo.aikar.co/content/groups/aikar")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://nexus.velocitypowered.com/repository/velocity-artifacts-snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

/*
val paperDir = layout.projectDirectory.dir("Paper")
val paperBranch = "dev/1.17"

val initSubmodules by tasks.registering {
    group = "paperweight"

    outputs.upToDateWhen { false }
    doLast {
        paperDir.asFile.mkdirs()
        Git(layout.projectDirectory)("submodule", "update", "--init", "--recursive").executeOut()
    }
}

val upstreamUpdate by tasks.registering {
    group = "paperweight"

    outputs.upToDateWhen { false }
    finalizedBy(cleanUpstreamCaches)

    doLast {
        Git(paperDir)("fetch").executeOut()
        Git(paperDir)("clean", "-fd").executeOut()
        Git(paperDir)("reset", "--hard", "origin/$paperBranch").executeOut()
        Git(layout.projectDirectory)("add", "--force", paperDir.asFile.name).executeOut()
        Git(layout.projectDirectory)("submodule", "update", "--init", "--recursive").executeOut()
    }
}

val cleanUpstreamCaches by tasks.registering(GradleBuild::class) {
    dir = paperDir.asFile
    tasks = listOf("cleanCache")
}

val upstreamCommit by tasks.registering {
    group = "paperweight"

    outputs.upToDateWhen { false }
    doLast {
        val old = Git(layout.projectDirectory)("ls-tree", "HEAD", paperDir.asFile.name).readText()
            ?.substringAfter("commit ")?.substringBefore("\t")
        val changes = Git(paperDir)("log", "--oneline", "$old...HEAD").readText()
        changes ?: run {
            println("No changes to commit?")
            return@doLast
        }
        val commitMessage = """
            |Updated Upstream (Paper)
            |
            |Upstream has released updates that appear to apply and compile correctly.
            |
            |Paper Changes:
            |$changes
        """.trimMargin()
        Git(layout.projectDirectory)("commit", "-m", commitMessage).executeOut()
    }
}
*/

paperweight {
    serverProject.set(project(":Purpur-Server"))

    usePaperUpstream(providers.gradleProperty("paperRef")) {
        withPaperPatcher {
            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            apiOutputDir.set(layout.projectDirectory.dir("Purpur-API"))

            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
            serverOutputDir.set(layout.projectDirectory.dir("Purpur-Server"))
        }
    }

    /*
    upstreams {
        register("paper") {
            upstreamDataTask {
                dependsOn(initSubmodules)
                projectDir.set(paperDir)
                workDir.set(layout.projectDirectory)
            }

            patchTasks {
                register("api") {
                    sourceDir.set(paperDir.dir("Paper-API"))
                    patchDir.set(layout.projectDirectory.dir("patches/api"))
                    outputDir.set(layout.projectDirectory.dir("Purpur-API"))
                }
                register("server") {
                    sourceDir.set(paperDir.dir("Paper-Server"))
                    patchDir.set(layout.projectDirectory.dir("patches/server"))
                    outputDir.set(layout.projectDirectory.dir("Purpur-Server"))
                }
            }
        }
    }
     */
}

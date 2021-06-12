import io.papermc.paperweight.util.Git

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("io.papermc.paperweight.patcher") version "1.0.0-SNAPSHOT"
}

group = "net.pl3x.purpur"
version = providers.gradleProperty("projectVersion").forUseAtConfigurationTime().get()

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

dependencies {
    implementation(gradleApi())
}

val paperDir = layout.projectDirectory.dir("Paper")
val initSubmodules by tasks.registering {
    outputs.upToDateWhen { false }
    doLast {
        paperDir.asFile.mkdirs()
        Git(paperDir)("submodule", "update", "--init", "--recursive").executeOut()
    }
}

paperweight {
    serverProject.set(project(":Purpur-Server"))

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
}

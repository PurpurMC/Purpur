plugins {
    `java-library`
    `maven-publish`
    toothpick
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
}

toothpick {
    forkName = "Purpur"
    groupId = "net.pl3x.purpur"

    minecraftVersion = "1.16.4"
    nmsRevision = "R0.1-SNAPSHOT"
    val gitHash = cmd("git", "rev-parse", "--short", "HEAD").output
    forkVersion = "git-$forkName-\"$gitHash\""

    serverProject = ToothpickSubproject(
        rootProject.projectDir.resolve("Paper/Paper-Server"),
        project(":$forkNameLowercase-server").projectDir,
        rootProject.projectDir.resolve("patches/server")
    )
    apiProject = ToothpickSubproject(
        rootProject.projectDir.resolve("Paper/Paper-API"),
        project(":$forkNameLowercase-api").projectDir,
        rootProject.projectDir.resolve("patches/api")
    )
}

subprojects {
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()

    repositories {
        mavenLocal()
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = project.name
                groupId = rootProject.group as String
                version = rootProject.version as String
                from(components["java"])
                pom {
                    name.set(project.name)
                    url.set("https://github.com/pl3xgaming/Purpur")
                }
            }
        }
    }
}
plugins {
    `java-library`
    toothpick
}

toothpick {
    forkName = "Purpur"
    groupId = "net.pl3x.purpur"

    minecraftVersion = "1.16.4"
    nmsRevision = "R0.1-SNAPSHOT"
    val versionTag = System.getenv("BUILD_NUMBER")
        ?: "\"${gitCmd("rev-parse", "--short", "HEAD").output}\""
    forkVersion = "git-$forkName-$versionTag"

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
    repositories {
        mavenCentral()
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://nexus.velocitypowered.com/repository/velocity-artifacts-snapshots/")
        maven("https://libraries.minecraft.net")
        mavenLocal()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
    }
}

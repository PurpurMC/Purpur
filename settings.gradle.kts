pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://wav.jfrog.io/artifactory/repo/")
        mavenLocal()
    }
}

rootProject.name = "Purpur"

include("Purpur-API", "Purpur-Server")

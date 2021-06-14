pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://wav.jfrog.io/artifactory/repo/")
    }
}

rootProject.name = "Purpur"
include("Purpur-API", "Purpur-MojangAPI", "Purpur-Server")

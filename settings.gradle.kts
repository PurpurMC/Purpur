pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.pl3x.net/snapshots/")
    }
}

rootProject.name = "Purpur"
include("Purpur-API", "Purpur-Server")

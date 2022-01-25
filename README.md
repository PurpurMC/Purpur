<div align="center">
<a href="https://purpurmc.org"><img src="https://user-images.githubusercontent.com/74448585/150906023-101cd383-da82-4a3c-9603-a3b5741c3994.png" alt="Purpur"></a>

## Purpur

[![MIT License](https://img.shields.io/github/license/PurpurMC/Purpur?&logo=github)](LICENSE)
[![Github Actions Build](https://img.shields.io/github/workflow/status/PurpurMC/purpur/Build?event=push&logo=github)](https://purpurmc.org/downloads/)
[![CodeFactor](https://www.codefactor.io/repository/github/PurpurMC/purpur/badge)](https://www.codefactor.io/repository/github/PurpurMC/purpur)
[![Join us on Discord](https://img.shields.io/discord/685683385313919172.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://purpurmc.org/discord)

[![Purpur's Stargazers](https://img.shields.io/github/stars/PurpurMC/Purpur?label=stars&logo=github)](https://github.com/PurpurMC/Purpur/stargazers)
[![Purpur Forks](https://img.shields.io/github/forks/PurpurMC/Purpur?label=forks&logo=github)](https://github.com/PurpurMC/Purpur/network/members)
[![Purpur Watchers](https://img.shields.io/github/watchers/PurpurMC/Purpur?label=watchers&logo=github)](https://github.com/PurpurMC/Purpur/watchers)

Purpur is a drop-in replacement for [Paper](https://github.com/PaperMC/Paper) servers designed for configurability, new fun and exciting gameplay features, and performance built on top of [Paper](https://github.com/PaperMC/Paper/).

</div>

## Contact
[![Join us on Discord](https://img.shields.io/discord/685683385313919172.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/mtAAnkk)

Join us on [Discord](https://discord.gg/mtAAnkk)

## Downloads
[![Downloads](https://img.shields.io/github/workflow/status/PurpurMC/purpur/Build?event=push&label=Downloads&logo=github)](https://purpurmc.org/downloads)

Downloads can be obtained from the [downloads page](https://purpurmc.org/downloads/) or the [downloads API](https://api.purpurmc.org).

Downloads API endpoints:
 * List versions of Minecraft with builds available:
   `https://api.purpurmc.org/v2/purpur`
 * List builds for a version of Minecraft:
   `https://api.purpurmc.org/v2/purpur/<version>`
 * Download a specific build of a specific version:
   `https://api.purpurmc.org/v2/purpur/<version>/<build>/download`
 * Download the latest build for a version of Minecraft:
   `https://api.purpurmc.org/v2/purpur/<version>/latest/download`

## License
[![MIT License](https://img.shields.io/github/license/PurpurMC/Purpur?&logo=github)](LICENSE)

All patches are licensed under the MIT license, unless otherwise noted in the patch headers.

See [PaperMC/Paper](https://github.com/PaperMC/Paper), and [PaperMC/Paperweight](https://github.com/PaperMC/paperweight) for the license of material used by this project.

## bStats

[![bStats Graph Data](https://bstats.org/signatures/server-implementation/Purpur.svg)](https://bstats.org/plugin/server-implementation/Purpur)


## API

### [Javadoc](https://purpurmc.org/javadoc)

### Dependency Information
Maven
```xml
<repository>
    <id>purpur</id>
    <url>https://repo.purpurmc.org/snapshots</url>
</repository>
```
```xml
<dependency>
    <groupId>org.purpurmc.purpur</groupId>
    <artifactId>purpur-api</artifactId>
    <version>1.18.1-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

Gradle
```kotlin
repositories {
    maven("https://repo.purpurmc.org/snapshots")
}
```
```kotlin
dependencies {
    compileOnly("org.purpurmc.purpur", "purpur-api", "1.18.1-R0.1-SNAPSHOT")
}
```

Yes, this also includes all API provided by Paper, Spigot, and Bukkit.

## Building and setting up

#### Initial setup
Run the following commands in the root directory:

```
./gradlew applyPatches
```

#### Creating a patch
Patches are effectively just commits in either `Purpur-API` or `Purpur-Server`. 
To create one, just add a commit to either repo and run `./gradlew rebuildPatches`, and a 
patch will be placed in the patches folder. Modifying commits will also modify its 
corresponding patch file.

See [CONTRIBUTING.md](CONTRIBUTING.md) for more detailed information.


#### Compiling

Use the command `./gradlew build` to build the API and server. Compiled JARs
will be placed under `Purpur-API/build/libs` and `Purpur-Server/build/libs`.

To get a purpurclip jar, run `./gradlew createReobfPaperclipJar`.
To install the `purpur-api` and `purpur` dependencies to your local Maven repo, run `./gradlew publishToMavenLocal`

Special Thanks To:
-------------

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](https://www.yourkit.com/), makers of the outstanding Java profiler,
support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler)
and [.NET](https://www.yourkit.com/.net/profiler) application profilers. We thank them for allowing us to use their
software so we can make Purpur the best it can be.


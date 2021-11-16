<div align="center">
<a href="https://purpur.pl3x.net"><img src="https://repository-images.githubusercontent.com/184300222/14b11480-3303-11eb-8ca4-ea5711d942fb" alt="Purpur"></a>

## Purpur

[![MIT License](https://img.shields.io/github/license/pl3xgaming/Purpur?&logo=github)](License)
[![Github Actions Build](https://img.shields.io/github/workflow/status/pl3xgaming/purpur/Build?event=push&logo=github)](https://purpur.pl3x.net/downloads/)
[![CodeFactor](https://www.codefactor.io/repository/github/pl3xgaming/purpur/badge)](https://www.codefactor.io/repository/github/pl3xgaming/purpur)
[![Join us on Discord](https://img.shields.io/discord/685683385313919172.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/mtAAnkk)

[![Purpur's Stargazers](https://img.shields.io/github/stars/pl3xgaming/Purpur?label=stars&logo=github)](https://github.com/pl3xgaming/Purpur/stargazers)
[![BillyGalbreath's Followers](https://img.shields.io/github/followers/BillyGalbreath?label=followers&logo=github)](https://github.com/BillyGalbreath?tab=followers)
[![Purpur Forks](https://img.shields.io/github/forks/pl3xgaming/Purpur?label=forks&logo=github)](https://github.com/pl3xgaming/Purpur/network/members)
[![Purpur Watchers](https://img.shields.io/github/watchers/pl3xgaming/Purpur?label=watchers&logo=github)](https://github.com/pl3xgaming/Purpur/watchers)

Purpur is a drop-in replacement for [Paper](https://github.com/PaperMC/Paper) servers designed for configurability, new fun and exciting gameplay features, and performance built on top of [Airplane](https://github.com/TECHNOVE/Airplane/).

</div>

## Contact
[![Join us on Discord](https://img.shields.io/discord/685683385313919172.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/mtAAnkk)

Join us on [Discord](https://discord.gg/mtAAnkk)

## Downloads
[![Downloads](https://img.shields.io/github/workflow/status/pl3xgaming/purpur/Build?event=push&label=Downloads&logo=github)](https://purpur.pl3x.net/downloads)

Downloads can be obtained from the [downloads page](https://purpur.pl3x.net/downloads/) or the [downloads API](https://api.pl3x.net).

Latest build shortcut links:
* [1.17.1](https://api.pl3x.net/v2/purpur/1.17.1/latest/download) builds 1256+
* [1.17](https://api.pl3x.net/v2/purpur/1.17/latest/download) builds 1172-1255
* [1.16.5](https://api.pl3x.net/v2/purpur/1.16.5/latest/download) builds 957-1171
* [1.16.4](https://api.pl3x.net/v2/purpur/1.16.4/latest/download) builds 809-956
* [1.16.3](https://api.pl3x.net/v2/purpur/1.16.3/latest/download) builds 751-808
* [1.16.2](https://api.pl3x.net/v2/purpur/1.16.2/latest/download) builds 711-750
* [1.16.1](https://api.pl3x.net/v2/purpur/1.16.1/latest/download) builds 608-710
* [1.15.2](https://api.pl3x.net/v2/purpur/1.15.2/latest/download) builds 398-606
* [1.15.1](https://api.pl3x.net/v2/purpur/1.15.1/latest/download) builds 348-397
* [1.15](https://api.pl3x.net/v2/purpur/1.15/latest/download) builds 339-346
* [1.14.x](https://api.pl3x.net/v2/purpur/1.14.4/latest/download) builds 337 and below


Downloads API endpoints:
 * List versions of Minecraft with builds available:
   `https://api.pl3x.net/v2/purpur`
 * List builds for a version of Minecraft:
   `https://api.pl3x.net/v2/purpur/<version>`
 * Download a specific build of a specific version:
   `https://api.pl3x.net/v2/purpur/<version>/<build>/download`
 * Download the latest build for a version of Minecraft:
   `https://api.pl3x.net/v2/purpur/<version>/latest/download`

## License
[![MIT License](https://img.shields.io/github/license/pl3xgaming/Purpur?&logo=github)](LICENSE)

All patches are licensed under the MIT license, unless otherwise noted in the patch headers.

See [PaperMC/Paper](https://github.com/PaperMC/Paper), [TECHNOVE/Airplane](https://github.com/TECHNOVE/Airplane), and [PaperMC/Paperweight](https://github.com/PaperMC/Paperweight) for the license of material used by this project.

## bStats

[![bStats Graph Data](https://bstats.org/signatures/server-implementation/Purpur.svg)](https://bstats.org/plugin/server-implementation/Purpur)


## API

### [Javadoc](https://purpur.pl3x.net/javadoc)

### Dependency Information
Maven
```xml
<repository>
    <id>purpur</id>
    <url>https://repo.pl3x.net/</url>
</repository>
```
```xml
<dependency>
    <groupId>net.pl3x.purpur</groupId>
    <artifactId>purpur-api</artifactId>
    <version>1.17.1-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

Gradle
```kotlin
repositories {
    maven("https://repo.pl3x.net/")
}
```
```kotlin
dependencies {
    compileOnly("net.pl3x.purpur", "purpur-api", "1.17.1-R0.1-SNAPSHOT")
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

To get a purpurclip jar, run `./gradlew paperclip`.
To install the `purpur-api` and `purpur` dependencies to your local Maven repo, run `./gradlew publishToMavenLocal`

Special Thanks To:
-------------

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](https://www.yourkit.com/), makers of the outstanding Java profiler,
support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler)
and [.NET](https://www.yourkit.com/.net/profiler) application profilers. We thank them for allowing us to use their
software so we can make Purpur the best it can be.


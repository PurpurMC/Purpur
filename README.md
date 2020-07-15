Pugpur
==

Pugpur is a fork of Purpur.

It contains many gameplay changes to suit our server that are deemed too wild to be included directly upstream into Paper.

## Contact
[Discord](https://discord.gg/UAJ7JBm)

## License
Everything is licensed under the MIT license, and is free to be used in your own fork.

See [starlis/empirecraft](https://github.com/starlis/empirecraft) and [electronicboy/byof](https://github.com/electronicboy/byof) 
for the license of material used/modified by this project.

## bStats (Purpur)

[![bStats Graph Data](https://bstats.org/signatures/server-implementation/Purpur.svg)](https://bstats.org/plugin/server-implementation/Purpur)

## Plugin developers

Purpur API maven dependency:
```
<dependency>
    <groupId>net.pl3x.purpur</groupId>
    <artifactId>purpur-api</artifactId>
    <version>1.15.2-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```
```
<repository>
    <id>purpur</id>
    <url>https://repo.pl3x.net/</url>
</repository>
```
Yes, this also includes all API provided by Paper, Spigot, and Bukkit.

## Building and setting up
Run the following commands in the root directory:

```
git submodule update --init
./purpur up
./purpur patch
```

#### Creating a patch
Patches are effectively just commits in either `Purpur-API` or `Purpur-Server`. 
To create one, just add a commit to either repo and run `./purpur rb`, and a 
patch will be placed in the patches folder. Modifying commits will also modify its 
corresponding patch file.

See [Paper's contributing guideline](https://github.com/PaperMC/Paper/blob/master/CONTRIBUTING.md) for more detailed information.


#### Building

Use the command `./purpur build` to build the api and server. Compiled jars
will be placed under `Purpur-API/target` and `Purpur-Server/target`.

To get a purpurclip jar, run `./purpur jar`.

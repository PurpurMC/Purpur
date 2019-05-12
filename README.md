Purpur
==

Purpur is a fork of Paper used by the Pl3xCraft server.

It contains many gameplay changes to suit our server that are deemed too wild to be included directly upstream into Paper.

## Contact
[Discord](https://discord.gg/zAs8W5u)

## License
Everything is licensed under the MIT license, and is free to be used in your own fork.

See [EMC](https://github.com/starlis/empirecraft) and [byof](https://github.com/electronicboy/byof) 
for the license of material used/modified by this project.

## Building and setting up
Run the following commands in the root directory:

```
git submodule init
git submodule update
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

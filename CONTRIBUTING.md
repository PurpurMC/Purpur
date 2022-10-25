Contributing to Purpur
==========================
Purpur is happy you're willing to contribute to our projects. We are usually
very lenient with all submitted PRs, but there are still some guidelines you
can follow to make the approval process go more smoothly.

## Use a Personal Fork and not Organization

Purpur will routinely modify your PR, whether it's a quick rebase or to take care
of any minor nitpicks we might have. Often, it's better for us to solve these
problems for you than make you go back and forth trying to fix it yourself.

Unfortunately, if you use an organization for your PR, it prevents Purpur from
modifying it. This requires us to manually merge your PR, resulting in us
closing the PR instead of marking it as merged.

We much prefer to have PRs show as merged, so please do not use repositories
on organizations for PRs.

See <https://github.com/isaacs/github/issues/1681> for more information on the
issue.

## Requirements

To get started with PRing changes, you'll need the following software, most of
which can be obtained in (most) package managers such as `apt` (Debian / Ubuntu;
you will most likely use this for WSL), `homebrew` (macOS / Linux), and more:

- `git` (package `git` everywhere);
- A Java 16 or later JDK (packages vary, use Google/DuckDuckGo/etc.).
    - [Adoptium](https://adoptium.net/) has builds for most operating systems.
    - Purpur requires JDK 16 to build, however makes use of Gradle's
      [Toolchains](https://docs.gradle.org/current/userguide/toolchains.html)
      feature to allow building with only JRE 8 or later installed. (Gradle will
      automatically provision JDK 16 for compilation if it cannot find an existing
      install).

If you're on Windows, check
[the section on WSL](#patching-and-building-is-really-slow-what-can-i-do).

If you're compiling with Docker, you can use Adoptium's
[`eclipse-temurin`](https://hub.docker.com/_/eclipse-temurin/) images like so:

```console
# docker run -it -v "$(pwd)":/data --rm eclipse-temurin:16.0.2_7-jdk bash
Pulling image...

root@abcdefg1234:/# javac -version
javac 16.0.2
```

## Understanding Patches

Purpur is mostly patches and extensions to Paper/Spigot. These patches/extensions are
split into different directories which target certain parts of the code. These
directories are:

- `Purpur-API` - Modifications to `Paper-API`;
- `Purpur-Server` - Modifications to `Paper-Server`.

Because the entire structure is based on patches and git, a basic understanding
of how to use git is required. A basic tutorial can be found here:
<https://git-scm.com/docs/gittutorial>.

Assuming you have already forked the repository:

1. Clone your fork to your local machine;
2. Type `./gradlew applyPatches` in a terminal to apply the changes from upstream.
   On Windows, leave out the `./` at the beginning for all `gradlew` commands;
3. cd into `Purpur-Server` for server changes, and `Purpur-API` for API changes.
<!--You can also run `./paper server` or `./paper api` for these same directories
respectively.
1. You can also run `./paper setup`, which allows you to type `paper <command>`
from anywhere in the Paper structure in most cases.-->

`Purpur-Server` and `Purpur-API` aren't git repositories in the traditional sense:

- `base` points to the unmodified source before Purpur patches have been applied.
- Each commit after `base` is a patch.

## Adding Patches

Adding patches to Purpur is very simple:

1. Modify `Purpur-Server` and/or `Purpur-API` with the appropriate changes;
1. Type `git add .` inside these directories to add your changes;
1. Run `git commit` with the desired patch message;
1. Run `./gradlew rebuildPatches` in the main directory to convert your commit into a new
   patch;
1. PR the generated patch file(s) back to this repository.

Your commit will be converted into a patch that you can then PR into Purpur.

> ❗ Please note that if you have some specific implementation detail you'd like
> to document, you should do so in the patch message *or* in comments.

## Modifying Patches

Modifying previous patches is a bit more complex:

### Method 1

This method works by temporarily resetting your `HEAD` to the desired commit to
edit it using `git rebase`.

> ❗ While in the middle of an edit, you will not be able to compile unless you
> *also* reset the opposing module(s) to a related commit. In the API's case,
> you must reset the Server, and reset the API if you're editing the Server.
> Note also that either module _may_ not compile when doing so. This is not
> ideal nor intentional, but it happens. Feel free to fix this in a PR to us!

1. If you have changes you are working on, type `git stash` to store them for
   later;
    - You can type `git stash pop` to get them back at any point.
1. Type `git rebase -i base`;
    - It should show something like
      [this](https://gist.github.com/zachbr/21e92993cb99f62ffd7905d7b02f3159) in
      the text editor you get.
    - If your editor does not have a "menu" at the bottom, you're using `vim`.  
      If you don't know how to use `vim` and don't want to
      learn, enter `:q!` and press enter. Before redoing this step, do
      `export EDITOR=nano` for an easier editor to use.
1. Replace `pick` with `edit` for the commit/patch you want to modify, and
   "save" the changes;
    - Only do this for **one** commit at a time.
1. Make the changes you want to make to the patch;
1. Type `git add .` to add your changes;
1. Type `git commit --amend` to commit;
    - **Make sure to add `--amend`** or else a new patch will be created.
    - You can also modify the commit message and author here.
1. Type `git rebase --continue` to finish rebasing;
1. Type `./gradlew rebuildPatches` in the root directory;
    - This will modify the appropriate patches based on your commits.
1. PR your modified patch file(s) back to this repository.

### Method 2 - Fixup commits

If you are simply editing a more recent commit or your change is small, simply
making the change at HEAD and then moving the commit after you have tested it
may be easier.

This method has the benefit of being able to compile to test your change without
messing with your HEADs.

#### Manual method

1. Make your change while at HEAD;
1. Make a temporary commit. You don't need to make a message for this;
1. Type `git rebase -i base`, move (cut) your temporary commit and
   move it under the line of the patch you wish to modify;
1. Change the `pick` to the appropriate action:
    1. `f`/`fixup`: Merge your changes into the patch without touching the
       message.
    1. `s`/`squash`: Merge your changes into the patch and use your commit message
       and subject.
1. Type `./gradlew rebuildPatches` in the root directory;
    - This will modify the appropriate patches based on your commits.
1. PR your modified patch file(s) back to this repository.

#### Automatic method

1. Make your change while at HEAD;
1. Make a fixup commit. `git commit -a --fixup <hashOfPatchToFix>`;
    - You can also use `--squash` instead of `--fixup` if you want the commit
      message to also be changed.
    - You can get the hash by looking at `git log` or `git blame`; your IDE can
      assist you too.
    - Alternatively, if you only know the name of the patch, you can do
      `git commit -a --fixup "Subject of Patch name"`.
1. Rebase with autosquash: `git rebase --autosquash -i base`.
   This will automatically move your fixup commit to the right place, and you just
   need to "save" the changes.
1. Type `./gradlew rebuildPatches` in the root directory;
    - This will modify the appropriate patches based on your commits.
1. PR your modified patch file(s) back to this repository.

## Rebasing PRs

Steps to rebase a PR to include the latest changes from `master`.  
These steps assume the `origin` remote is your fork of this repository and `upstream` is the official Purpur repository.

1. Pull the latest changes from upstreams master: `git checkout master && git pull upstream master`.
1. Checkout feature/fix branch and rebase on master: `git checkout patch-branch && git rebase master`.
1. Apply updated patches: `./gradlew applyPatches`.
1. If there are conflicts, fix them.
1. If your PR creates new patches instead of modifying exist ones, in both the `Purpur-Server` and `Purpur-API` directories, ensure your newly-created patch is the last commit by either:
    * Renaming the patch file with a large 4-digit number in front (e.g. 9999-Patch-to-add-some-new-stuff.patch), and re-applying patches.
    * Running `git rebase --interactive base` and moving the commits to the end.
1. Rebuild patches: `./gradlew rebuildPatches`.
1. Commit modified patches.
1. Force push changes: `git push --force`.

## PR Policy

We'll accept changes that make sense. You should be able to justify their
existence, along with any maintenance costs that come with them. Using
[obfuscation helpers](#obfuscation-helpers) aids in the maintenance costs.
Remember that these changes will affect everyone who runs Purpur, not just you
and your server.

While we will fix minor formatting issues, you should stick to the guide below
when making and submitting changes.

## Formatting

All modifications to non-Purpur files should be marked.

- Multi-line changes start with `// Purpur start` and end with `// Purpur end`;
- You can put a comment with an explanation if it isn't obvious, like this:
  `// Purpur start - reason`.
    - The comments should generally be about the reason the change was made, what
      it was before, or what the change is.
    - Multi-line messages should start with `// Purpur start` and use `/* Multi
      line message here */` for the message itself.
- One-line changes should have `// Purpur` or `// Purpur - reason`.

Here's an example of how to mark changes by Purpur:

```java
entity.getWorld().dontbeStupid(); // Purpur - was beStupid() which is bad
entity.getFriends().forEach(Entity::explode);
entity.a();
entity.b();
// Purpur start - use plugin-set spawn
// entity.getWorld().explode(entity.getWorld().getSpawn());
Location spawnLocation = ((CraftWorld)entity.getWorld()).getSpawnLocation();
entity.getWorld().explode(new BlockPosition(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
// Purpur end
```

We generally follow usual Java style (aka. Oracle style), or what is programmed
into most IDEs and formatters by default. There are a few notes, however:
- It is fine to go over 80 lines as long as it doesn't hurt readability.  
  There are exceptions, especially in Spigot-related files
- When in doubt or the code around your change is in a clearly different style,
  use the same style as the surrounding code.

## Patch Notes

When submitting patches to Purpur, we may ask you to add notes to the patch
header. While we do not require it for all changes, you should add patch notes
when the changes you're making are technical, complex, or require an explanation
of some kind. It is very likely that your patch will remain long after we've all
forgotten about the details of your PR; patch notes will help us maintain it
without having to dig back through GitHub history looking for your PR.

These notes should express the intent of your patch, as well as any pertinent
technical details we should keep in mind long-term. Ultimately, they exist to
make it easier for us to maintain the patch across major version changes.

If you add a message to your commit in the `Purpur-Server`/`Purpur-API`
directories, the rebuild patches script will handle these patch notes
automatically as part of generating the patch file. If you are not
extremely careful, you should always just `squash` or `amend` a patch (see the
above sections on modifying patches) and rebuild.

Editing messages and patches by hand is possible, but you should patch and
rebuild afterwards to make sure you did it correctly. This is slower than just
modifying the patches properly after a few times, so you will not really gain
anything but headaches from doing it by hand.

Underneath is an example patch header/note:

```patch
From 02abc033533f70ef3165a97bfda3f5c2fa58633a Mon Sep 17 00:00:00 2001
From: Shane Freeder <theboyetronic@gmail.com>
Date: Sun, 15 Oct 2017 00:29:07 +0100
Subject: [PATCH] revert serverside behavior of keepalives

This patch intends to bump up the time that a client has to reply to the
server back to 30 seconds as per pre 1.12.2, which allowed clients
more than enough time to reply, potentially allowing them to be less
temperamental due to lag spikes on the network thread, e.g. that caused
by plugins that are interacting with netty.

We also add a system property to allow people to tweak how long the server
will wait for a reply. There is a compromise here between lower and higher
values, lower values will mean that dead connections can be closed sooner,
whereas higher values will make this less sensitive to issues such as spikes
from networking or during connections flood of chunk packets on slower clients,
 at the cost of dead connections being kept open for longer.

diff --git a/src/main/java/net/minecraft/server/PlayerConnection.java b/src/main/java/net/minecraft/server/PlayerConnection.java
index a92bf8967..d0ab87d0f 100644
--- a/src/main/java/net/minecraft/server/PlayerConnection.java
+++ b/src/main/java/net/minecraft/server/PlayerConnection.java
```

## Obfuscation Helpers

While rarely needed, obfuscation helpers are sometimes useful when it comes
to unmapped local variables, or poorly named method parameters. In an effort
to make future updates easier on ourselves, Purpur tries to use obfuscation
helpers wherever it makes sense. The purpose of these helpers is to make the
code more readable and maintainable. These helpers should be made easy to
inline by the JVM wherever possible.

An example of an obfuscation helper for a local variable:
```java
double d0 = entity.getX(); final double fromX = d0; // Purpur - OBFHELPER
// ...   
this.someMethod(fromX); // Purpur
```

While they may not always be done in exactly the same way, the general goal is
always to improve readability and maintainability. Use your best judgment and do
what fits best in your situation.

## Configuration files

To use a configurable value in your patch, add a new entry in either the
`PurpurConfig` or `PurpurWorldConfig` classes. Use `PurpurConfig` if a value
must remain the same throughout all worlds, or the latter if it can change
between worlds. World-specific configuration options are preferred whenever
possible.

### PurpurConfig example

```java
public static boolean saveEmptyScoreboardTeams = false;
private static void saveEmptyScoreboardTeams() {
    // This is called automatically!
    // The name also doesn't matter.
    saveEmptyScoreboardTeams = getBoolean("settings.save-empty-scoreboard-teams", false);
}
```

Notice that the field is always public, but the setter is always private. This
is important to the way the configuration generation system works. To access
this value, reference it as you would any other static value:

```java
if (!PurpurConfig.saveEmptyScoreboardTeams) {
```

It is often preferred that you use the fully qualified name for the
configuration class when accessing it, like so:
`org.purpurmc.purpur.PurpurConfig.valueHere`.  
If this is not done, a developer for Purpur might fix that for you before
merging, but it's always nice if you make it a habit where you only need 1-2
lines changed.

### PurpurWorldConfig example

```java
public boolean useInhabitedTime = true;
private void useInhabitedTime() {
    // This is called automatically!
    // The name also doesn't matter.
    useInhabitedTime = getBoolean("use-chunk-inhabited-timer", true);
}
```

Again, notice that the field is always public, but the setter is always private.
To access this value, you'll need an instance of the `net.minecraft.world.level.Level`
object:

```java
return this.level.purpurConfig.useInhabitedTime ? this.inhabitedTime : 0;
```

## Testing API changes

### Using the Purpur Test Plugin

The Purpur project has a `test-plugin` module for easily testing out API changes
and additions. To use the test plugin, enable it in `test-plugin.settings.gradle.kts`,
which will be generated after running Gradle at least once. After this, you can edit
the test plugin, and run a server with the plugin using `./gradlew runDev` (or any
of the other Purpur run tasks).

### Publishing to Maven local (use in external plugins)

To build and install the Purpur APIs and Server to your local Maven repository, do the following:

- Run `./gradlew publishToMavenLocal` in the base directory.

If you use Gradle to build your plugin:
- Add `mavenLocal()` as a repository. Gradle checks repositories in the order they are declared,
  so if you also have the Purpur repository added, put the local repository above Purpur's.
- Make sure to remove `mavenLocal()` when you are done testing, see the [Gradle docs](https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:case-for-maven-local)
  for more details.

If you use Maven to build your plugin:
- If you later need to use the Purpur-API, you might want to remove the jar
  from your local Maven repository.  
  If you use Windows and don't usually build using WSL, you might not need to
  do this.

## Frequently Asked Questions

### I can't find the NMS file I need!

By default, Purpur (and upstream) only import files we make changes to. If you
would like to make changes to a file that isn't present in `Purpur-Server`'s
source directory, you just need to add it to our import script ran during the
patching process.

1. Save (rebuild) any patches you are in the middle of working on! Their
   progress will be lost if you do not;
1. Identify the name(s) of the file(s) you want to import.
    - A complete list of all possible file names can be found at
      `./Purpur-Server/.gradle/caches/paperweight/mc-dev-sources/net/minecraft/`. You might find
      [MiniMappingViewer] useful if you need to translate between Mojang and Spigot mapped names.
1. Open the file at `./build-data/dev-imports.txt` and add the name of your file to
   the script. Follow the instructions there;
1. Re-patch the server `./gradlew applyPatches`;
1. Edit away!

> ❗ This change is temporary! **DO NOT COMMIT CHANGES TO THIS FILE!**  
> Once you have made your changes to the new file, and rebuilt patches, you may
> undo your changes to `dev-imports.txt`.

Any file modified in a patch file gets automatically imported, so you only need
this temporarily to import it to create the first patch.

To undo your changes to the file, type `git checkout build-data/dev-imports.txt`.

### My commit doesn't need a build, what do I do?

Well, quite simple: You add `[ci skip]` to the start of your commit subject.

This case most often applies to changes to files like `README.md`, this very
file (`CONTRIBUTING.md`), the `LICENSE.md` file, and so forth.

### Patching and building is *really* slow, what can I do?

This only applies if you're running Windows. If you're running a prior Windows
release, either update to Windows 10 or move to macOS/Linux/BSD.

In order to speed up patching process on Windows, it's recommended you get WSL.
2. This is available in Windows 10 v2004, build 19041 or higher. (You can check
   your version by running `winver` in the run window (Windows key + R)). If you're
   out of date, update your system with the
   [Windows Update Assistant](https://www.microsoft.com/en-us/software-download/windows10).

To set up WSL 2, follow the information here:
<https://docs.microsoft.com/en-us/windows/wsl/install-win10>

You will most likely want to use the Ubuntu apps. Once it's set up, install the
required tools with `sudo apt-get update && sudo apt-get install $TOOL_NAMES
-y`. Replace `$TOOL_NAMES` with the packages found in the
[requirements](#requirements). You can now clone the repository and do
everything like usual.

> ❗ Do not use the `/mnt/` directory in WSL! Instead, mount the WSL directories
> in Windows like described here:
> <https://www.howtogeek.com/426749/how-to-access-your-linux-wsl-files-in-windows-10/>

[MiniMappingViewer]: https://minidigger.github.io/MiniMappingViewer/

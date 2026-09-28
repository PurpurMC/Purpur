Contributing to Purpur
==========================
Purpur is happy you're willing to contribute to our projects. We are usually
very lenient with all submitted PRs, but there are still some guidelines you
can follow to make the approval process go more smoothly.

## Use a Personal Fork and not an Organization

Purpur will routinely modify your PR, whether it's a quick rebase or to take care
of any minor nitpicks we might have. Often, it's better for us to solve these
problems for you than make you go back and forth trying to fix them yourself.

Unfortunately, if you use an organization for your PR, it prevents Purpur from
modifying it. To avoid this, please do not use repositories on organizations
for PRs.

## Requirements

To get started with making changes, you'll need the following software, most of
which can be obtained in (most) package managers such as `apt` (Debian / Ubuntu;
you will most likely use this for WSL), `homebrew` (macOS / Linux), and more:

- `git` (package `git` everywhere);
- A Java 21 or later JDK (packages vary, use Google/DuckDuckGo/etc.).
    - [Adoptium](https://adoptium.net/) has builds for most operating systems.
    - Purpur requires JDK 21 to build, however, makes use of Gradle's
      [Toolchains](https://docs.gradle.org/current/userguide/toolchains.html)
      feature to allow building with only JRE 11 or later installed. (Gradle will
      automatically provision JDK 21 for compilation if it cannot find an existing
      install).

If you're on Windows, check
[the section on WSL](#patching-and-building-is-really-slow-what-can-i-do).

If you're compiling with Docker, you can use Adoptium's
[`eclipse-temurin`](https://hub.docker.com/_/eclipse-temurin/) images like so:

```console
# docker run -it -v "$(pwd)":/data --rm eclipse-temurin:21.0.5_11-jdk bash
Pulling image...

root@abcdefg1234:/# javac -version
javac 21.0.5
```

## Understanding Patches

Unlike the Purpur API and its implementation, modifications to Paper and Minecraft source files
are done through patches. These patches/extensions are split into three different sets of two
categories, which are formatted like so:

Under `purpur-api`:

- `paper-patches` (applies to the `paper-server/` git repo)
    - `sources`: Per-file patches to Paper API classes;
    - `features`: Larger feature patches that modify multiple Paper API classes.

Under `purpur-server`:

- `minecraft-patches` (applies to the `purpur-server//` git repo)
  - `sources`: Per-file patches to Minecraft classes;
  - `resources`: Per-file patches to Minecraft data files;
  - `features`: Larger feature patches that modify multiple classes.
- `paper-patches`
  - `sources`: Per-file patches to Paper Server classes;
  - `features`: Larger feature patches that modify multiple Paper Server classes.

Because this entire structure is based on patches and git, a basic understanding
of how to use git is required. A basic tutorial can be found here:
<https://git-scm.com/docs/gittutorial>.

Assuming you have already forked the repository:

1. Clone your fork to your local machine;
2. Type `./gradlew applyAllPatches` in a terminal to apply the patches to both paper and minecraft classes.
   On Windows, remove `./` from the beginning of `gradlew` commands;
3. cd into `purpur-server` for server changes, `purpur-api` for API changes,
   `paper-api` for Paper API changes, and `paper-server` for Paper Server changes.

`purpur-server/src/minecraft/java` and `purpur-server/src/minecraft/java/resources` are not git repositories in the traditional sense.
Its initial commits are the decompiled and deobfuscated Minecraft source and resource files. The per-file
patches are applied on top of these files as a single, large commit, which is then followed
by the individual feature-patch commits. `paper-api/` and `paper-server/`
follow the same concept; each paper "project" has its own git repository that also includes it's own feature and per-file patches.

## Understanding the Gradle Tasks

It can be overwhelming when you look over all the available gradle tasks, but it's pretty straightforward.
Each "project" that includes a local git repository has the following available gradle tasks attached to it:

- `./gradlew apply[project]FeaturePatches` - Applies [project] feature patches
- `./gradlew apply[project]FilePatches` - Applies [project] file patches
- `./gradlew apply[project]Patches` - Applies all [project] patches
- `./gradlew fixup[project]FilePatches` - Puts the currently tracked source changes into the [project] file patches commit
- `./gradlew rebuild[project]FeaturePatches` - Rebuilds [project] feature patches
- `./gradlew rebuild[project]FilePatches` - Rebuilds [project] file patches
- `./gradlew rebuild[project]Patches` - Rebuilds all [project] patches

Some additional useful tasks are listed below:

- `./gradlew applyAllPatches` - Applies all patches defined in the paperweight-patcher project and the server project. (equivalent to running `applyPaperPatches` and then `applyAllServerPatches` in a second Gradle invocation)
- `./gradlew applyAllServerFeaturePatches` - Applies all Minecraft and upstream server feature patches (equivalent to `applyMinecraftFeaturePatches applyServerFeaturePatches`)
- `./gradlew applyAllServerFilePatches` - Applies all Minecraft and upstream server file patches (equivalent to `applyMinecraftFilePatches applyServerFilePatches`)
- `./gradlew applyAllServerPatches` - Applies all Minecraft and upstream server patches (equivalent to `applyMinecraftPatches applyServerPatches`)

- `./gradlew rebuildPaperSingleFilePatches` - Fixups and rebuilds all paper single-file patches. This is how you'd make changes to the `build.gradle.kts` files located under `purpur-api` and `purpur-server`

## Modifying (per-file) patches

This is generally what you need to do when editing files. Updating our
per-file patches is as easy as:
1. Making your changes;
2. Running `./gradlew fixup[project]FilePatches` in the root directory;
3. If nothing went wrong, rebuilding patches with `./gradlew rebuild[project]FilePatches`;

### Resolving rebase conflicts
If you run into conflicts while running `fixup[project]FilePatches`, you need to go a more
manual route:

This method works by temporarily resetting your `HEAD` to the desired commit to
edit it using `git rebase`.

0. If you have changes you are working on, type `git stash` to store them for
   later;
    - You can type `git stash pop` to get them back at any point.
1. cd into the relevant local git repo and run `git rebase -i base`;
    - It should show something like
      [this](https://gist.github.com/zachbr/21e92993cb99f62ffd7905d7b02f3159) in
      the text editor you get.
    - If your editor does not have a "menu" at the bottom, you're using `vim`.  
      If you don't know how to use `vim` and don't want to
      learn, enter `:q!` and press enter. Before redoing this step, do
      `export EDITOR=nano` for an easier editor to use.
1. Replace `pick` with `edit` for the commit/patch you want to modify (in this
   case the very first commit, `purpur File Patches`), and
   "save" the changes;
1. Make the changes you want to make to the patch;
1. Run `git add .` to add your changes;
1. Run `git commit --amend` to commit;
1. Run `git rebase --continue` to finish rebasing;
1. Run `./gradlew rebuild[project]FilePatches` in the root directory;

## Adding larger feature patches

Feature patches are exclusively used for large-scale changes that are hard to
track and maintain and that can be optionally dropped, such as the more involved
optimizations we have. This makes it easier to update Purpur during Minecraft updates,
since we can temporarily drop these patches and reapply them later.

There is only a very small chance that you will have to use this system, but adding
such patches is very simple:

1. Modify the relevant local git repo with the appropriate changes;
1. Run `git add .` inside that directory to add your changes;
1. Run `git commit` with the desired patch message;
1. Run `./gradlew rebuild[project]FeaturePatches` in the root directory.

Your commit will be converted into a patch that you can then PR into Purpur.

> ❗ Please note that if you have some specific implementation detail you'd like
> to document, you should do so in the patch message *or* in comments.

## Modifying larger feature patches

One way of modifying feature patches is to reset to the patch commit and follow
the instructions from the [rebase section](#resolving-rebase-conflicts). If you
are sure there won't be any conflicts from later patches, you can also use the
fixup method.

### Fixup method

#### Manual method

1. Make your changes;
1. Make a temporary commit. You don't need to make a message for this;
1. Type `git rebase -i base`, move (cut) your temporary commit and
   move it under the line of the patch you wish to modify;
1. Change the `pick` to the appropriate action:
    1. `f`/`fixup`: Merge your changes into the patch without touching the
       message.
    1. `s`/`squash`: Merge your changes into the patch and use your commit message
       and subject.
1. Run `./gradlew rebuild[project]Patches` in the root directory;
    - This will modify the appropriate patches based on your commits.

#### Automatic method

1. Make your changes;
1. Make a fixup commit: `git commit -a --fixup <hash of patch to fix>`;
    - If you want to modify a per-file patch, use `git commit -a --fixup file`
    - You can also use `--squash` instead of `--fixup` if you want the commit
      message to also be changed.
    - You can get the hash by looking at `git log` or `git blame`; your IDE can
      assist you too.
    - Alternatively, if you only know the name of the patch, you can do
      `git commit -a --fixup "Subject of Patch name"`.
1. Rebase with autosquash: `git rebase -i --autosquash base`.
   This will automatically move your fixup commit to the right place, and you just
   need to "save" the changes.
1. Run `./gradlew rebuild[project]Patches` in the root directory. This will modify the
   appropriate patches based on your commits.

## Rebasing PRs

Steps to rebase a PR to include the latest changes from `main`.  
These steps assume the `origin` remote is your fork of this repository and `upstream` is the official PurpurMC repository.

1. Fetch the latest changes from upstream's main: `git fetch upstream`.
1. Checkout your feature/fix branch and rebase on main: `git switch patch-branch && git rebase upstream/main`.
1. Apply updated patches: `./gradlew applyPatches`.
1. If there are conflicts, fix them.
1. If your PR creates new feature patches instead of modifying existing ones, ensure your newly-created patch is the last commit by either:
    * Renaming the patch file with a large 4-digit number in front (e.g. 9999-Patch-to-add-some-new-stuff.patch), and re-applying patches.
    * Running `git rebase --interactive base` and moving the commits to the end.
1. Rebuild patches: `./gradlew rebuildPatches`.
1. Commit modified patches.
1. Force push changes: `git push --force`. Make sure you're not deleting any of your commits or changes here!

## PR Policy

We'll accept changes that make sense. You should be able to justify their
existence, along with any maintenance costs that come with them. Using
[obfuscation helpers](#obfuscation-helpers) aids in the maintenance costs.
Remember that these changes will affect everyone who runs Purpur, not just you
and your server.

While we will fix minor formatting issues, you should stick to the guide below
when making and submitting changes.

## Formatting

All modifications to Minecraft files and Paper files should be marked. For historical reasons,
API and API-implementation contain a lot of these too, but they are no longer
required.

- You need to add a comment with a short and identifiable description of the patch:
  `// Purpur start - <COMMIT DESCRIPTION>`
    - The comments should generally be about the reason the change was made, what
      it was before, or what the change is.
    - After the general commit description, you can add additional information either
      after a `;` or in the next line.
- Multi-line changes start with `// Purpur start - <COMMIT DESCRIPTION>` and end
  with `// Purpur end - <COMMIT DESCRIPTION>`.
- One-line changes should have `// Purpur - <COMMIT DESCRIPTION>` at the end of the line.

> [!NOTE]
> These comments are incredibly important to be able to keep track of changes
> across files and to remember what they are for, even a decade into the future.

Here's an example of how to mark changes by Purpur:

```java
entity.getWorld().dontBeStupid(); // Purpur - Move away from beStupid()
entity.getFriends().forEach(Entity::explode);
entity.updateFriends();

// Purpur start - Use plugin-set spawn
// entity.getWorld().explode(entity.getWorld().getSpawn());
Location spawnLocation = ((CraftWorld) entity.getWorld()).getSpawnLocation();
entity.getWorld().explode(new BlockPosition(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
// Purpur end - Use plugin-set spawn
```

We generally follow the usual Java style (aka. Oracle style), or what is programmed
into most IDEs and formatters by default. There are a few notes, however:
- It is fine to go over 80 lines as long as it doesn't hurt readability.  
  There are exceptions, especially in Spigot-related files
- When in doubt or the code around your change is in a clearly different style,
  use the same style as the surrounding code.
- Usage of the `var` keyword is discouraged, as it makes reading patch files a
  lot harder and can lead to confusion during updates due to changed return types.
  The only exception to this is if a line would otherwise be way too long/filled with
  hard to parse generics in a case where the base type itself is already obvious.

### Imports
When adding new imports to a Minecraft or Paper class, use the fully qualified class name
instead of adding a new import to the top of the file. If you are using a type a significant number of times, you
can add an import with a comment. However, if it's only used a couple of times, the FQN is preferred to prevent future
patch conflicts in the import section of the file.


```java
import net.minecraft.server.MinecraftServer;
// don't add import here, use FQN like below

public class SomeVanillaClass {
    public final org.bukkit.Location newLocation; // Purpur - add location
}
```

### Nullability annotations

We are in the process of switching nullability annotation libraries, so you might need to use one or the other:

**For classes we add**: Fields, method parameters and return types that are nullable should be marked via the
`@Nullable` annotation from `org.jspecify.annotations`. Whenever you create a new class, add `@NullMarked`, meaning types
are assumed to be non-null by default. For less obvious placing such as on generics or arrays, see the [JSpecify docs](https://jspecify.dev/docs/user-guide/).

**For other classes**: Keep using both `@Nullable` and `@NotNull` from `org.jetbrains.annotations`. These
will be replaced later.

### API checks

When performing API-related checks where an exception needs to be thrown under specific conditions, you should use the `Preconditions` class.

#### Checking Method Arguments
To validate method arguments, use `Preconditions#checkArgument`. This will throw an `IllegalArgumentException` if the condition is not met.
> Don't use Preconditions#checkNotNull, as it throws a NullPointerException, which makes it harder to determine whether the error was caused by an internal issue or invalid arguments.

ex:
```java
@Override
public void sendMessage(Player player, Component message) {
    Preconditions.checkArgument(player != null, "player cannot be null");
    Preconditions.checkArgument(player.isOnline(), "player %s must be online", player.getName());
    Preconditions.checkArgument(message != null, "message cannot be null");
    // rest of code
}
```

#### Checking Object State
To validate the state of an object inside a method, use `Preconditions#checkState`. This will throw an `IllegalStateException` if the condition is not met.
ex:
```java
private Player player;

@Override
public void sendMessage(Component message) {
    Preconditions.checkArgument(message != null, "message cannot be null");
    Preconditions.checkState(this.player != null, "player cannot be null");
    Preconditions.checkState(this.player.isOnline(), "player %s must be online", this.player.getName());
    // rest of code
}
```

## Access Transformers
Sometimes, Vanilla code already contains a field, method, or type you want to access
but the visibility is too low (e.g. a private field in an entity class). Purpur can use access transformers
to change the visibility or remove the final modifier from fields, methods, and classes. Inside the `build-data/purpur.at`
file, you can add ATs that are applied when you `./gradlew applyPatches`. You can read about the format of ATs
[here](https://mcforge.readthedocs.io/en/latest/advanced/accesstransformers/#access-modifiers).

<!--
## Patch Notes

When submitting feature patches to Purpur, we may ask you to add notes to the patch
header. While we do not require it for all changes, you should add patch notes
when the changes you're making are technical, complex, or require an explanation
of some kind. It is very likely that your patch will remain long after we've all
forgotten about the details of your PR; patch notes will help us maintain it
without having to dig back through GitHub history looking for your PR.

These notes should express the intent of your patch, as well as any pertinent
technical details we should keep in mind long-term. Ultimately, they exist to
make it easier for us to maintain the patch across major version changes.

If you add a message to your commit in the Minecraft source directory,
the rebuild patches script will handle these patch notes
automatically as part of generating the patch file. If you are not
extremely careful, you should always just `squash` or `amend` a patch (see the
above sections on modifying patches) and rebuild.

Editing messages and patches by hand is possible, but you should patch and
rebuild afterwards to make sure you did it correctly. This is slower than just
modifying the patches properly after a few times, so you will not really gain
anything but headaches from doing it by hand.

Underneath is an example patch header/note:

```patch
From 0000000000000000000000000000000000000000 Mon Sep 17 00:00:00 2001
From: Shane Freeder <theboyetronic@gmail.com>
Date: Sun, 15 Oct 2017 00:29:07 +0100
Subject: [PATCH] revert serverside behavior of keepalives

This patch intends to bump up the time that a client has to reply to the
server back to 30 seconds as per pre 1.12.2, which allowed clients
more than enough time to reply potentially allowing them to be less
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
-->

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

## Tips and Tricks

### IntelliJ IDEA

- Under `Settings > Appearance & Behavior > System Settings`, disable
  `Sync external changes: Periodically when the IDE is inactive (experimental)`.
  When disabled, the IDE will not attempt to reindex files while patches are applying
  unless you interact with the IDE during the process. This avoids severe slowdowns and freezes.
- Under `Settings > Appearance & Behavior > System Settings`, you may also want to
  disable `Reopen projects on startup` to avoid freeze loops.

## Frequently Asked Questions

### My commit doesn't need a build, what do I do?

Quite simple: You add `[ci skip]` to the start of your commit subject.

This case most often applies to changes to files like `README.md`, this very
file (`CONTRIBUTING.md`), the `LICENSE.md` file, and so forth.

### Patching and building is *really* slow, what can I do?

This only applies if you're running Windows. If you're running a prior Windows
release, either update to Windows 10/11 or move to macOS/Linux/BSD.

In order to speed up patching process on Windows, it's recommended you get WSL 2.
This is available in Windows 10 v2004, build 19041 or higher. (You can check
your version by running `winver` in the run window (Windows key + R)). If you're
using an out of date version of Windows 10, update your system with the
[Windows 10 Update Assistant](https://www.microsoft.com/en-us/software-download/windows10) or [Windows 11 Update Assistant](https://www.microsoft.com/en-us/software-download/windows11).

To set up WSL 2, follow the information here:
<https://docs.microsoft.com/en-us/windows/wsl/install>

You will most likely want to use the Ubuntu apps. Once it's set up, install the
required tools with `sudo apt-get update && sudo apt-get install $TOOL_NAMES
-y`. Replace `$TOOL_NAMES` with the packages found in the
[requirements](#requirements). You can now clone the repository and do
everything like usual.

> ❗ Do not use the `/mnt/` directory in WSL! Instead, mount the WSL directories
> in Windows like described here:
> <https://docs.microsoft.com/en-us/windows/wsl/filesystems#view-your-current-directory-in-windows-file-explorer>

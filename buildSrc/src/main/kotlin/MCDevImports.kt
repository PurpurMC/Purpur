/**
 * This is the set of extra NMS files which will be imported as part of the patch process
 *
 * See `./Paper/work/Minecraft/$MCVER/net/minecraft/server` for a list of possible files
 *
 * The `.java` extension is always assumed and should be excluded
 *
 * NOTE: Do not commit changes to this set! Instead make changes, rebuild patches, and commit the modified patches.
 *       Files already modified in existing patches will be imported automatically.
 */
val nmsImports = setOf<String>(
    // ex:
    //"EntityZombieVillager"
)

data class LibraryImport(val group: String, val library: String, val prefix: String, val file: String)

/**
 * This is the set of extra files to import into the server workspace from libraries
 *
 * Changes to this set should be committed to the repo, as these won't be automatically imported.
 */
val libraryImports = setOf<LibraryImport>(
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier", "CommandDispatcher"),
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier/tree", "LiteralCommandNode"),
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier/suggestion", "SuggestionsBuilder"),
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier/arguments", "BoolArgumentType")
)

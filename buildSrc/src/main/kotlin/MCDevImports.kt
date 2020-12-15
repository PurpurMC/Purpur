val nmsImports = setOf<String>(

)

data class LibraryImport(val group: String, val library: String, val prefix: String, val file: String)

val libraryImports = setOf<LibraryImport>(
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier", "CommandDispatcher"),
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier/tree", "LiteralCommandNode"),
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier/suggestion", "SuggestionsBuilder"),
    LibraryImport("com.mojang", "brigadier", "com/mojang/brigadier/arguments", "BoolArgumentType")
)

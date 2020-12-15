import java.io.File
import java.nio.file.Path

data class ToothpickSubproject(val baseDir: File, val projectDir: File, val patchesDir: File) {
    val basePath: Path
        get() = baseDir.toPath()
    val projectPath: Path
        get() = projectDir.toPath()
    val patchesPath: Path
        get() = patchesDir.toPath()
}

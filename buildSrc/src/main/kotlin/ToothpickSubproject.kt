import org.gradle.api.Project
import java.io.File

class ToothpickSubproject {
    lateinit var project: Project

    val baseDir: File by lazy {
        val name = project.name
        val upstream = project.toothpick.upstream
        val suffix = if (name.endsWith("server")) "Server" else "API"
        project.upstreamDir.resolve("$upstream-$suffix")
    }
    val projectDir: File
        get() = project.projectDir
    lateinit var patchesDir: File

    operator fun component1(): File = baseDir
    operator fun component2(): File = projectDir
    operator fun component3(): File = patchesDir
}

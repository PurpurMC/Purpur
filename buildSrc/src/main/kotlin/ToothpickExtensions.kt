import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import java.io.File
import java.nio.file.Path

val Project.toothpick: ToothpickExtension
    get() = rootProject.extensions.findByType(ToothpickExtension::class)!!

fun Project.toothpick(receiver: ToothpickExtension.() -> Unit) {
    toothpick.project = this
    receiver(toothpick)
    allprojects {
        group = toothpick.groupId
        version = "${toothpick.minecraftVersion}-${toothpick.nmsRevision}"
    }
    configureSubprojects()
    initToothpickTasks()
}

val Project.lastUpstream: File
    get() = rootProject.projectDir.resolve("last-${toothpick.upstreamLowercase}")

val Project.rootProjectDir: File
    get() = rootProject.projectDir

val Project.upstreamDir: File
    get() = rootProject.projectDir.resolve(toothpick.upstream)

val Project.projectPath: Path
    get() = projectDir.toPath()

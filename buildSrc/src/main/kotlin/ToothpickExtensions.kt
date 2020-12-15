import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import java.nio.file.Path

val Project.toothpick: ToothpickExtension
    get() = rootProject.extensions.findByType(ToothpickExtension::class)!!

fun Project.toothpick(receiver: ToothpickExtension.() -> Unit) {
    receiver(toothpick)
    allprojects {
        group = toothpick.groupId
        version = "${toothpick.minecraftVersion}-${toothpick.nmsRevision}"
    }
}

val Project.projectPath: Path
    get() = projectDir.toPath()

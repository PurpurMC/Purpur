import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class Toothpick : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<ToothpickExtension>("toothpick", project.objects)
    }
}

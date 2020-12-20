import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import java.io.File
import java.util.Locale

@Suppress("UNUSED_PARAMETER")
open class ToothpickExtension(objects: ObjectFactory) {
    lateinit var project: Project
    lateinit var forkName: String
    val forkNameLowercase
        get() = forkName.toLowerCase(Locale.ENGLISH)
    lateinit var forkUrl: String
    lateinit var forkVersion: String
    lateinit var groupId: String
    lateinit var minecraftVersion: String
    lateinit var nmsRevision: String
    lateinit var nmsPackage: String

    lateinit var upstream: String
    val upstreamLowercase
        get() = upstream.toLowerCase(Locale.ENGLISH)
    lateinit var upstreamBranch: String

    var paperclipName: String = ""
        get(): String = if (field.isEmpty()) {
            "$forkNameLowercase-paperclip.jar"
        } else "$field.jar"

    lateinit var serverProject: ToothpickSubproject
    fun server(receiver: ToothpickSubproject.() -> Unit) {
        serverProject = ToothpickSubproject()
        receiver(serverProject)
    }

    lateinit var apiProject: ToothpickSubproject
    fun api(receiver: ToothpickSubproject.() -> Unit) {
        apiProject = ToothpickSubproject()
        receiver(apiProject)
    }

    val subprojects: Map<String, ToothpickSubproject>
        get() = if (::forkName.isInitialized) mapOf(
            "$forkName-API" to apiProject,
            "$forkName-Server" to serverProject
        ) else emptyMap()

    val paperDir: File by lazy {
        if (upstream == "Paper") {
            project.upstreamDir
        } else {
            project.upstreamDir.walk().find {
                it.name == "Paper" && it.isDirectory
                        && it.resolve("work/Minecraft/${minecraftVersion}").exists()
            } ?: error("Failed to find Paper directory!")
        }
    }

    val paperWorkDir: File
        get() = paperDir.resolve("work/Minecraft/${minecraftVersion}")
}

import org.gradle.api.model.ObjectFactory
import java.util.Locale

@Suppress("UNUSED_PARAMETER")
open class ToothpickExtension(objects: ObjectFactory) {
    lateinit var minecraftVersion: String
    lateinit var nmsRevision: String
    lateinit var forkName: String
    val forkNameLowercase
        get() = forkName.toLowerCase(Locale.ENGLISH)
    lateinit var forkVersion: String
    lateinit var groupId: String
    lateinit var serverProject: ToothpickSubproject
    lateinit var apiProject: ToothpickSubproject
    val subprojects: Map<String, ToothpickSubproject>
        get() = mapOf(
            "$forkName-API" to apiProject,
            "$forkName-Server" to serverProject
        )
}

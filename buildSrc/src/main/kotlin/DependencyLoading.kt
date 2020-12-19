import kotlinx.dom.elements
import kotlinx.dom.parseXml
import kotlinx.dom.search
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project

fun RepositoryHandler.loadRepositories(project: Project) {
    val pomFile = project.projectDir.resolve("pom.xml")
    if (!pomFile.exists()) return
    val dom = parseXml(pomFile)
    val repositoriesBlock = dom.search("repositories").firstOrNull() ?: return

    // Load repositories
    repositoriesBlock.elements("repository").forEach { repositoryElem ->
        val url = repositoryElem.search("url").firstOrNull()?.textContent ?: return@forEach
        maven(url)
    }
}

fun DependencyHandlerScope.loadDependencies(project: Project) {
    val pomFile = project.projectDir.resolve("pom.xml")
    if (!pomFile.exists()) return
    val dom = parseXml(pomFile)

    val dependenciesBlock = dom.search("dependencies").firstOrNull() ?: return

    // Load dependencies
    dependenciesBlock.elements("dependency").forEach { dependencyElem ->
        val groupId = dependencyElem.search("groupId").first().textContent
        val artifactId = dependencyElem.search("artifactId").first().textContent
        val version = dependencyElem.search("version").first().textContent.applyReplacements(
            mapOf(
                "project.version" to "${project.toothpick.minecraftVersion}-${project.toothpick.nmsRevision}",
                "minecraft.version" to project.toothpick.minecraftVersion
            )
        )
        val scope = dependencyElem.search("scope").firstOrNull()?.textContent
        val classifier = dependencyElem.search("classifier").firstOrNull()?.textContent

        val dependencyString = "${groupId}:${artifactId}:${version}${classifier?.run { ":$this" } ?: ""}"
        project.logger.debug("Read dependency '{}' from '{}'", dependencyString, pomFile.absolutePath)

        // Special case API
        if (artifactId == "${project.toothpick.forkNameLowercase}-api"
            || artifactId == "${project.toothpick.upstreamLowercase}-api"
        ) {
            if (project.name.endsWith("-server")) {
                add("api", project(":${project.toothpick.forkNameLowercase}-api"))
            }
            return@forEach
        }

        when (scope) {
            "compile", null -> add("api", dependencyString)
            "provided" -> {
                add("compileOnly", dependencyString)
                add("testImplementation", dependencyString)
            }
            "runtime" -> add("runtimeOnly", dependencyString)
            "test" -> add("testImplementation", dependencyString)
        }
    }
}

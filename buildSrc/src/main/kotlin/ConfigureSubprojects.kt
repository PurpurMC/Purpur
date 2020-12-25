import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import kotlinx.dom.elements
import kotlinx.dom.parseXml
import kotlinx.dom.search
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.withType
import java.nio.charset.StandardCharsets.UTF_8
import java.text.SimpleDateFormat
import java.util.Date

internal fun Project.configureSubprojects() {
    subprojects {
        apply<JavaLibraryPlugin>()
        apply<MavenPublishPlugin>()

        tasks.withType<JavaCompile> {
            options.encoding = UTF_8.name()
        }
        tasks.withType<Javadoc> {
            options.encoding = UTF_8.name()
        }

        extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    groupId = rootProject.group as String
                    version = rootProject.version as String
                    pom {
                        name.set(project.name)
                        url.set(toothpick.forkUrl)
                    }
                }
            }
        }

        when {
            project.name.endsWith("server") -> configureServerProject()
            project.name.endsWith("api") -> configureApiProject()
        }
    }
}

private fun Project.configureServerProject() {
    apply<ShadowPlugin>()

    val generatePomFileForMavenJavaPublication by tasks.getting(GenerateMavenPom::class) {
        destination = project.buildDir.resolve("tmp/pom.xml")
    }

    @Suppress("UNUSED_VARIABLE")
    val test by tasks.getting(Test::class) {
        // didn't bother to look into why these fail. paper excludes them in paperweight as well though
        exclude("org/bukkit/craftbukkit/inventory/ItemStack*Test.class")
    }

    val shadowJar by tasks.getting(ShadowJar::class) {
        archiveClassifier.set("") // ShadowJar is the main server artifact
        dependsOn(generatePomFileForMavenJavaPublication)
        transform(Log4j2PluginsCacheFileTransformer::class.java)
        mergeServiceFiles()
        manifest {
            attributes(
                "Main-Class" to "org.bukkit.craftbukkit.Main",
                "Implementation-Title" to "CraftBukkit",
                "Implementation-Version" to toothpick.forkVersion,
                "Implementation-Vendor" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Date()),
                "Specification-Title" to "Bukkit",
                "Specification-Version" to "${project.version}",
                "Specification-Vendor" to "Bukkit Team"
            )
        }
        from(project.buildDir.resolve("tmp/pom.xml")) {
            // dirty hack to make "java -Dpaperclip.install=true -jar paperclip.jar" work without forking paperclip
            into("META-INF/maven/io.papermc.paper/paper")
        }

        // Don't like to do this but sadly have to do this for compatibility reasons
        relocate("org.bukkit.craftbukkit", "org.bukkit.craftbukkit.v${toothpick.nmsPackage}") {
            exclude("org.bukkit.craftbukkit.Main*")
        }
        relocate("net.minecraft.server", "net.minecraft.server.v${toothpick.nmsPackage}")

        // Make sure we relocate deps the same as Paper et al.
        val pomFile = project.projectDir.resolve("pom.xml")
        if (!pomFile.exists()) return@getting
        val dom = parseXml(pomFile)
        val buildSection = dom.search("build").first()
        val plugins = buildSection.search("plugins").first()
        plugins.elements("plugin").filter {
            val artifactId = it.search("artifactId").first().textContent
            artifactId == "maven-shade-plugin"
        }.forEach {
            it.search("executions").first()
                .search("execution").first()
                .search("configuration").first()
                .search("relocations").first()
                .elements("relocation").forEach { relocation ->
                    val pattern = relocation.search("pattern").first().textContent
                    val shadedPattern = relocation.search("shadedPattern").first().textContent
                    if (pattern != "org.bukkit.craftbukkit" && pattern != "net.minecraft.server") { // We handle these ourselves above
                        logger.debug("Imported relocation to server project shadowJar from ${pomFile.absolutePath}: $pattern to $shadedPattern")
                        relocate(pattern, shadedPattern)
                    }
                }
        }
    }

    tasks.getByName("build") {
        dependsOn(shadowJar)
    }

    extensions.configure<PublishingExtension> {
        publications {
            getByName<MavenPublication>("mavenJava") {
                artifactId = rootProject.name
                artifact(tasks["shadowJar"])
            }
        }
    }
}

@Suppress("UNUSED_VARIABLE")
private fun Project.configureApiProject() {
    val jar by this.tasks.getting(Jar::class) {
        doFirst {
            buildDir.resolve("tmp/pom.properties")
                .writeText("version=${project.version}")
        }
        from(buildDir.resolve("tmp/pom.properties")) {
            into("META-INF/maven/${project.group}/${project.name}")
        }
        manifest {
            attributes("Automatic-Module-Name" to "org.bukkit")
        }
    }

    extensions.configure<PublishingExtension> {
        publications {
            getByName<MavenPublication>("mavenJava") {
                artifactId = project.name
                from(components["java"])
            }
        }
    }
}

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting

internal fun Project.configureSubprojects() {
    subprojects {
        apply<JavaLibraryPlugin>()
        apply<MavenPublishPlugin>()

        afterEvaluate {
            extensions.configure(PublishingExtension::class.java) {
                publications {
                    create<MavenPublication>("mavenJava") {
                        artifactId = if (project.name.endsWith("server")) rootProject.name else project.name
                        groupId = rootProject.group as String
                        version = rootProject.version as String
                        from(components["java"])
                        pom {
                            name.set(project.name)
                            url.set("https://github.com/pl3xgaming/Purpur")
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
        dependsOn(generatePomFileForMavenJavaPublication)
        transform(Log4j2PluginsCacheFileTransformer::class.java)
        manifest {
            attributes(
                "Main-Class" to "org.bukkit.craftbukkit.Main",
                "Implementation-Title" to "CraftBukkit",
                "Implementation-Version" to toothpick.forkVersion,
                "Implementation-Vendor" to java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .format(java.util.Date()),
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
        val relocVersion = toothpick.minecraftVersion.replace(".", "_")
        relocate("org.bukkit.craftbukkit", "org.bukkit.craftbukkit.v$relocVersion") {
            exclude("org.bukkit.craftbukkit.Main*")
        }
        relocate("net.minecraft.server", "net.minecraft.server.v$relocVersion")
    }
    tasks.getByName("build") {
        dependsOn(shadowJar)
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
}

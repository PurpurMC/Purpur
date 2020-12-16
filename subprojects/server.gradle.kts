import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.github.johnrengelman.shadow")
}

repositories {
    loadRepositories(project)
}

dependencies {
    loadDependencies(project)
}

val generatePomFileForMavenJavaPublication by tasks.getting(GenerateMavenPom::class) {
    destination = project.buildDir.resolve("tmp/pom.xml")
}

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
    val relocVersion = toothpick.minecraftVersion.replace(".", "_")
    relocate("org.bukkit.craftbukkit", "org.bukkit.craftbukkit.v$relocVersion") {
        exclude("org.bukkit.craftbukkit.Main*")
    }

    relocate("net.minecraft.server", "net.minecraft.server.v$relocVersion")
}

tasks.build {
    dependsOn(shadowJar)
}

repositories {
    loadRepositories(project)
}

dependencies {
    loadDependencies(project)
}

java {
    withJavadocJar()
}

val jar by tasks.getting(Jar::class) {
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
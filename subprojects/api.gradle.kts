import xyz.jpenilla.toothpick.loadDependencies
import xyz.jpenilla.toothpick.loadRepositories

repositories {
    loadRepositories(project)
}

dependencies {
    loadDependencies(project)
}

java {
    withJavadocJar()
}

import xyz.jpenilla.toothpick.loadDependencies
import xyz.jpenilla.toothpick.loadRepositories

repositories {
    loadRepositories(project)
}

dependencies {
    loadDependencies(project)
    implementation("cat.inspiracio", "rhino-js-engine", "1.7.7.1")
}

val kotlinxDomVersion = "0.0.10"
val shadowVersion = "6.1.0"

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx.dom:$kotlinxDomVersion")
    implementation("com.github.jengelman.gradle.plugins:shadow:$shadowVersion")
}

gradlePlugin {
    plugins {
        register("Toothpick") {
            id = "toothpick"
            implementationClass = "Toothpick"
        }
    }
}

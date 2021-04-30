repositories {
    // Airplane libs...
    maven("https://jitpack.io") {
        content { includeGroupByRegex("com\\.github\\..*") }
    }
    maven("https://notom3ga.jfrog.io/artifactory/repo") {
        content { includeGroup("me.notom3ga") }
    }
}

dependencies {
    implementation("cat.inspiracio", "rhino-js-engine", "1.7.7.1")
}

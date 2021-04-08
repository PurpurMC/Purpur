repositories {
    // Airplane libs...
    maven("https://jitpack.io") {
        content { includeGroupByRegex("com\\.github\\..*") }
    }
}

dependencies {
    implementation("cat.inspiracio", "rhino-js-engine", "1.7.7.1")
}

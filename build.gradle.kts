plugins {
    id("io.papermc.paperweight.patcher") version "2.0.0-SNAPSHOT"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.10.4:fat")
    decompiler("org.vineflower:vineflower:1.10.1")
    paperclip("io.papermc:paperclip:3.0.3")
}

paperweight {
    serverProject.set(project(":hikari-server"))

    remapRepo.set("https://repo.maven.apache.org/maven2/")
    decompileRepo.set("https://repo.maven.apache.org/maven2/")

    usePaperUpstream(providers.gradleProperty("paperRef")) {
        withPaperPatcher {
            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
        }
    }
}

allprojects {
    apply(plugin = "java")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

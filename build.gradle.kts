plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "ntdotjsx.jumpscare"
version = "1.0-SNAPSHOT"
description = "Jumpscare plugin for Minecraft 1.21"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
    maven("https://artifactory.papermc.io/artifactory/universe/") {
        name = "papermc"
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("Jumpscare-${project.version}-by-ntdotjsx.jar")
    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }
}
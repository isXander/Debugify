plugins {
    java
    id("fabric-loom") version "0.11.+"
}

group = "dev.isxander"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val minecraftVersion: String by project
    val yarnVersion: String by project
    val loaderVersion: String by project

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnVersion:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version
                )
            )
        }
    }
}

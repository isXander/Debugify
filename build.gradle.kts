plugins {
    val kotlinVersion: String by System.getProperties()

    java
    kotlin("jvm") version kotlinVersion
    id("fabric-loom") version "0.11.+"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val kotlinVersion: String by System.getProperties()
    val minecraftVersion: String by project
    val yarnVersion: String by project
    val loaderVersion: String by project
    val fabricVersion: String by project
    val fabricKotlinVersion: String by project

    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnVersion:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion+kotlin.$kotlinVersion")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
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

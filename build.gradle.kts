plugins {
    id("architectury-plugin") version "3.4.+"
    id("dev.architectury.loom") version "0.11.0.+" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.+" apply false
}

architectury {
    val minecraftVersion: String by rootProject
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "io.github.juuxel.loom-quiltflower")

    dependencies {
        val minecraftVersion: String by project
        val yarnVersion: String by project

        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"("net.fabricmc:yarn:$yarnVersion:v2")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")

    group = "cc.woverflow"
    version = "1.0"

    repositories {
        mavenCentral()
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(17)
        }
    }
}

dependencies {
    val minecraftVersion: String by project
    val yarnVersion: String by project
    val loaderVersion: String by project

//    minecraft("com.mojang:minecraft:$minecraftVersion")
//    mappings("net.fabricmc:yarn:$yarnVersion:v2")
//    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
}

//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(17))
//    }
//}

tasks {
//    processResources {
//        inputs.property("version", project.version)
//        filesMatching("fabric.mod.json") {
//            expand(
//                mutableMapOf(
//                    "version" to project.version
//                )
//            )
//        }
//    }
}

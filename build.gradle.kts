plugins {
    id("architectury-plugin") version "3.4.+"
    id("dev.architectury.loom") version "0.11.0.+" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.+" apply false

    id("com.modrinth.minotaur") version "2.+" apply false
    id("com.matthewprenger.cursegradle") version "1.4.+" apply false
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
    version = "1.2.0"

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

tasks {
    val publishToModrinth by registering
    val publishToCurseforge by registering
    register("publish") {
        dependsOn("clean")
        dependsOn(publishToModrinth)
        dependsOn(publishToCurseforge)
    }
}

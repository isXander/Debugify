plugins {
    id("architectury-plugin") version "3.4.+"
    id("io.github.p03w.machete") version "1.+" apply false
    `mod-release`
    `bug-status-checker`
}

architectury {
    val minecraftVersion: String by rootProject
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "mc-setup")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "io.github.p03w.machete")

    group = "dev.isxander"
    version = "1.11.0"

    extra.set("changelog", rootProject.file("changelogs/${project.version}.md").takeIf { it.exists() }?.readText())

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.shedaniel.me")
        maven("https://maven.terraformersmc.com")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(17)
        }
    }
}

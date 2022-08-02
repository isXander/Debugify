plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net")
    maven("https://maven.architectury.dev")
    maven("https://maven.minecraftforge.net")
    maven("https://server.bbkr.space/artifactory/libs-release")
    maven("https://maven.quiltmc.org/repository/release")
}

dependencies {
    fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
}

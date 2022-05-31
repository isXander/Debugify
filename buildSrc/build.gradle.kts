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

    implementation(plugin("dev.architectury.loom", "0.12.+"))
    implementation(plugin("io.github.juuxel.loom-quiltflower", "1.7.1"))
    implementation(plugin("org.quiltmc.quilt-mappings-on-loom", "4.+")) {
        exclude(module = "fabric-loom")
    }

    implementation(plugin("com.modrinth.minotaur", "2.+"))
    implementation(plugin("com.matthewprenger.cursegradle", "1.+"))
    implementation(plugin("com.github.breadmoirai.github-release", "2.+"))
    implementation("com.google.code.gson:gson:2.9.0")
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://server.bbkr.space/artifactory/libs-release")
    }
}

rootProject.name = "Debugify"

include("common")
include("fabric")
include("forge")

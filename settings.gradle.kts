pluginManagement {
	includeBuild("build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.isxander.dev/releases")
    }
}

rootProject.name = "debugify"


plugins {
    id("com.github.johnrengelman.shadow") version "7.+"
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    forge.apply {
        mixinConfig("debugify-common.mixins.json")
        convertAccessWideners.set(true)
    }
}

val common by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentForge"].extendsFrom(this)
}
val shadowCommon by configurations.creating

dependencies {
    val minecraftVersion: String by rootProject
    val forgeVersion: String by rootProject

    forge("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("META-INF/mods.toml") {
            expand(
                "version" to project.version
            )
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set(null as String?)
    }

    jar {
        archiveClassifier.set("dev")
    }
}

components["java"].withGroovyBuilder {
    "withVariantsFromConfiguration"(configurations["shadowRuntimeElements"]) {
        "skip"()
    }
}

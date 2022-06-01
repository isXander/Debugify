plugins {
    id("com.github.johnrengelman.shadow") version "7.+"
    `platform-publishing`
}

base.archivesName.set("debugify")

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    forge.apply {
        mixinConfig("debugify-common.mixins.json")
        mixinConfig("debugify.mixins.json")
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
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
    val clothVersion: String by rootProject
    val jSemVerVersion: String by rootProject
    val mixinExtrasVersion: String by rootProject

    forge("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }


    "com.github.zafarkhaja:java-semver:$jSemVerVersion".let {
        implementation(it)
        forgeRuntimeLibrary(it)
        shadowCommon(it)
    }

    "com.github.llamalad7:mixinextras:$mixinExtrasVersion".let {
        forgeRuntimeLibrary(it)
        implementation(it)
        annotationProcessor(it)
        shadowCommon(it)
    }

    modImplementation("me.shedaniel.cloth:cloth-config-forge:$clothVersion")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("description", modDescription)
        filesMatching("META-INF/mods.toml") {
            expand(
                "version" to project.version,
                "description" to modDescription,
            )
        }
    }

    shadowJar {
        relocate("com.llamalad7.mixinextras", "dev.isxander.debugify.lib.mixinextras")
        relocate("com.github.zafarkhaja.semver", "dev.isxander.debugify.lib.jsemver")

        exclude("fabric.mod.json")
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        val minecraftVersion: String by rootProject
        archiveClassifier.set("${project.name}-$minecraftVersion")
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

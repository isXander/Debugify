plugins {
    id("com.github.johnrengelman.shadow") version "7.+"
    `platform-publishing`
}

base.archivesName.set("debugify")

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val common by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentFabric"].extendsFrom(this)
}
val shadowCommon by configurations.creating

dependencies {
    val fabricLoaderVersion: String by rootProject
    val clothVersion: String by rootProject
    val jSemVerVersion: String by rootProject
    val mixinExtrasVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }

    "com.github.zafarkhaja:java-semver:$jSemVerVersion".let {
        implementation(it)
        shadowCommon(it)
    }

    "com.github.llamalad7:mixinextras:$mixinExtrasVersion".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:$clothVersion") {
        exclude(module = "fabric-api")
    }

    modImplementation("com.terraformersmc:modmenu:3.+")
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

    shadowJar {
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        injectAccessWidener.set(true)
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

quiltflower {
    addToRuntimeClasspath.set(true)
}

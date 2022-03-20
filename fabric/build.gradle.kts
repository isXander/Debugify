plugins {
    id("com.github.johnrengelman.shadow") version "7.+"
    id("com.modrinth.minotaur")
    id("com.matthewprenger.cursegradle")
}

base.archivesName.set("debugify-${project.name}")

architectury {
    platformSetupLoomIde()
    fabric()
}

val common by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentFabric"].extendsFrom(this)
}
val shadowCommon by configurations.creating

dependencies {
    val fabricLoaderVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }

    implementation("com.github.zafarkhaja:java-semver:0.9.+")
    shadowCommon("com.github.zafarkhaja:java-semver:0.9.+")

    "com.github.llamalad7:mixinextras:0.0.+".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }
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

val minecraftVersion: String by rootProject

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("QwxR6Gcd")
    versionName.set("[${project.name.capitalize()} $minecraftVersion] ${project.version}")
    versionNumber.set("${project.version}-${project.name}")
    versionType.set("release")
    uploadFile.set(tasks.remapJar.get())
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(listOf(project.name))
}

rootProject.tasks["publishToModrinth"].dependsOn(tasks["modrinth"])

if (hasProperty("curseforge.token")) {
    curseforge {
        apiKey = findProperty("curseforge.token")
        project(closureOf<com.matthewprenger.cursegradle.CurseProject> {
            mainArtifact(tasks.remapJar.get(), closureOf<com.matthewprenger.cursegradle.CurseArtifact> {
                displayName = "[${project.name.capitalize()} $minecraftVersion] ${project.version}"
            })

            id = "596224"
            releaseType = "beta"
            addGameVersion(minecraftVersion)
            addGameVersion(project.name.capitalize())
            addGameVersion("Java 17")
        })

        options(closureOf<com.matthewprenger.cursegradle.Options> {
            forgeGradleIntegration = false
        })
    }
}

rootProject.tasks["publishToCurseforge"].dependsOn(tasks["curseforge"])

import ru.vyarus.gradle.plugin.python.PythonExtension
import ru.vyarus.gradle.plugin.python.task.PythonTask

plugins {
    java

    id("fabric-loom") version "0.12.+"
    id("io.github.juuxel.loom-quiltflower") version "1.7.+"

    id("com.modrinth.minotaur") version "2.4.+"
    id("me.hypherionmc.cursegradle") version "2.+"
    id("com.github.breadmoirai.github-release") version "2.+"
    id("io.github.p03w.machete") version "1.+"
    `maven-publish`

    id("ru.vyarus.use-python") version "2.3.+"
}

group = "dev.isxander"
version = "2.4.0"

loom {
    splitEnvironmentSourceSets()
    mods {
        register("debugify") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }

    accessWidenerPath.set(file("src/main/resources/debugify.accesswidener"))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com")
}

val minecraftVersion: String by rootProject
val fabricLoaderVersion: String by rootProject
val fabricApiVersion: String by rootProject
val clothVersion: String by rootProject
val mixinExtrasVersion: String by rootProject
val modMenuVersion: String by  rootProject

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    "com.github.llamalad7:mixinextras:$mixinExtrasVersion".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:$clothVersion") {
        exclude(module = "fabric-api")
    }

    modImplementation("com.terraformersmc:modmenu:$modMenuVersion") {
        exclude(module = "fabric-api")
    }

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    modImplementation(fabricApi.module("fabric-resource-loader-v0", "$fabricApiVersion"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

java {
    withSourcesJar()
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("description", modDescription)
    filesMatching(listOf("fabric.mod.json", "quilt.mod.json")) {
        expand(
            "version" to project.version,
            "description" to modDescription,
        )
    }
}

python {
    pip("requests:2.28.1")

    scope = PythonExtension.Scope.USER
}

tasks.register<PythonTask>("checkBugStatuses") {
    group = "debugify"

    command = rootProject.file("scripts/check_bug_fixes.py").toString()
}

val changelogText = rootProject.file("changelogs/${project.version}.md").takeIf { it.exists() }?.readText() ?: "No changelog is provided"

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("debugify")
    versionName.set("${project.version} ($minecraftVersion)")
    versionNumber.set("${project.version}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(listOf("fabric", "quilt"))
    changelog.set(changelogText)
    dependencies {
        optional.project("modmenu")
        optional.project("cloth-config")
    }
    syncBodyFrom.set(rootProject.file("README.md").readText())
}

if (hasProperty("curseforge.token")) {
    curseforge {
        apiKey = findProperty("curseforge.token")
        project(closureOf<me.hypherionmc.cursegradle.CurseProject> {
            mainArtifact(tasks["remapJar"], closureOf<me.hypherionmc.cursegradle.CurseArtifact> {
                displayName = "${project.version} ($minecraftVersion)"
            })

            id = "596224"
            releaseType = "release"
            addGameVersion(minecraftVersion)
            addGameVersion("Fabric")
            addGameVersion("Quilt")
            addGameVersion("Java 17")

            relations(closureOf<me.hypherionmc.cursegradle.CurseRelation> {
                requiredDependency("cloth-config")
                optionalDependency("modmenu")
            })

            changelog = changelogText
            changelogType = "markdown"
        })

        options(closureOf<me.hypherionmc.cursegradle.Options> {
            forgeGradleIntegration = false
        })
    }
}

publishing {
    publications {
        create<MavenPublication>("debugify") {
            groupId = "dev.isxander"
            artifactId = "debugify"

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("xander-repo.username") && hasProperty("xander-repo.password")) {
            maven(url = "https://maven.isxander.dev/releases") {
                credentials {
                    username = property("xander-repo.username")?.toString()
                    password = property("xander-repo.password")?.toString()
                }
            }
        }
    }
}

tasks.register("publishDebugify") {
    group = "debugify"

    dependsOn("checkBugStatuses")

    dependsOn("clean")

    dependsOn("modrinth")
    dependsOn("modrinthSyncBody")

    dependsOn("curseforge")

    dependsOn("githubRelease")

    dependsOn("publish")
}


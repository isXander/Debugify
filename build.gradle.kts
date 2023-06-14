import ru.vyarus.gradle.plugin.python.PythonExtension
import ru.vyarus.gradle.plugin.python.task.PythonTask

plugins {
    java

    id("fabric-loom") version "1.2.+"
    id("io.github.juuxel.loom-quiltflower") version "1.8.+"

    id("com.modrinth.minotaur") version "2.7.+"
    id("me.hypherionmc.cursegradle") version "2.+"
    id("com.github.breadmoirai.github-release") version "2.+"
    `maven-publish`

    id("io.github.p03w.machete") version "2.+"
    id("org.ajoberstar.grgit") version "5.0.0"

    id("ru.vyarus.use-python") version "3.0.0"
}

group = "dev.isxander"
version = "1.20.1+1.1"

loom {
    splitEnvironmentSourceSets()
    mods {
        register("debugify") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }
}

val gametest by sourceSets.registering {
    compileClasspath += sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
    compileClasspath += sourceSets["client"].compileClasspath
    runtimeClasspath += sourceSets["client"].runtimeClasspath
}

loom {
    runs {
        register("gametest") {
            client()
            ideConfigGenerated(true)
            name("Game Test")
            source(gametest.get())
        }

        listOf(named("client"), named("server")).forEach {
            it {
                vmArg("-Ddebugify.forceMacFixes=true")
                vmArg("-Ddebugify.forceLinuxFixes=true")
                vmArg("-Ddebugify.forceWindowsFixes=true")
            }
        }
    }
    createRemapConfigurations(gametest.get())

    accessWidenerPath.set(file("src/main/resources/debugify.accesswidener"))
}

machete {
    json.enabled.set(false)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.isxander.dev/snapshots")
    maven("https://maven.terraformersmc.com")
    maven("https://maven.quiltmc.org/repository/release")
}

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val qmBuild: String by project
val fabricApiVersion: String by project
val yaclVersion: String by project
val mixinExtrasVersion: String by project
val modMenuVersion: String by project

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        if (qmBuild != "0")
            mappings("org.quiltmc:quilt-mappings:$minecraftVersion+build.$qmBuild:intermediary-v2")
        officialMojangMappings()
    })

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    "com.github.llamalad7.mixinextras:mixinextras-fabric:$mixinExtrasVersion".let {
        implementation(it)
        annotationProcessor(it)
        "clientAnnotationProcessor"(it)
        include(it)
    }

    modImplementation(fabricApi.module("fabric-resource-loader-v0", fabricApiVersion))
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    "modClientImplementation"("dev.isxander.yacl:yet-another-config-lib-fabric:$yaclVersion") { exclude(module = "fabric-loader") }
    "modClientImplementation"("com.terraformersmc:modmenu:$modMenuVersion") { exclude(module = "fabric-loader") }

    "gametestImplementation"(sourceSets.main.get().output)
    "gametestImplementation"(sourceSets["client"].output)
    "modGametestImplementation"(fabricApi.module("fabric-gametest-api-v1", fabricApiVersion))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

java {
    withSourcesJar()
}

tasks.processResources {
    val modDescription = """
        Fixes Minecraft bugs found on the bug tracker

        License stuff:
        j-Tai's TieFix - Code used licensed under LGPLv3
        FlashyReese's Sodium Extra - Code used licensed under LGPLv3
        """.trimIndent()
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
    pip("packaging:21.3")

    scope = PythonExtension.Scope.USER
}

tasks.register<PythonTask>("checkBugStatuses") {
    group = "debugify"

    command = project.file("scripts/check_bug_fixes.py").toString()
}

var changelogText = file("changelogs/$minecraftVersion/${project.version}.md").takeIf { it.exists() }?.readText()
    ?: "No changelog is provided"
file("changelogs/header.md").takeIf { it.exists() }?.readText()?.let { changelogText = it + "\n\n" + changelogText }

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
        required.project("yacl")
        optional.project("modmenu")
    }
    syncBodyFrom.set(project.file("README.md").readText())
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
                requiredDependency("yacl")
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
            artifact(tasks["remapSourcesJar"])
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
        } else {
            println("Xander Maven credentials not satisfied")
        }
    }
}

githubRelease {
    token(findProperty("github.token")?.toString())

    owner("isXander")
    repo("Debugify")
    tagName("${project.version}")
    targetCommitish(grgit.branch.current().name)
    body(changelogText)
    releaseAssets(tasks["remapJar"].outputs.files)
}

tasks["githubRelease"].dependsOn("optimizeOutputsOfRemapJar")
tasks["modrinth"].dependsOn("optimizeOutputsOfRemapJar")
tasks["generateMetadataFileForDebugifyPublication"].dependsOn("optimizeOutputsOfRemapJar")

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


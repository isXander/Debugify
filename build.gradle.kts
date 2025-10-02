plugins {
    id("dev.isxander.modstitch.base") version "0.7.1-unstable"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
    `maven-publish`
    id("org.ajoberstar.grgit") version "5.3.2"
}

modstitch {
    minecraftVersion = "1.21.9"
    modLoaderVersion = "0.17.2"

    parchment {
        mappingsVersion = "2025.09.14"
        minecraftVersion = "1.21.8"
    }

    metadata {
        modVersion = minecraftVersion.map { "$it+1.0" }
        modId = "debugify"
        modName = "Debugify"
        modDescription = "Fixes Minecraft bugs found on the bug tracker"
        modCredits = """
            j-Tai's TieFix - Code used licensed under LGPLv3
            FlashyReese's Sodium Extra - Code used licensed under LGPLv3
            Ampflower's 2x2 Surrounded Saplings Fix - Code used licensed under Zlib
            NoahvdAa's Thorium - Code used licensed under LGPLv3
            Moulberry's MoulberryTweaks - Code used licensed under MIT
        """.trimIndent()
    }

    mixin {
        addMixinsToModManifest = true
        configs.register("debugify")
        configs.register("debugify.client") { side = CLIENT }
    }

    loom {
        configureLoom {
            mixin.useLegacyMixinAp = false

            splitEnvironmentSourceSets()

            val gametest by sourceSets.registering {
                compileClasspath += sourceSets.main.get().compileClasspath
                runtimeClasspath += sourceSets.main.get().runtimeClasspath
                compileClasspath += sourceSets["client"].compileClasspath
                runtimeClasspath += sourceSets["client"].runtimeClasspath
            }

            //createProxyConfigurations(sourceSets["client"])
            createProxyConfigurations(gametest.get())

            mods.register("debugify") {
                sourceSet(sourceSets["main"])
                sourceSet(sourceSets["client"])
            }

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
        }
    }
}

repositories {
    exclusiveContent {
        forRepository { maven("https://maven.terraformersmc.com/releases") }
        filter { includeGroup("com.terraformersmc") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.isxander.dev/releases") }
        filter {
            includeGroup("dev.isxander")
            includeGroup("org.quiltmc.parsers")
        }
    }
}

val fabricApiVersion: String by project
val yaclVersion: String by project
val mixinExtrasVersion: String by project
val modMenuVersion: String by project

dependencies {
    modImplementation(fabricApi.module("fabric-resource-loader-v0", fabricApiVersion))
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    "modClientImplementation"("dev.isxander:yet-another-config-lib:$yaclVersion")
    "modClientImplementation"("com.terraformersmc:modmenu:$modMenuVersion")

    "gametestImplementation"(sourceSets.main.get().output)
    "gametestImplementation"(sourceSets["client"].output)
    "modGametestImplementation"(fabricApi.module("fabric-gametest-api-v1", fabricApiVersion))
}

java {
    withSourcesJar()
}

publishMods {
    displayName.set("Debugify ${project.version}")

    file.set(tasks.remapJar.get().archiveFile)

    changelog.set(modstitch.minecraftVersion.zip(modstitch.metadata.modVersion) { mcVersion, modVersion ->
        val header = file("changelogs/header.md")
            .takeIf { it.exists() }
            ?.readText()

        file("changelogs/$mcVersion/$modVersion.md")
            .takeIf { it.exists() }
            ?.readText()
            ?.let { if (header != null) "$header\n\n$it" else it }
    })

    type.set(STABLE)
    modLoaders.add("fabric")

    val modrinthId: String by project
    if (modrinthId.isNotBlank() && hasProperty("modrinth.token")) {
        modrinth {
            projectId.set(modrinthId)
            accessToken.set(findProperty("modrinth.token")?.toString())
            minecraftVersions.add(modstitch.minecraftVersion)

            requires { slug.set("yacl") }
            requires { slug.set("fabric-api") }
            optional { slug.set("modmenu") }
        }
    }

    val curseforgeId: String by project
    if (curseforgeId.isNotBlank() && hasProperty("curseforge.token")) {
        curseforge {
            projectId.set(curseforgeId)
            accessToken.set(findProperty("curseforge.token")?.toString())
            minecraftVersions.add(modstitch.minecraftVersion)

            requires { slug.set("yacl") }
            requires { slug.set("fabric-api") }
            optional { slug.set("modmenu") }
        }
    }

    val githubProject: String by project
    if (githubProject.isNotBlank() && hasProperty("github.token")) {
        github {
            repository.set(githubProject)
            accessToken.set(findProperty("github.token")?.toString())
            commitish.set(grgit.branch.current().name)
        }
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
        if (hasProperty("XANDER_MAVEN_USER") && hasProperty("XANDER_MAVEN_PASS")) {
            maven(url = "https://maven.isxander.dev/releases") {
                credentials {
                    username = property("XANDER_MAVEN_USER")?.toString()
                    password = property("XANDER_MAVEN_PASS")?.toString()
                }
            }
        } else {
            println("Xander Maven credentials not satisfied")
        }
    }
}

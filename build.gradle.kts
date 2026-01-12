import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("dev.isxander.modstitch.base") version "0.8.4"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"

    `maven-publish`
    signing
    id("dev.isxander.secrets") version "0.1.0"
    id("org.ajoberstar.grgit") version "5.3.2"
    id("com.gradleup.nmcp.aggregation") version "1.4.3"
    id("com.gradleup.nmcp") version "1.4.3"
}

val debugifyVersion = "1.0"

modstitch {
    minecraftVersion = "1.21.11"
    modLoaderVersion = "0.18.4"

    parchment {
        mappingsVersion = "2025.12.20"
    }

    metadata {
        modVersion = minecraftVersion.map { "$it+$debugifyVersion" }
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

    nmcpAggregation(project)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    isFailOnError = false
}

publishMods {
    displayName = modstitch.metadata.modVersion.map { "Debugify $it" }
    version = modstitch.metadata.modVersion
    file = tasks.remapJar.get().archiveFile
    type = STABLE
    modLoaders.add("fabric")

    changelog = modstitch.minecraftVersion.zip(modstitch.metadata.modVersion) { mcVersion, modVersion ->
        val header = file("changelogs/header.md")
            .takeIf { it.exists() }
            ?.readText()

        file("changelogs/$mcVersion/$modVersion.md")
            .takeIf { it.exists() }
            ?.readText()
            ?.let { if (header != null) "$header\n\n$it" else it }
    }

    modrinth {
        projectId = providers.gradleProperty("pub.modrinthId")
        accessToken = secrets.gradleProperty("modrinth.accessToken")

        minecraftVersions.add(modstitch.minecraftVersion)

        requires { slug.set("yacl") }
        requires { slug.set("fabric-api") }
        optional { slug.set("modmenu") }
    }

    curseforge {
        projectId = providers.gradleProperty("pub.curseforgeId")
        projectSlug = providers.gradleProperty("pub.curseforgeSlug")
        accessToken = secrets.gradleProperty("curseforge.accessToken")

        minecraftVersions.add(modstitch.minecraftVersion)

        requires { slug.set("yacl") }
        requires { slug.set("fabric-api") }
        optional { slug.set("modmenu") }
    }

    val githubProject: String by project
    if (githubProject.isNotBlank() && hasProperty("github.token")) {

    }
    github {
        repository = providers.gradleProperty("githubProject")
        accessToken = secrets.gradleProperty("github.accessToken")

        commitish = grgit.branch.current().name
    }
}

publishing {
    publications {
        create<MavenPublication>("mod") {
            from(components["java"])

            groupId = "dev.isxander"
            artifactId = "debugify"
            version = modstitch.metadata.modVersion.get()

            pom {
                name = modstitch.metadata.modName
                description = modstitch.metadata.modDescription
                url = "https://www.isxander.dev/projects/debugify"
                licenses {
                    license {
                        name = "LGPL-3.0-or-later"
                        url = "https://www.gnu.org/licenses/lgpl-3.0.en.html"
                    }
                }
                developers {
                    developer {
                        id = "isXander"
                        name = "Xander"
                        email = "business@isxander.dev"
                    }
                }
                scm {
                    url = "https://github.com/isXander/Debugify"
                    connection = "scm:git:git//github.com/isXander/Debugify.git"
                    developerConnection = "scm:git:ssh://git@github.com/isXander/Debugify.git"
                }
            }
        }
    }
}

val signingKeyProvider = secrets.gradleProperty("signing.secretKey")
val signingPasswordProvider = secrets.gradleProperty("signing.password")
signing {
    sign(publishing.publications["mod"])
}
// not configuration cache friendly, but neither is the whole of signing plugin
// this plugin does not support lazy configuration of signing keys
gradle.taskGraph.whenReady {
    val willSign = allTasks.any { it.name.startsWith("sign") }
    if (willSign) {
        signing {
            val signingKey = signingKeyProvider.orNull
            val signingPassword = signingPasswordProvider.orNull

            isRequired = signingKey != null && signingPassword != null
            if (isRequired) {
                useInMemoryPgpKeys(signingKey, signingPassword)
            } else {
                logger.error("Signing keys not found; skipping signing!")
            }
        }
    }
}

nmcpAggregation {
    centralPortal {
        username = secrets.gradleProperty("mcentral.username")
        password = secrets.gradleProperty("mcentral.password")

        publicationName = "debugify:$version"
    }
}


val generatePatchedTable by tasks.registering {
    val inputFile = rootProject.file(".bugs")
    inputs.file(inputFile)

    val outputFile = rootProject.file("PATCHED.md")
    outputs.file(outputFile)

    group = "debugify-utils"

    doLast {
        val inputContents = inputFile.readText()
        val entries = parsePatchedFile(inputContents)

        val patched = entries.filterIsInstance<PatchedFileEntry.Patched>()
        val clientPatched = patched.filter { it.env == PatchedFileEntry.Patched.Environment.Client }
        val serverPatched = patched.filter { it.env == PatchedFileEntry.Patched.Environment.Server }
        fun generatePatchedRows(entries: List<PatchedFileEntry.Patched>) =
            entries.joinToString("\n") { "- | ${it.type.friendlyName} | [${it.bugId}](https://mojira.dev/${it.bugId}) | ${getBugDescription(it.bugId)} |" }

        val previous = entries.filterIsInstance<PatchedFileEntry.Previous>()

        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

        val markdownTable = """
            - <!--
            - !!!! DO NOT UPDATE THIS FILE MANUALLY !!!!
            - Generated by `./gradlew generatePatchedTable`
            - Sourced from `${inputFile.name}`
            - Generated $timestamp
            - -->
            -
            - # List of Patched Bugs
            - ## Unpatched in vanilla
            - ### Client side
            - | Type | Bug ID | Name |
            - |------|--------|------|
            ${generatePatchedRows(clientPatched)}

            - ### Server side (both)
            - | Type | Bug ID | Name |
            - |------|--------|------|
            ${generatePatchedRows(serverPatched)}

            - ## Previously patched
            - Bugs that this mod has patched in the past, but has since been fixed by a vanilla update.

            - | Bug ID | Name | Fixed Version |
            - |--------|------|---------------|
            ${previous.joinToString("\n") { "- | [${it.bugId}](https://mojira.dev/${it.bugId}) | ${getBugDescription(it.bugId)} | ${it.patchVersion} |" }}
        """.trimMargin("-")

        outputFile.parentFile.mkdirs()
        outputFile.writeText(markdownTable)
    }
}

data class MojiraBug(
    val key: String,
    val summary: String,
    val status: String,
    val confirmation_status: String,
    val resolution: String,
    val fix_versions: List<String>
) {
    companion object {
        fun fetch(bugId: String): MojiraBug? {
            try {
                val url = "https://mojira.dev/api/v1/issues/$bugId"
                val response = `java.net`.URI(url).toURL().readText()
                return try {
                    com.google.gson.Gson().fromJson(response, MojiraBug::class.java)
                } catch (e: com.google.gson.JsonSyntaxException) {
                    throw IllegalStateException("Failed to parse Mojira response <$url>: \"$response\"", e)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}

sealed class PatchedFileEntry(val bugNumber: Int) {
    val bugId = "MC-$bugNumber"

    class Patched(bugNumber: Int, val env: Environment, val type: Type) : PatchedFileEntry(bugNumber) {
        enum class Environment(val friendlyName: String) {
            Client("Client"), Server("Server");

            companion object {
                fun from(string: String): Environment? =
                    when (string) {
                        "client" -> Client
                        "server" -> Server
                        else -> null
                    }
            }
        }
        enum class Type(val friendlyName: String) {
            Basic("Basic"), Gameplay("Gameplay");

            companion object {
                fun from(string: String): Type? =
                    when (string) {
                        "basic" -> Basic
                        "gameplay" -> Gameplay
                        else -> null
                    }
            }
        }
    }

    class Previous(bugNumber: Int, val patchVersion: String) : PatchedFileEntry(bugNumber)
}

fun parsePatchedFile(contents: String): List<PatchedFileEntry> {
    return contents.lineSequence()
        .map { it.trim() }
        .filter { it.isNotBlank() && !it.startsWith("#") }
        .map { it.lowercase() }
        .map { it.split(" ") }
        .filter { it.size >= 2 }
        .map { words ->
            val command = words[0]
            val bugNumber = words[1].toInt()
            return@map when (command) {
                "patched" -> {
                    if (words.size != 4) throw IllegalArgumentException("patched command must have exactly 4 words: got ${words.joinToString(" ")}")
                    val env = PatchedFileEntry.Patched.Environment.from(words[2])!!
                    val type = PatchedFileEntry.Patched.Type.from(words[3])!!
                    PatchedFileEntry.Patched(bugNumber, env, type)
                }
                "previous" -> {
                    if (words.size != 3) throw IllegalArgumentException("previous command must have exactly 3 words: got ${words.joinToString(" ")}")
                    val fixVersion = words[2]
                    PatchedFileEntry.Previous(bugNumber, fixVersion)
                }
                else -> throw IllegalArgumentException("Unknown command: $command")
            }
        }
        .toList()
        .sortedBy { it.bugNumber }
}
fun getBugDescription(bugId: String): String {
    val mojiraBug = MojiraBug.fetch(bugId)
    return mojiraBug?.summary ?: ""
}


import dev.isxander.debugify.GeneratePatchedTableTask
import dev.isxander.debugify.PatchedFileEntry

plugins {
    `java-library`

    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.modstitch.manifests)
    alias(libs.plugins.modstitch.modrepos)

    `maven-publish`
    signing
    alias(libs.plugins.mod.publish.plugin)
    alias(libs.plugins.central.portal.publishing)

    alias(libs.plugins.spotless)

	id("dev.isxander.debugify.bugs")
}

val minecraftVersion = libs.versions.minecraft.get()

group = "dev.isxander"
version = "26.2.0.0"
base.archivesName = "debugify"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }

	withSourcesJar()
	withJavadocJar()
}

loom {
	splitEnvironmentSourceSets()

	accessWidenerPath = layout.projectDirectory.file("src/main/resources/debugify.ct")

	mods.register("debugify") {
		sourceSet(sourceSets.getByName("main"))
		sourceSet(sourceSets.getByName("client"))
	}

	runs.configureEach {
		preferGradleTask = true // IDEA runs are not compatible with config cache
		// ensure that all fix applications are tested, regardless of their efficacy on the dev system
		jvmArguments.add("-Ddebugify.forceMacFixes=true")
		jvmArguments.add("-Ddebugify.forceLinuxFixes=true")
		jvmArguments.add("-Ddebugify.forceWindowsFixes=true")
	}
}

repositories {
    mavenCentral()
    isxander()
    terraformersMC()
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)

    api(libs.fabric.api)
    "clientImplementation"(libs.yet.another.config.lib)
    "clientImplementation"(libs.mod.menu)
}

val bugsList = bugs.parseBugs(providers.fileContents(rootProject.layout.projectDirectory.file(".bugs")).asText)

val minecraftVersionRange = "[26.2]"
val supportedMinecraftVersions = manifests.minecraftReleasesMatching(minecraftVersionRange)

val modManifest = manifests.fabricModJson {
	modId = providers.gradleProperty("mod.id")
	version = project.version.toString()
	displayName = providers.gradleProperty("mod.name")
	description = providers.gradleProperty("mod.description").map {
		it + """
			
			Credits:
			j-Tai's TieFix - Code used licensed under LGPLv3
            FlashyReese's Sodium Extra - Code used licensed under LGPLv3
            Ampflower's 2x2 Surrounded Saplings Fix - Code used licensed under Zlib
            NoahvdAa's Thorium - Code used licensed under LGPLv3
            Moulberry's MoulberryTweaks - Code used licensed under MIT
		""".trimIndent()
	}
	authors.add("isXander")
	iconPath = "debugify.png"
	licenses.add(providers.gradleProperty("mod.license"))
	issueTrackerUrl = providers.gradleProperty("mod.issuesUrl")
	sourcesUrl = providers.gradleProperty("mod.sourcesUrl")
	homepage = sourcesUrl

	entrypoint("main", "dev.isxander.debugify.Debugify::onInitialize")
	entrypoint("client", "dev.isxander.debugify.client.DebugifyClient::onInitializeClient")
	entrypoint("modmenu", "dev.isxander.debugify.client.integrations.ModMenuIntegration")

	mixin("debugify.mixins.json")
	mixin("debugify.client.mixins.json", CLIENT)

	dependency("minecraft", REQUIRED, minecraftVersionRange)
	dependency("fabricloader", REQUIRED, "[0.19,)")
	dependency("fabric-resource-loader-v0", REQUIRED, "*")
	dependency("yet_another_config_lib_v3", SUGGESTS, "*")
}
manifests.fabricModJson(sourceSets.main.get()) {
	from(modManifest)
}

val gametestManifest = manifests.fabricModJson {
    modId = "debugify_test"
    displayName = "Debugify Test"
    version = "1.0.0"

	val testedBugs = bugsList.map { list ->
		list.filterIsInstance<PatchedFileEntry.Patched>()
			.filter { it.tested }
	}

	entrypoints.addAll(
		testedBugs.map { list ->
			list.filter { it.env.isServer }
				.map { bug -> bug.bugId.replace("-", "") }
				.map { bugId -> makeEntrypoint {
					entrypoint = "fabric-gametest"
					value = "dev.isxander.debugify.test.suites.$bugId"
				} }
		}
	)

	entrypoints.addAll(
		testedBugs.map { list ->
			list.filter { it.env.isClient }
				.map { bug -> bug.bugId.replace("-", "") }
				.map { bugId -> makeEntrypoint {
					entrypoint = "fabric-client-gametest"
					value = "dev.isxander.debugify.client.test.suites.$bugId"
					environment = CLIENT
				} }
		}
	)

    entrypoint("debugify", "dev.isxander.debugify.test.DebugifyApiTest")
}

// Set up gametest
fabricApi {
    @Suppress("UnstableApiUsage")
    configureTests {
        createSourceSet = true
        modId = gametestManifest.modId
        enableGameTests = true
        enableClientGameTests = false
        eula = true
    }
}

manifests.fabricModJson(sourceSets["gametest"]) {
    from(gametestManifest)
}

// Add LICENSE to the jars
tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) {
        into("META-INF")
    }
}

// Ensure sub-par javadoc doesn't fail the build
tasks.withType<Javadoc>().configureEach {
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
	isFailOnError = false
}

publishMods {
    file = tasks.jar.flatMap { it.archiveFile }

    val projectVersion = project.version.toString()

    displayName = providers.gradleProperty("mod.name").map { "$it $projectVersion" }
    version = project.version.toString()
    modLoaders.addAll("fabric")
    type = STABLE

    changelog = providers.fileContents(rootProject.layout.projectDirectory.file("CHANGELOG.md")).asText
        .map { it.replace("{version}", projectVersion) }

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = providers.gradleProperty("modrinth.id")
        minecraftVersions.addAll(supportedMinecraftVersions)
        announcementTitle = "Download from Modrinth"
    }

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        projectId = providers.gradleProperty("curseforge.id")
        projectSlug = providers.gradleProperty("curseforge.slug")
        minecraftVersions.addAll(supportedMinecraftVersions)
        announcementTitle = "Download from Curseforge"
        server = true
        client = true
        javaVersions.add(JavaVersion.VERSION_25)
    }

    discord {
        webhookUrl = providers.environmentVariable("DISCORD_WEBHOOK_URL")
        dryRunWebhookUrl = providers.environmentVariable("DISCORD_WEBHOOK_URL_DRY_RUN")
        username = providers.gradleProperty("mod.name")
        avatarUrl = "https://raw.githubusercontent.com/isXander/Debugify/main/src/client/resources/debugify.png"
        content = changelog.zip(providers.gradleProperty("discord.ping")) { c, p -> "$c\n\n$p" }
    }
}

centralPortalPublishing.bundle("main") {
    username = providers.environmentVariable("MAVEN_CENTRAL_USERNAME")
    password = providers.environmentVariable("MAVEN_CENTRAL_PASSWORD")

    publishingType = "AUTOMATIC"
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name = modManifest.displayName
                description = modManifest.description
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

    repositories {
        // Darn you, Kotlin!
        val repos = this as ExtensionAware
        repos.extensions.getByType<dev.lukebemish.centralportalpublishing.CentralPortalRepositoryHandlerExtension>()
            .portalBundle(":", "main")
    }
}

val shouldSign = providers.environmentVariable("SIGN")
    .map { it.toBoolean() }
    .orElse(false)

signing {
    isRequired = shouldSign.get()
    useInMemoryPgpKeys(
        providers.environmentVariable("GPG_PRIVATE_KEY").orNull,
        providers.environmentVariable("GPG_PASSPHRASE").orNull,
    )
    sign(publishing.publications["mavenJava"])
}

tasks.register("publishDebugify") {
    description = "Publishes Debugify to all remotes"
    group = "publishing"

    dependsOn("publishMods")
    dependsOn("publishMavenJavaPublicationToCentralPortalMainRepository")
}

spotless {
    java {
        target("src/**/*.java")
        licenseHeaderFile(rootProject.layout.projectDirectory.file("HEADER"))

        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
        formatAnnotations()
        leadingSpacesToTabs(4)
    }
}

val generatePatchedTable = tasks.register<GeneratePatchedTableTask>("generatePatchedTable") {
	group = "debugify"
	description = "Generates PATCHED.md from .bugs"

	bugs = bugsList
	output = rootProject.layout.projectDirectory.file("PATCHED.md")
}

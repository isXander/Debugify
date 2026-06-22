package dev.isxander.debugify

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.net.URI

@DisableCachingByDefault(because = "Checks live Mojira and Minecraft version metadata")
abstract class CheckBugFixesTask : DefaultTask() {
	@get:Input
	abstract val minecraftVersion: Property<String>

	@get:Input
	abstract val bugs: ListProperty<PatchedFileEntry>

	@TaskAction
	fun checkBugFixes() {
		val versionManifest = fetchVersionManifest()
		val targetVersion = minecraftVersion.get()
		val targetVersionIndex = versionManifest.indexOfVersion(targetVersion)
			?: throw GradleException("Minecraft version $targetVersion was not found in Mojang's version manifest")

		var resolvedCount = 0
		var duplicateCount = 0

		bugs.get().filterIsInstance<PatchedFileEntry.Patched>().forEach { entry ->
			val mojiraBug = MojiraBug.fetch(entry.bugId)
			if (mojiraBug == null) {
				logger.warn("Failed to fetch ${entry.bugId}")
				return@forEach
			}

			val fixVersions = mojiraBug.fix_versions.orEmpty().map(::normaliseFixVersion)
			when (mojiraBug.resolution) {
				"Fixed", "Won't Fix", "Works As Intended" -> {
					var shouldRemove = true
					val isFutureOrUnknown = fixVersions.isEmpty() ||
							fixVersions.any { it == "Future Update" } ||
							fixVersions.all { fixVersion ->
								val fixVersionIndex = versionManifest.indexOfVersion(fixVersion)
								fixVersionIndex == null || fixVersionIndex < targetVersionIndex
							}

					if (isFutureOrUnknown) {
						shouldRemove = false
					}

					val status = buildString {
						append(mojiraBug.resolution)
						if (fixVersions.isNotEmpty()) {
							append(" in ")
							append(fixVersions.joinToString())
						}
					}

					if (shouldRemove) {
						resolvedCount++
						logger.error("${entry.bugId} (${entry.env.name.lowercase()}): $status")
					} else {
						logger.warn("${entry.bugId} (${entry.env.name.lowercase()}): $status")
					}
				}
				"Duplicate" -> {
					duplicateCount++
					logger.warn("${entry.bugId} (${entry.env.name.lowercase()}): Duplicate")
				}
				else -> logger.lifecycle("${entry.bugId} (${entry.env.name.lowercase()}): OK!")
			}
		}

		if (resolvedCount == 0 && duplicateCount == 0) {
			logger.lifecycle("")
			logger.lifecycle("Nothing to report!")
			return
		}

		logger.lifecycle("")
		if (duplicateCount > 0) {
			logger.warn("$duplicateCount ${"bug".pluralise(duplicateCount)} ${if (duplicateCount == 1) "has" else "have"} been marked as duplicate!")
		}
		if (resolvedCount > 0) {
			throw GradleException("$resolvedCount ${"bug".pluralise(resolvedCount)} ${if (resolvedCount == 1) "needs" else "need"} removing!")
		}
	}

	private fun fetchVersionManifest(): VersionManifest {
		val url = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
		return try {
			Gson().fromJson(URI(url).toURL().readText(), VersionManifest::class.java)
		} catch (e: JsonSyntaxException) {
			throw GradleException("Failed to parse Minecraft version manifest <$url>", e)
		} catch (e: Exception) {
			throw GradleException("Failed to fetch Minecraft version manifest <$url>", e)
		}
	}

	private fun VersionManifest.indexOfVersion(version: String): Int? =
		versions.indexOfFirst { it.id == version }.takeIf { it >= 0 }

	private fun normaliseFixVersion(version: String): String =
		version
			.replace(" Pre-release ", "-pre")
			.replace(" Release Candidate ", "-rc")
			.replace(" Snapshot ", "-snapshot-")
			.replace("Minecraft ", "")

	private fun String.pluralise(count: Int): String =
		if (count == 1) this else "${this}s"

	private data class VersionManifest(val versions: List<Version>)

	private data class Version(val id: String)
}

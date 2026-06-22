package dev.isxander.debugify

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import javax.inject.Inject

class BugsPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		target.extensions.create("bugs", BugsExtension::class.java, target)
	}
}

open class BugsExtension @Inject constructor(private val project: Project) {
	fun mojiraBugProvider(bugId: String): Provider<MojiraBug> {
		return project.providers.of(MojiraBugValueSource::class.java) {
			parameters.bugId.set(bugId)
		}
	}

	fun parseBugs(provider: Provider<String>): Provider<List<PatchedFileEntry>> {
		return provider.map { fileContents -> PatchedFileEntry.parseFile(fileContents) }
	}
}

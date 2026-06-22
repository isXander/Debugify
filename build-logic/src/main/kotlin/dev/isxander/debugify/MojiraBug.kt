package dev.isxander.debugify

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.net.URI

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
				val response = URI(url).toURL().readText()
				return try {
					Gson().fromJson(response, MojiraBug::class.java)
				} catch (e: JsonSyntaxException) {
					throw IllegalStateException("Failed to parse Mojira response <$url>: \"$response\"", e)
				}
			} catch (e: Exception) {
				e.printStackTrace()
				return null
			}
		}

		fun fetchDescription(bugId: String): String {
			val mojiraBug = fetch(bugId)
			return mojiraBug?.summary ?: ""
		}
	}
}

abstract class MojiraBugValueSource : ValueSource<MojiraBug, MojiraBugValueSource.Parameters> {
	interface Parameters : ValueSourceParameters {
		val bugId: Property<String>
	}

	override fun obtain(): MojiraBug? {
		val bugId = parameters.bugId.get()
		return MojiraBug.fetch(bugId)
	}
}

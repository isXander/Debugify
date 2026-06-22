package dev.isxander.debugify

import java.io.Serializable

sealed class PatchedFileEntry(val bugNumber: Int) : Serializable {
	val bugId = "MC-$bugNumber"

	class Patched(bugNumber: Int, val env: Environment, val type: Type, val tested: Boolean) : PatchedFileEntry(bugNumber) {
		enum class Environment(val friendlyName: String) {
			Client("Client"), Server("Server");

			val isClient: Boolean
				get() = this == Client
			val isServer: Boolean
				get() = this == Server

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

	companion object {
		fun parseFile(contents: String): List<PatchedFileEntry> {
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
							if (words.size !in 4..5) throw IllegalArgumentException("patched command must have exactly 4 words: got ${words.joinToString(" ")}")
							val env = PatchedFileEntry.Patched.Environment.from(words[2])!!
							val type = PatchedFileEntry.Patched.Type.from(words[3])!!
							val tested = words.getOrNull(4) == "tested"
							PatchedFileEntry.Patched(bugNumber, env, type, tested)
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
	}
}


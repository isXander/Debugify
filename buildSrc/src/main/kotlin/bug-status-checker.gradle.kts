import com.google.gson.Gson
import com.google.gson.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

tasks.register("checkBugStatuses") {
    group = "debugify"

    logger.lifecycle("\n")
    logger.error("CHECKING BUG STATUSES\n")

    val bugsToCheck = run {
        val bugs = mutableListOf<String>()

        val patchedBugsFile = rootProject.file("PATCHED.md").readLines()
        var startedUnpatched = false
        val bugRegex = Regex("\\|.+\\| \\[(MC-\\d+)]")
        for (line in patchedBugsFile) {
            if (line.startsWith("## ")) {
                if (startedUnpatched) break
                startedUnpatched = line.equals("## Unpatched in vanilla")
            }

            if (!startedUnpatched) continue

            val match = bugRegex.find(line) ?: continue
            bugs += match.destructured.component1()
        }

        bugs
    }

    val gson = Gson()
    val client = HttpClient.newHttpClient()
    var count = 0
    for (bug in bugsToCheck) {
        val request = HttpRequest.newBuilder(URI("https://bugs.mojang.com/rest/api/2/issue/$bug"))
            .build()

        val response = client.send(request, BodyHandlers.ofString())
        val json = gson.fromJson(response.body(), JsonObject::class.java)

        val fields = json.getAsJsonObject("fields")
        val resolutionId = if (fields.has("resolution") && !fields.get("resolution").isJsonNull) {
            fields.getAsJsonObject("resolution")["id"].asString.toInt()
        } else {
            -1
        }

        val resolved = resolutionId == 1
        val fixVersions = mutableListOf<String>()
        if (resolved) {
            fields.getAsJsonArray("fixVersions")?.forEach {
                fixVersions += it.asJsonObject["name"].asString
            }

            count++
        }
        logger.log(if (resolved) LogLevel.ERROR else LogLevel.LIFECYCLE, "$bug: ${if (resolved) "Resolved" else "OK!"}" + if (resolved && !fixVersions.isEmpty()) " - Fix Versions: ${fixVersions.joinToString(", ")}" else "")
    }

    if (count == 0) {
        logger.lifecycle("Nothing to report!")
    } else {
        logger.error("\n$count issue${if (count == 1) "" else "s"} need addressing!")
        throw GradleScriptException("bug statuses not ok!", RuntimeException("$count issue${if (count == 1) "" else "s"} need addressing!"))
    }
}

import com.modrinth.minotaur.dependencies.Dependency

plugins {
    id("com.modrinth.minotaur")
    id("com.matthewprenger.cursegradle")
    `maven-publish`
    base
}

val minecraftVersion: String by rootProject

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("QwxR6Gcd")
    versionName.set("[${project.name.capitalize()} $minecraftVersion] ${project.version}")
    versionNumber.set("${project.version}-${project.name}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(if (project.name == "fabric") listOf("fabric", "quilt") else listOf(project.name))
    changelog.set(extra["changelog"].toString())
    if (project.name == "fabric")
        dependencies.add(Dependency("modmenu", "optional"))
}

rootProject.tasks["publishToModrinth"].dependsOn(tasks["modrinth"])

if (hasProperty("curseforge.token")) {
    curseforge {
        apiKey = findProperty("curseforge.token")
        project(closureOf<com.matthewprenger.cursegradle.CurseProject> {
            mainArtifact(tasks["remapJar"], closureOf<com.matthewprenger.cursegradle.CurseArtifact> {
                displayName = "[${project.name.capitalize()} $minecraftVersion] ${project.version}"
            })

            id = "596224"
            releaseType = if (project.name == "fabric") "beta" else "release"
            addGameVersion(minecraftVersion)
            addGameVersion(project.name.capitalize())
            addGameVersion("Java 17")

            relations(closureOf<com.matthewprenger.cursegradle.CurseRelation> {
                requiredDependency(if (project.name == "fabric") "cloth-config" else "cloth-config-forge")
                if (project.name == "fabric")
                    optionalDependency("modmenu")
            })

            changelog = extra["changelog"]
            changelogType = "markdown"
        })

        options(closureOf<com.matthewprenger.cursegradle.Options> {
            forgeGradleIntegration = false
        })
    }
}

rootProject.tasks["publishToCurseforge"].dependsOn(tasks["curseforge"])

val publicationName = "debugify${project.name.capitalize()}"
publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = group.toString()
            artifactId = base.archivesName.get()

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("woverflow.username") && hasProperty("woverflow.token")) {
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = property("woverflow.username")?.toString()
                    password = property("woverflow.token")?.toString()
                }
            }
        }
    }
}

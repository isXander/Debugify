import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    id("com.modrinth.minotaur")
    id("com.matthewprenger.cursegradle")
    `maven-publish`
    base
}

val minecraftVersion: String by rootProject
val isFabric = project.name == "fabric"

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("QwxR6Gcd")
    versionName.set("[${project.name.capitalize()} $minecraftVersion] ${project.version}")
    versionNumber.set("${project.version}-${project.name}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(if (isFabric) listOf("fabric", "quilt") else listOf(project.name))
    changelog.set(extra["changelog"].toString())
    if (isFabric)
        dependencies.add(ModDependency("mOgUt4GM", "optional"))
    dependencies.add(ModDependency("9s6osm5g", "optional"))
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
            releaseType = if (isFabric) "release" else "beta"
            addGameVersion(minecraftVersion)
            addGameVersion(project.name.capitalize())
            if (isFabric) addGameVersion("Quilt")
            addGameVersion("Java 17")

            relations(closureOf<com.matthewprenger.cursegradle.CurseRelation> {
                requiredDependency("cloth-config")
                if (isFabric)
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

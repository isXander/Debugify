import com.google.gson.Gson
import com.google.gson.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.code.gson:gson:2.9.0")
    }
}

plugins {
    id("architectury-plugin") version "3.4.+"
    id("dev.architectury.loom") version "0.11.0.+" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.+" apply false

    id("com.modrinth.minotaur") version "2.+" apply false
    id("com.matthewprenger.cursegradle") version "1.4.+" apply false
}

architectury {
    val minecraftVersion: String by rootProject
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "io.github.juuxel.loom-quiltflower")

    dependencies {
        val minecraftVersion: String by project
        val yarnVersion: String by project

        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"("net.fabricmc:yarn:$yarnVersion:v2")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")

    group = "cc.woverflow"
    version = "1.3.0"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.shedaniel.me")
        maven("https://maven.terraformersmc.com")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(17)
        }
    }
}

tasks {
    val publishToModrinth by registering
    val publishToCurseforge by registering

    val updateApiVersion by registering {
        onlyIf { hasProperty("xander-api.username") && hasProperty("xander-api.password") }

        val gson = Gson()

        val client = HttpClient.newHttpClient()

        val loginRequest = HttpRequest.newBuilder(URI.create("https://api.isxander.dev/login")).apply {
            val json = JsonObject()
            json.addProperty("username", findProperty("xander-api.username")?.toString())
            json.addProperty("password", findProperty("xander-api.password")?.toString())
            POST(BodyPublishers.ofString(gson.toJson(json)))
            header("Content-Type", "application/json")
        }.build()

        val loginResponse = client.send(loginRequest, BodyHandlers.ofString())
        if (loginResponse.statusCode() != 200) {
            println("FAILED TO LOGIN TO API.ISXANDER.DEV")
            return@registering
        }

        val loginResponseJson = gson.fromJson(loginResponse.body(), JsonObject::class.java)
        val jwtToken = loginResponseJson.get("token").asString

        val loaders = listOf("forge", "fabric")
        val minecraftVersion: String by rootProject
        for (loader in loaders) {
            val newVersionRequest = HttpRequest.newBuilder(URI.create("https://api.isxander.dev/updater/new/debugify?loader=$loader&minecraft=$minecraftVersion&version=${project.version}")).apply {
                GET()
                header("Authorization", "Bearer $jwtToken")
            }.build()

            val response = client.send(newVersionRequest, BodyHandlers.ofString())
        }
    }

    register("publish") {
        dependsOn("clean")
        dependsOn(publishToModrinth)
        dependsOn(publishToCurseforge)
        dependsOn(updateApiVersion)
    }
}

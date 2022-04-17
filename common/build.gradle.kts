plugins {
    id("net.kyori.blossom") version "1.+"
}

dependencies {
    val fabricLoaderVersion: String by rootProject
    val clothVersion: String by rootProject
    val mixinExtrasVersion: String by rootProject
    val jSemVerVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    implementation("com.github.zafarkhaja:java-semver:$jSemVerVersion")

    implementation("com.github.llamalad7:mixinextras:$mixinExtrasVersion")
    annotationProcessor("com.github.llamalad7:mixinextras:$mixinExtrasVersion")

    modImplementation("me.shedaniel.cloth:cloth-config:$clothVersion")
}

architectury {
    common()
}

loom {
    accessWidenerPath.set(file("src/main/resources/debugify.accesswidener"))
}

blossom {
    val debugifyClass = "src/main/java/cc/woverflow/debugify/Debugify.java"

    replaceToken("@VERSION@", project.version, debugifyClass)
}

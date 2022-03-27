plugins {
    id("net.kyori.blossom") version "1.+"
}

dependencies {
    val fabricLoaderVersion: String by rootProject
    val clothVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    implementation("com.github.zafarkhaja:java-semver:0.9.+")

    implementation("com.github.llamalad7:mixinextras:0.0.+")
    annotationProcessor("com.github.llamalad7:mixinextras:0.0.+")

    modImplementation("me.shedaniel.cloth:cloth-config:$clothVersion")
}

architectury {
    common()
}

blossom {
    val debugifyClass = "src/main/java/cc/woverflow/debugify/Debugify.java"

    replaceToken("@VERSION@", project.version, debugifyClass)
}

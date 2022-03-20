plugins {
    id("net.kyori.blossom") version "1.+"
}

dependencies {
    val fabricLoaderVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")

    implementation("com.github.zafarkhaja:java-semver:0.9.+")
}

architectury {
    common()
}

blossom {
    val debugifyClass = "src/main/java/cc/woverflow/debugify/Debugify.java"

    replaceToken("@VERSION@", project.version, debugifyClass)
}

dependencies {
    val fabricLoaderVersion: String by rootProject

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
}

architectury {
    common()
}

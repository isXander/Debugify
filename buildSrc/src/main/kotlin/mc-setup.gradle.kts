plugins {
    id("dev.architectury.loom")
    id("io.github.juuxel.loom-quiltflower")
    id("org.quiltmc.quilt-mappings-on-loom")
}

loom {
    silentMojangMappingsLicense()
}

/**
 * have to use buildSrc for this because
 * subprojects doesn't allow you to use loom or quiltMappings
 */
dependencies {
    val minecraftVersion: String by rootProject

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        officialMojangMappings()
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:$minecraftVersion+build.+:v2"))
    })
}

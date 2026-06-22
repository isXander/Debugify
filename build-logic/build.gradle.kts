plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	gradlePluginPortal()
}

dependencies {
	implementation("com.google.code.gson:gson:2.14.0")
}

gradlePlugin {
	plugins.register("bugs") {
		id = "dev.isxander.debugify.bugs"
		implementationClass = "dev.isxander.debugify.BugsPlugin"
	}
}

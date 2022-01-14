plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // Required for the "interaktions-sampled-module.gradle.kts"
    implementation(kotlin("gradle-plugin", version = "1.6.10")) // We can't use Versions.KOTLIN here because... well, we are in the buildSrc!
}
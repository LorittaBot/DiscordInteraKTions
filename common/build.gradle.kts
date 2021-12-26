plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.KOTLIN
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))

    api("dev.kord:kord-rest:${Versions.KORD}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        register("PerfectDreams", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib"))
    // Used for the *snowflakes*
    api("dev.kord:kord-core:0.7.0-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

publishing {
    publications {
        register("PerfectDreams", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}
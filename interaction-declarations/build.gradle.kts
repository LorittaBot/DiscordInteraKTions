import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "net.perfectdreams.discordinteraktions"
version = "0.0.1-SNAPSHOT"

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api("dev.kord:kord-rest:0.7.0-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

subprojects {
    apply<MavenPublishPlugin>()

    publishing {
        repositories {
            maven {
                name = "PerfectDreams"
                url = uri("https://repo.perfectdreams.net/")

                credentials {
                    username = System.getProperty("USERNAME") ?: System.getenv("USERNAME")
                    password = System.getProperty("PASSWORD") ?: System.getenv("PASSWORD")
                }
            }
        }
    }
}
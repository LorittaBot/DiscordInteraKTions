import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"
version = Versions.DISCORD_INTERAKTIONS

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.perfectdreams.net")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "15"
    }
}

subprojects {
    apply<MavenPublishPlugin>()
    version = Versions.DISCORD_INTERAKTIONS

    publishing {
        repositories {
            maven {
                name = "PerfectDreams"
                url = uri("https://repo.perfectdreams.net/")
                credentials(PasswordCredentials::class)
            }
        }
    }
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    api(project(":platforms:common-kord"))
    implementation("dev.kord:kord-rest:0.8.x-SNAPSHOT")
    implementation("dev.kord:kord-gateway:0.8.x-SNAPSHOT")
    implementation("io.ktor:ktor-server-netty:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        register("PerfectDreams", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}
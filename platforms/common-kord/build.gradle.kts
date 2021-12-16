plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.10"
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    implementation("dev.kord:kord-rest:0.8.x-SNAPSHOT")
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
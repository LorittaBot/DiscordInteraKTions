plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.kord:kord-common:${Versions.KORD}")
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
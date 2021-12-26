plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
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
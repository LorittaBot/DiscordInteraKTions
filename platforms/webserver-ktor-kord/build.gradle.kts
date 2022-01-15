plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":requests-verifier"))
    api(project(":common"))
    implementation("dev.kord:kord-rest:${Versions.KORD}")
    implementation("io.ktor:ktor-server-netty:${Versions.KTOR}")
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
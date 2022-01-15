plugins {
    `interaktions-sampled-module`
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    implementation("dev.kord:kord-rest:${Versions.KORD}")
    implementation("dev.kord:kord-gateway:${Versions.KORD}")
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
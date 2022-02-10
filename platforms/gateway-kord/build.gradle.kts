plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    implementation(libs.kord.rest)
    implementation(libs.kord.gateway)
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
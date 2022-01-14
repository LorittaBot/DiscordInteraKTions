plugins {
    `interaktions-sampled-module`
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    api(project(":platforms:common-kord"))
    implementation("dev.kord:kord-rest:${Versions.KORD}")
    implementation("dev.kord:kord-gateway:${Versions.KORD}")
    samplesImplementation("ch.qos.logback:logback-core:1.3.0-alpha12")
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
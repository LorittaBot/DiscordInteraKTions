plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.30"
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":requests-verifier"))
    api(project(":interaction-declarations"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    api("io.ktor:ktor-server-core:1.4.1")
    api("io.ktor:ktor-server-netty:1.4.1")
    api("io.ktor:ktor-client-core:1.4.1")
    implementation("io.ktor:ktor-client-cio:1.4.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.67")
    api("dev.kord:kord-rest:0.7.x-SNAPSHOT")

    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    api("io.github.microutils:kotlin-logging:2.0.3")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        register("PerfectDreams", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}
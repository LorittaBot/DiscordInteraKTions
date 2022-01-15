plugins {
    kotlin("jvm")
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":platforms:gateway-kord"))
    implementation(project(":platforms:webserver-ktor-kord"))
    implementation("dev.kord:kord-rest:${Versions.KORD}")
    implementation("dev.kord:kord-gateway:${Versions.KORD}")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha12")
}

tasks.test {
    useJUnitPlatform()
}
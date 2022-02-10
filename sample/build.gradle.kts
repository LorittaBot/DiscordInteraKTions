plugins {
    kotlin("jvm")
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":platforms:gateway-kord"))
    implementation(project(":platforms:webserver-ktor-kord"))
    implementation(libs.kord.rest)
    implementation(libs.kord.gateway)
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha12")
}

tasks.test {
    useJUnitPlatform()
}
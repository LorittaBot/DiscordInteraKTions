rootProject.name = "DiscordInteraKTions"

enableFeaturePreview("VERSION_CATALOGS")

include(":requests-verifier")
include(":common")
// include(":platforms:gateway-jda")
include(":platforms:gateway-kord")
include(":platforms:webserver-ktor-kord")
include(":sample")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("discordinteraktions", "0.0.16-SNAPSHOT")
            version("kotlin", "1.7.10")
            version("kord", "0.8.x-20220720.101712-222")
            alias("kord-common").to("dev.kord", "kord-common").versionRef("kord")
            alias("kord-rest").to("dev.kord", "kord-rest").versionRef("kord")
            alias("kord-gateway").to("dev.kord", "kord-gateway").versionRef("kord")
            alias("ktor-server-netty").to("io.ktor:ktor-server-netty:2.0.3")
        }
    }
}

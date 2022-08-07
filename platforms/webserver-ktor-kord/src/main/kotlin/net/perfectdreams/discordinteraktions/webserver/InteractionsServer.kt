package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.DiscordInteraKTions
import net.perfectdreams.discordinteraktions.common.commands.InteractionsManager

/**
 * Class represents an Rest Interactions Server, which'll connect
 * to the Discord API and wrap your requests.
 *
 * @param applicationId Your bot ID/Client ID (https://i.imgur.com/075OBWk.png)
 * @param publicKey The public key of your bot (https://i.imgur.com/xDZnJ5J.png)
 * @param token Your bot token (https://i.imgur.com/VXLOFte.png)
 * @param port HTTP server port to bind
 */
class InteractionsServer(
    val interaKTions: DiscordInteraKTions,
    val publicKey: String,
    val port: Int = 12212,
) {
    companion object {
        val json = Json {
            // If there're any unknown keys, we'll ignore them instead of throwing an exception.
            this.ignoreUnknownKeys = true
        }
        private val logger = KotlinLogging.logger {}
    }

    val interactionsManager = InteractionsManager()
    val interactionRequestHandler: InteractionRequestHandler = DefaultInteractionRequestHandler(interaKTions)

    /**
     * You can use this method to start the interactions server,
     * which will open an connection on the 12212 port with the **Netty** engine.
     */
    fun start() {
        val server = embeddedServer(Netty, port = port) {
            routing {
                get("/") {
                    call.respondText("Hello, Discord Interactions!")
                }

                installDiscordInteractions(
                    publicKey,
                    "/",
                    interactionRequestHandler
                )
            }
        }

        server.start(true)
    }
}

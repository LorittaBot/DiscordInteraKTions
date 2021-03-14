package net.perfectdreams.discordinteraktions

import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import kotlinx.serialization.json.*
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.commands.CommandManager

class InteractionsServer(
    val applicationId: Long,
    val publicKey: String,
    val token: String
) {
    companion object {
        val json = Json {
            this.ignoreUnknownKeys = true
        }
        private val logger = KotlinLogging.logger {}
    }

    val commandManager = CommandManager(this)
    val http = HttpClient {}
    val rest = RestClient(token)

    fun start() {
        val server = embeddedServer(Netty, port = 12212) {
            routing {
                get("/api/discord/interactions") {
                    call.respondText("Hello, Discord Interactions!")
                }

                installDiscordInteractions(
                    publicKey,
                    "/api/discord/interactions",
                    DefaultInteractionRequestHandler(this@InteractionsServer)
                )
            }
        }

        server.start(true)
    }
}
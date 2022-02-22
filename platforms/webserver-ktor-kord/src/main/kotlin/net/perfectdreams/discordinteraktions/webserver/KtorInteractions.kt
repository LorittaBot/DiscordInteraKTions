package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.verifier.InteractionRequestVerifier

/**
 * Installs the Discord Interactions in the [path]
 * post requests to our defined route.
 *
 * @param publicKey The public key of your bot (https://i.imgur.com/xDZnJ5J.png)
 * @param path The location that we'll listing the requests.
 * @param handler The class that we use to handle these requests.
 *
 * @see InteractionsServer
 */
fun Routing.installDiscordInteractions(
    publicKey: String,
    path: String,
    handler: InteractionRequestHandler,
) {
    val keyVerifier = InteractionRequestVerifier(publicKey)
    val logger = KotlinLogging.logger {}

    post(path) {
        val signature = call.request.header("X-Signature-Ed25519")!!
        val timestamp = call.request.header("X-Signature-Timestamp")!!

        logger.debug { "Signature Header: $signature" }
        logger.debug { "Timestamp: $timestamp" }
        val text = withContext(Dispatchers.IO) {
            call.receiveStream().bufferedReader(charset = Charsets.UTF_8).readText()
        }

        logger.debug { "Payload: $text" }

        val parse = Json.parseToJsonElement(text)
            .jsonObject

        val type = parse["type"]!!.jsonPrimitive.int

        logger.debug { "Type: $type" }

        logger.debug { "Checking Signature..." }

        val verified = keyVerifier.verifyKey(
            text,
            signature,
            timestamp
        )

        if (!verified) {
            call.respondText("", ContentType.Application.Json, HttpStatusCode.Unauthorized)
            return@post
        }

        // println("Our Signature: $output")
        logger.debug { parse }

        // Kord still has some fields missing (like "deaf") so we need to decode the DiscordInteraction object while ignoring missing fields
        when (type) {
            InteractionType.Ping.type -> handler.onPing(call)
            InteractionType.ApplicationCommand.type -> {
                val interaction = InteractionsServer.json.decodeFromString<DiscordInteraction>(text)
                handler.onCommand(call, interaction)
            }
            InteractionType.Component.type -> {
                val interaction = InteractionsServer.json.decodeFromString<DiscordInteraction>(text)
                handler.onComponent(call, interaction)
            }
            InteractionType.AutoComplete.type -> {
                val interaction = InteractionsServer.json.decodeFromString<DiscordInteraction>(text)
                handler.onAutocomplete(call, interaction)
            }
            InteractionType.ModalSubmit.type -> {
                val interaction = InteractionsServer.json.decodeFromString<DiscordInteraction>(text)
                handler.onModalSubmit(call, interaction)
            }
        }
    }
}

package net.perfectdreams.discordinteraktions

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.perfectdreams.discordinteraktions.entities.Interaction
import net.perfectdreams.discordinteraktions.verifier.InteractionRequestVerifier

/**
 * On this method we'll install the **POST** route that'll handle
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
    handler: InteractionRequestHandler
) {
    val keyVerifier = InteractionRequestVerifier(publicKey)

    post(path) {
        val signature = call.request.header("X-Signature-Ed25519")!!
        val timestamp = call.request.header("X-Signature-Timestamp")!!

        println("Signature Header: $signature")
        println("Timestamp: $timestamp")
        val text = withContext(Dispatchers.IO) {
            call.receiveStream().bufferedReader(charset = Charsets.UTF_8).readText()
        }

        println("Payload: $text")

        val parse = Json.parseToJsonElement(text)
            .jsonObject

        val type = parse["type"]!!.jsonPrimitive.int

        println("Type: $type")

        println("Checking Signature...")

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
        println(parse)

        // Kord still has some fields missing (like "deaf") so we need to decode ignoring missing fields
        val interaction = InteractionsServer.json.decodeFromString<Interaction>(text)

        println(interaction)
        println(interaction::class)

        handler.onRequest(call, interaction)
    }
}
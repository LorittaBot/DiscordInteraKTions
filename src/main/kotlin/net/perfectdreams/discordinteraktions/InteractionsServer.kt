package net.perfectdreams.discordinteraktions

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.commands.CommandManager
import net.perfectdreams.discordinteraktions.context.HttpRequestManager
import net.perfectdreams.discordinteraktions.context.InteractionCommandContext
import net.perfectdreams.discordinteraktions.context.WebServerRequestManager
import net.perfectdreams.discordinteraktions.entities.requests.ApplicationCommandRequest
import net.perfectdreams.discordinteraktions.entities.requests.InteractionRequest
import net.perfectdreams.discordinteraktions.entities.requests.PingRequest

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

    private val keyVerifier = InteractionsKeyVerifier(publicKey)
    val commandManager = CommandManager(this)
    val http = HttpClient {}

    fun start() {
        val server = embeddedServer(Netty, port = 12212) {
            routing {
                get("/api/discord/interactions") {
                    call.respondText("Hello, Discord Interactions!")
                }

                post("/api/discord/interactions") {
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

                    val request = json.decodeFromString<InteractionRequest>(text)

                    if (request is PingRequest) {
                        println("Responding PING")

                        call.respondText(
                            buildJsonObject {
                                put("type", 1)
                            }.toString(),
                            ContentType.Application.Json
                        )
                    } else if (request is ApplicationCommandRequest) {
                        val command = commandManager.commands.first { it.label == request.data.name }

                        val notificationChannel = Channel<Unit>(0)
                        val commandContext = InteractionCommandContext(
                            request,
                            WebServerRequestManager(call, request, notificationChannel)
                        )

                        launch {
                            command.executes(commandContext)
                            println("Finished execution!")
                        }

                        notificationChannel.receiveOrNull()
                        logger.info { "Switching Request Manager..." }
                        commandContext.manager = HttpRequestManager(this@InteractionsServer, request)

                        // Send a notification indicating that the code can continue
                        notificationChannel.send(Unit)
                    } else {
                        println("Unknown Type $type")
                    }
                }
            }
        }

        server.start(true)
    }
}
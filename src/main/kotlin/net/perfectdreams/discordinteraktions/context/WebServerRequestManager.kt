package net.perfectdreams.discordinteraktions.context

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.entities.OriginalMessage
import net.perfectdreams.discordinteraktions.entities.Message
import net.perfectdreams.discordinteraktions.entities.MessageToBeSent
import net.perfectdreams.discordinteraktions.entities.requests.ApplicationCommandRequest

class WebServerRequestManager(
    val call: ApplicationCall,
    val request: ApplicationCommandRequest,
    val notificationChannel: Channel<Unit>
) : RequestManager {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun defer() {
        logger.info { "Deferring interaction..." }
        call.respondText(
            buildJsonObject {
                put("type", 5)
            }.toString(),
            ContentType.Application.Json
        )
        notificationChannel.send(Unit)
        // Wait until the we receive a notification back, this is useful to wait until the manager is replaced
        notificationChannel.receive()
    }

    override suspend fun sendMessage(message: MessageToBeSent): Message {
        call.respondText(
            buildJsonObject {
                put("type", 4)

                putJsonObject("data") {
                    put("tts", message.tts)
                    put("content", message.content)
                    put("flags", message.flags)
                    putJsonArray("embeds") {}
                    putJsonArray("allowed_mentions") {}
                }
            }.toString(),
            ContentType.Application.Json
        )

        notificationChannel.send(Unit)
        // Wait until the we receive a notification back, this is useful to wait until the manager is replaced
        notificationChannel.receive()

        return OriginalMessage(message.content)
    }
}
package net.perfectdreams.discordinteraktions.context

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.internal.entities.InitialResponseMessage
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param call The request data
 * @param notificationChannel The notification pipe that we use for notifying events
 */
class WebServerRequestManager(
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val call: ApplicationCall,
    val notificationChannel: Channel<Unit>
) : RequestManager {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun defer() {
        // How this works? https://discord.com/developers/docs/interactions/slash-commands#interaction-response
        logger.info { "Deferring interaction..." }
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredChannelMessageWithSource.type)
            }.toString(),
            ContentType.Application.Json
        )
        notificationChannel.send(Unit)
        // Wait until the we receive a notification back, this is useful to wait until the manager is replaced
        notificationChannel.receive()
    }

    override suspend fun sendMessage(message: InteractionMessage): Message {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.ChannelMessageWithSource.type)
                put("data", Json.encodeToJsonElement(message))

                /* putJsonObject("data") {
                    // put("tts", message.tts)
                    put("content", message.content)
                    // put("flags", message.flags)
                    // putJsonArray("embeds") {}
                    // putJsonArray("allowed_mentions") {}
                } */
            }.toString(),
            ContentType.Application.Json
        )

        notificationChannel.send(Unit)
        // Wait until the we receive a notification back, this is useful to wait until the manager is replaced
        notificationChannel.receive()

        return InitialResponseMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
        // return OriginalMessage(message.content)
    }
}
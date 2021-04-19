package net.perfectdreams.discordinteraktions.context.manager

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
import net.perfectdreams.discordinteraktions.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.context.RequestBridge
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.utils.Observable

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
    bridge: RequestBridge,
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val request: CommandInteraction,
    val call: ApplicationCall
) : RequestManager(bridge) {
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

        bridge.state.value = InteractionRequestState.DEFERRED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
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

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return InitialResponseMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }
}
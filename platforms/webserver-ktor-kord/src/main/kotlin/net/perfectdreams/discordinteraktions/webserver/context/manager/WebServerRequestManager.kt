package net.perfectdreams.discordinteraktions.webserver.context.manager

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.HttpRequestManager

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
    val request: DiscordInteraction,
    val call: ApplicationCall
) : RequestManager(bridge) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun deferReply(isEphemeral: Boolean) {
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

    override suspend fun deferEdit(message: InteractionMessage?) = TODO("Not yet implemented")

    override suspend fun sendMessage(message: InteractionMessage): Message {
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.ChannelMessageWithSource.type)
                // put("data", Json.encodeToJsonElement(message))

                putJsonObject("data") {
                    // put("tts", message.tts)
                    put("content", message.content)
                    // put("flags", message.flags)
                    // putJsonArray("embeds") {}
                    // putJsonArray("allowed_mentions") {}
                }
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

        return DummyMessage() // TODO: Fix
        /* return DummyMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        ) */
    }
}
package net.perfectdreams.discordinteraktions.webserver.context.manager

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.coerceToMissing
import dev.kord.common.entity.optional.map
import dev.kord.common.entity.optional.mapList
import dev.kord.common.entity.optional.optional
import dev.kord.common.entity.optional.toPrimitive
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.HttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordAllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordEmbedBuilder

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

    override suspend fun deferMessage(isEphemeral: Boolean) {
        // How this works? https://discord.com/developers/docs/interactions/slash-commands#interaction-response
        logger.info { "Deferring interaction..." }

        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.DeferredChannelMessageWithSource.type)

                if (isEphemeral)
                    putJsonObject("data") {
                        put("flags", 64)
                    }
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
            Json.encodeToString(
                InteractionResponseCreateRequest(
                    type = InteractionResponseType.ChannelMessageWithSource,
                    data = Optional(
                        InteractionApplicationCommandCallbackData(
                            content = Optional(message.content).coerceToMissing(),
                            tts = Optional(message.tts).coerceToMissing().toPrimitive(),
                            embeds = Optional(message.embeds?.map { it.toKordEmbedBuilder().toRequest() } ?: listOf()),
                            allowedMentions = Optional(message.allowedMentions?.toKordAllowedMentions()).coerceToMissing().map { it.build() },
                            components = Optional(null).coerceToMissing(),
                            flags = MessageFlags {
                                if (message.isEphemeral)
                                    + MessageFlag.Ephemeral
                            }.optional()
                        )
                    )
                )
            ),
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
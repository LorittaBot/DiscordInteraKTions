package net.perfectdreams.discordinteraktions.platforms.kord.context.manager

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.optional
import dev.kord.rest.builder.message.create.EphemeralInteractionResponseCreateBuilder
import dev.kord.rest.builder.message.create.PublicInteractionResponseCreateBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordOriginalInteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordAllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordEmbedBuilder

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param request The interaction (wrapped by the [InteractionRequestHandler]
 */
@OptIn(KordPreview::class)
class InitialHttpRequestManager(
    bridge: RequestBridge,
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val request: DiscordInteraction
) : RequestManager(bridge) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    init {
        require(bridge.state.value == InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager should be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferMessage(isEphemeral: Boolean) {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredChannelMessageWithSource,
                InteractionApplicationCommandCallbackData(
                    flags = MessageFlags {
                        if (isEphemeral)
                            + MessageFlag.Ephemeral
                    }.optional()
                ).optional()
            )
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
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            if (message.isEphemeral) {
                // Ephemeral does not support file upload
                EphemeralInteractionResponseCreateBuilder().apply {
                    this.content = message.content
                    this.tts = message.tts
                    this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                    message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.forEach {
                        this.embeds.add(it)
                    }
                }.toRequest()
            } else {
                PublicInteractionResponseCreateBuilder().apply {
                    this.content = message.content
                    this.tts = message.tts
                    this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                    message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.forEach {
                        this.embeds.add(it)
                    }
                }.toRequest()
            }
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }
}
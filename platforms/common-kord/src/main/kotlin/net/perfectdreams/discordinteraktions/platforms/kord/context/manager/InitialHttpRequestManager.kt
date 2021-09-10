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
import dev.kord.rest.builder.message.modify.EphemeralInteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionPublicMessage

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

    override suspend fun deferChannelMessage() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredChannelMessageWithSource,
                InteractionApplicationCommandCallbackData().optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
    }

    override suspend fun deferChannelMessageEphemerally() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredChannelMessageWithSource,
                InteractionApplicationCommandCallbackData(
                    flags = MessageFlags {
                        + MessageFlag.Ephemeral
                    }.optional()
                ).optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
    }

    override suspend fun sendPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): PublicMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createInteractionResponse(
            applicationId,
            request.token,
            PublicInteractionResponseCreateBuilder().apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
                this.files.addAll(message.files)
            }.toRequest()
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }

    override suspend fun sendEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EphemeralMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createInteractionResponse(
            applicationId,
            request.token,
            EphemeralInteractionResponseCreateBuilder().apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
            }.toRequest()
        )

        return KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }

    override suspend fun deferUpdateMessage() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredUpdateMessage,
                InteractionApplicationCommandCallbackData().optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_UPDATE_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
    }

    override suspend fun updateMessage(message: PublicInteractionResponseModifyBuilder): Message {
        rest.interaction.modifyInteractionResponse(
            request.id,
            interactionToken,
            message.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }

    override suspend fun updateEphemeralMessage(message: EphemeralInteractionResponseModifyBuilder): Message {
        rest.interaction.modifyInteractionResponse(
            request.id,
            interactionToken,
            message.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }
}
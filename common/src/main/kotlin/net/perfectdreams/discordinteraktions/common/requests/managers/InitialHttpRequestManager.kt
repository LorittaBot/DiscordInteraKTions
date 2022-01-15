package net.perfectdreams.discordinteraktions.common.requests.managers

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Choice
import dev.kord.common.entity.DiscordAutoComplete
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.coerceToMissing
import dev.kord.common.entity.optional.optional
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionPublicMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application ID
 * @param interactionToken The request's token
 * @param request The Discord Interaction request
 */
class InitialHttpRequestManager(
    bridge: RequestBridge,
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionId: Snowflake,
    val interactionToken: String
) : RequestManager(bridge) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    init {
        require(bridge.state.value == InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager should be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage() {
        rest.interaction.createInteractionResponse(
            interactionId,
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
            interactionToken
        )
    }

    override suspend fun deferChannelMessageEphemerally() {
        rest.interaction.createInteractionResponse(
            interactionId,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredChannelMessageWithSource,
                InteractionApplicationCommandCallbackData(
                    flags = MessageFlags {
                        +MessageFlag.Ephemeral
                    }.optional()
                ).optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        rest.interaction.createInteractionResponse(
            interactionId,
            interactionToken,
            message.toInteractionMessageResponseCreateBuilder().toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        rest.interaction.createInteractionResponse(
            interactionId,
            interactionToken,
            message.toInteractionMessageResponseCreateBuilder().toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun deferUpdateMessage() {
        rest.interaction.createInteractionResponse(
            interactionId,
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
            interactionToken
        )
    }

    override suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        rest.interaction.createInteractionResponse(
            interactionId,
            interactionToken,
            InteractionResponseCreateRequest(
                type = InteractionResponseType.UpdateMessage,
                data = Optional(
                    InteractionApplicationCommandCallbackData(
                        content = Optional(message.content).coerceToMissing(),
                        embeds = Optional(message.embeds?.map { it.toRequest() } ?: listOf()),
                        allowedMentions = Optional(message.allowedMentions?.build()).coerceToMissing(),
                        components = message.components?.map { it.build() }.optional().coerceToMissing(),
                        flags = MessageFlags {}.optional()
                    )
                )
            )
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken
        )
    }

    override suspend fun sendStringAutocomplete(list: List<Choice<String>>) {
        rest.interaction.createAutoCompleteInteractionResponse(
            interactionId,
            interactionToken,
            DiscordAutoComplete(list)
        )
    }

    override suspend fun sendIntegerAutocomplete(list: List<Choice<Long>>) {
        rest.interaction.createAutoCompleteInteractionResponse(
            interactionId,
            interactionToken,
            DiscordAutoComplete(list)
        )
    }

    override suspend fun sendNumberAutocomplete(list: List<Choice<Double>>) {
        rest.interaction.createAutoCompleteInteractionResponse(
            interactionId,
            interactionToken,
            DiscordAutoComplete(list)
        )
    }
}
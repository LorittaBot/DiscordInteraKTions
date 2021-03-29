package net.perfectdreams.discordinteraktions.context

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.rest.builder.interaction.FollowupMessageCreateBuilder
import dev.kord.rest.builder.interaction.InteractionApplicationCommandCallbackDataBuilder
import dev.kord.rest.builder.interaction.InteractionResponseModifyBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.InteractionRequestHandler
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.internal.entities.KordMessage
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.internal.entities.InitialResponseMessage
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param request The interaction (wrapped by the [InteractionRequestHandler]
 */
class InitialHttpRequestManager(
    bridge: RequestBridge,
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val request: CommandInteraction
) : RequestManager(bridge) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun defer() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(InteractionResponseType.DeferredChannelMessageWithSource)
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
            InteractionResponseCreateRequest(
                InteractionResponseType.ChannelMessageWithSource,
                Optional(
                    InteractionApplicationCommandCallbackDataBuilder().apply {
                        this.content = message.content
                        this.tts = message.tts
                        this.flags = message.flags
                        // this.allowedMentions = message.allowedMentions
                    }.build()
                )
            )
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return InitialResponseMessage(rest, applicationId, interactionToken, message.content)
    }
}
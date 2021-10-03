package net.perfectdreams.discordinteraktions.platforms.kord.context.manager

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableEphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EditablePersistentMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordEphemeralFollowupMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordOriginalInteractionPublicMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicFollowupMessage

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
class HttpRequestManager(
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
        require(bridge.state.value != InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager shouldn't be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage() = error("Can't defer a interaction that was already deferred!")

    override suspend fun deferChannelMessageEphemerally() =  error("Can't defer a interaction that was already deferred!")

    override suspend fun sendPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): EditablePersistentMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createFollowupMessage(
            applicationId,
            request.token,
            FollowupMessageCreateBuilder(false).apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
                this.files.addAll(message.files)
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordPublicFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            kordMessage
        )
    }

    override suspend fun sendEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EditableEphemeralMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createFollowupMessage(
            applicationId,
            request.token,
            FollowupMessageCreateBuilder(true).apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordEphemeralFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            kordMessage
        )
    }

    override suspend fun deferUpdateMessage() = error("Can't defer a interaction that was already deferred!")

    override suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): EditablePersistentMessage {
        val interactionMessage = KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            null
        )

        val newMessage = interactionMessage.editMessage(message)

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return newMessage
    }

    override suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): EditableEphemeralMessage {
        val interactionMessage = KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            null
        )

        val newMessage = interactionMessage.editMessage(message)

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return newMessage
    }
}
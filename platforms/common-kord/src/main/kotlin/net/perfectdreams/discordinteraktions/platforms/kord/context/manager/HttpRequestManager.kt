package net.perfectdreams.discordinteraktions.platforms.kord.context.manager

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.EphemeralFollowupMessageCreateBuilder
import dev.kord.rest.builder.message.create.PublicFollowupMessageCreateBuilder
import dev.kord.rest.builder.message.modify.EphemeralInteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
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
        if (bridge.state.value == InteractionRequestState.NOT_REPLIED_YET)
            throw IllegalStateException("HttpRequestManager shouldn't be in the NOT_REPLIED_YET state!")
    }

    override suspend fun deferReply(isEphemeral: Boolean) = throw RuntimeException("Can't defer a interaction that was already deferred!")

    override suspend fun deferEdit(message: InteractionMessage?) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: InteractionMessage): Message {
        if (bridge.state.value == InteractionRequestState.DEFERRED) {
            // If it was deferred, we are going to edit the original message
            val kordMessage = rest.interaction.modifyInteractionResponse(
                applicationId,
                request.token,
                if (message.isEphemeral) {
                    EphemeralInteractionResponseModifyBuilder().apply {
                        // You can't modify a message to change its tts status, so it is ignored
                        this.content = message.content
                        this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()
                        this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                        // There are "username" and "avatar" flags, but they seem to be unused for slash commands
                        // TODO: Also, what to do about message flags? Silently ignore them or throw a exception?
                    }.toRequest()
                } else {
                    PublicInteractionResponseModifyBuilder().apply {
                        // You can't modify a message to change its tts status, so it is ignored
                        this.content = message.content
                        this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()

                        val filePairs = message.files?.map { it.key to it.value }
                        filePairs?.forEach {
                            addFile(it.first, it.second)
                        }

                        this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                        // There are "username" and "avatar" flags, but they seem to be unused for slash commands
                        // TODO: Also, what to do about message flags? Silently ignore them or throw a exception?
                    }.toRequest()
                }
            )

            // And also change the state to "ALREADY_REPLIED"
            bridge.state.value = InteractionRequestState.ALREADY_REPLIED

            return DummyMessage()
            // return KordMessage(rest, applicationId, interactionToken, kordMessage)
        } else {
            // *Technically* we can respond to the initial interaction via HTTP too
            val kordMessage = rest.interaction.createFollowupMessage(
                applicationId,
                request.token,
                if (message.isEphemeral) {
                    EphemeralFollowupMessageCreateBuilder().apply {
                        this.content = message.content
                        this.tts = message.tts
                        this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                        message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.forEach {
                            this.embeds.add(it)
                        }
                        // There are "username" and "avatar" flags, but they seem to be unused for slash commands
                        // TODO: Also, what to do about message flags? Silently ignore them or throw a exception?
                    }.toRequest()
                } else {
                    PublicFollowupMessageCreateBuilder().apply {
                        this.content = message.content
                        this.tts = message.tts
                        this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                        message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.forEach {
                            this.embeds.add(it)
                        }

                        val filePairs = message.files?.map { it.key to it.value }
                        if (filePairs != null)
                            files.addAll(filePairs)

                        // There are "username" and "avatar" flags, but they seem to be unused for slash commands
                        // TODO: Also, what to do about message flags? Silently ignore them or throw a exception?
                    }.toRequest()
                }
            )

            return DummyMessage()
            // return KordMessage(rest, applicationId, interactionToken, kordMessage)
        }
    }
}
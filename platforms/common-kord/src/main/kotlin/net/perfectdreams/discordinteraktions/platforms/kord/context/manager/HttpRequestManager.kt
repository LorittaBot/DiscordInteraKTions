package net.perfectdreams.discordinteraktions.platforms.kord.context.manager

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.EphemeralFollowupMessageCreateBuilder
import dev.kord.rest.builder.message.create.PublicFollowupMessageCreateBuilder
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordEditedOriginalInteractionPublicMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordActionRowBuilder
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
        require(bridge.state.value != InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager shouldn't be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage() = error("Can't defer a interaction that was already deferred!")

    override suspend fun deferChannelMessageEphemerally() =  error("Can't defer a interaction that was already deferred!")

    override suspend fun sendMessage(message: InteractionMessage): Message {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createFollowupMessage(
            applicationId,
            request.token,
            if (message.isEphemeral) {
                EphemeralFollowupMessageCreateBuilder().apply {
                    this.content = message.content
                    this.tts = message.tts
                    this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()

                    message.components?.let { it.map { it.toKordActionRowBuilder() } }?.forEach {
                        this.components.add(it)
                    }

                    message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.forEach {
                        this.embeds.add(it)
                    }
                    // There are "username" and "avatar" flags, but they seem to be unused for application commands
                    // TODO: Also, what to do about message flags? Silently ignore them or throw a exception?
                }.toRequest()
            } else {
                PublicFollowupMessageCreateBuilder().apply {
                    this.content = message.content
                    this.tts = message.tts
                    this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()

                    message.components?.let { it.map { it.toKordActionRowBuilder() } }?.forEach {
                        this.components.add(it)
                    }

                    message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.forEach {
                        this.embeds.add(it)
                    }

                    val filePairs = message.files?.map { it.key to it.value }
                    if (filePairs != null)
                        files.addAll(filePairs)

                    // There are "username" and "avatar" flags, but they seem to be unused for application commands
                    // TODO: Also, what to do about message flags? Silently ignore them or throw a exception?
                }.toRequest()
            }
        )

        return if (message.isEphemeral)
            KordEphemeralMessage(kordMessage)
        else
            KordPublicMessage(kordMessage)
    }

    override suspend fun deferUpdateMessage() {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessage(message: InteractionMessage, isEphemeral: Boolean): Message {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            PublicInteractionResponseModifyBuilder().apply {
                this.content = message.content
                this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()

                if (message.removeAlreadyUploadedFiles) {
                    println("Removing already uploaded files...")
                    this.files = mutableListOf()
                }

                this.files = message.files?.map { it.key to it.value }?.toMutableList()
            }.toRequest()
        )

        return KordEditedOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
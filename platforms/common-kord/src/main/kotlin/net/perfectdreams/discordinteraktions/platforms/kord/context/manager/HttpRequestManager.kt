package net.perfectdreams.discordinteraktions.platforms.kord.context.manager

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.PublicFollowupMessageCreateBuilder
import dev.kord.rest.builder.interaction.PublicInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param request The interaction (wrapped by the [InteractionRequestHandler]
 */
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
                PublicInteractionResponseModifyBuilder().apply {
                    this.content = message.content
                    // this.embeds = listOfNotNull(message.abstractEmbed?.intoBuilder()).toMutableList()
                    val filePairs = message.files?.map { it.key to it.value }
                    if (filePairs != null)
                        files.addAll(filePairs)
                    // this.tts = message.tts
                    // this.allowedMentions = message.allowedMentions

                    // There are "username" and "avatar" flags, but they seem to be unused
                    // Also, what to do about message flags? Silently ignore them or throw a exception?
                }.toRequest()
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
                PublicFollowupMessageCreateBuilder().apply {
                    this.content = message.content
                    this.tts = message.tts
                    // this.allowedMentions = message.allowedMentions
                    // this.embeds = listOfNotNull(message.abstractEmbed?.intoBuilder()).map { it.toRequest() }.toMutableList()

                    val filePairs = message.files?.map { it.key to it.value }
                    if (filePairs != null)
                        files.addAll(filePairs)

                    // There are "username" and "avatar" flags, but they seem to be unused
                    // Also, what to do about message flags? Silently ignore them or throw a exception?
                }.toRequest()
            )

            return DummyMessage()
            // return KordMessage(rest, applicationId, interactionToken, kordMessage)
        }
    }
}
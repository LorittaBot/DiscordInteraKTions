package net.perfectdreams.discordinteraktions.context

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.FollowupMessageCreateBuilder
import dev.kord.rest.service.RestClient
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.InteractionRequestHandler
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.internal.entities.KordMessage
import net.perfectdreams.discordinteraktions.api.entities.Message
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
class HttpRequestManager(
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val request: CommandInteraction
) : RequestManager {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun defer() = throw RuntimeException("Can't defer a interaction that already had a message sent!")

    override suspend fun sendMessage(message: InteractionMessage): Message {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createFollowupMessage(
            applicationId,
            request.token,
            FollowupMessageCreateBuilder().apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions

                // There are "username" and "avatar" flags, but they seem to be unused
                // Also, what to do about message flags? Silently ignore them or throw a exception?
            }.toRequest()
        )

        return KordMessage(rest, applicationId, interactionToken, kordMessage)
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicThinkingMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordAllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordEmbedBuilder

class KordPublicThinkingMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val bridge: RequestBridge
) : PublicThinkingMessage {
    override suspend fun editMessage(block: MessageBuilder.() -> Unit): PublicMessage {
        val message = buildMessage(block)
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            PublicInteractionResponseModifyBuilder().apply {
                this.content = message.content
                this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()

                val filePairs = message.files?.map { it.key to it.value }

                if (filePairs != null)
                    this.files = filePairs.toMutableList()
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordEditedOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
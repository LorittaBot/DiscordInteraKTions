package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralThinkingMessage
import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordAllowedMentions
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordEmbedBuilder

class KordEphemeralThinkingMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val bridge: RequestBridge
) : EphemeralThinkingMessage {
    override suspend fun editMessage(block: EphemeralMessageBuilder.() -> Unit): EphemeralMessage {
        val message = buildEphemeralMessage(block)
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            PublicInteractionResponseModifyBuilder().apply {
                this.content = message.content
                this.allowedMentions = message.allowedMentions?.toKordAllowedMentions()
                this.embeds = message.embeds?.let { it.map { it.toKordEmbedBuilder() } }?.toMutableList()

                val filePairs = message.files?.map { it.key to it.value }
                if (filePairs != null)
                    files?.addAll(filePairs)
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordEditedOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
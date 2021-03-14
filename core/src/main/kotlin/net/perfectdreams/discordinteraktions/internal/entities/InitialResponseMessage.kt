package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.InteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

class InitialResponseMessage(
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    override val content: String
) : Message {
    override suspend fun editMessage(message: InteractionMessage): Message {
        val kordMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            InteractionResponseModifyBuilder().apply {
                this.content = message.content
                // TODO: Fix this and add embeds
                // this.allowedMentions = message.allowedMentions
            }.toRequest()
        )

        return KordMessage(
            rest,
            applicationId,
            interactionToken,
            kordMessage
        )
    }
}
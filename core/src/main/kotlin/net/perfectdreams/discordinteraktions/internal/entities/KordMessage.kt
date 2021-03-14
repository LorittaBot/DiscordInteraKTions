package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.FollowupMessageModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

class KordMessage(
    val rest: RestClient,
    val applicationId: Snowflake,
    val interactionToken: String,
    val message: DiscordMessage
) : Message {
    override val content: String
        get() = message.content

    override suspend fun editMessage(message: InteractionMessage): Message {
        val result = rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            this.message.id,
            FollowupMessageModifyBuilder().apply {
                this.content = message.content
            }.toRequest()
        )

        return KordMessage(
            rest,
            applicationId,
            interactionToken,
            result
        )
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage

class KordPublicFollowupMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    handle: DiscordMessage
) : KordPublicMessage(handle), EditableMessage {
    override val id = handle.id
    override val content by handle::content

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            data.id,
            message.toFollowupMessageModifyBuilder().toRequest()
        )

        return KordPublicFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
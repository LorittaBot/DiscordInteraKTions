package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage

class KordPublicFollowupMessage(
    kord: Kord,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    handle: DiscordMessage
) : KordPublicMessage(kord, handle), EditableMessage {
    override val id = handle.id
    override val content by handle::content

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = kord.rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            data.id,
            message.toFollowupMessageModifyBuilder().toRequest()
        )

        return KordPublicFollowupMessage(
            kord,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
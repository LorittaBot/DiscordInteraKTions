package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage

open class KordEphemeralFollowupMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    val handle: DiscordMessage
) : EphemeralMessage, EditableMessage {
    override val id = handle.id
    override val content by handle::content

    override suspend fun editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> Unit): EditableMessage = editMessage(InteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            handle.id,
            convertToFollowupMessageModifyBuilder(message).toRequest()
        )

        return KordEphemeralFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
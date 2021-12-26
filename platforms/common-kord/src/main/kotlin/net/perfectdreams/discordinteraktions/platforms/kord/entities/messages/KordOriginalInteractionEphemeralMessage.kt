package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage

class KordOriginalInteractionEphemeralMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    override val content: String?
) : EphemeralMessage, EditableMessage {
    override val id: Snowflake
        get() = error("Original Interaction Messages do not have an ID!")

    override suspend fun editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> Unit): EditableMessage = editMessage(InteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            convertToInteractionResponseModifyBuilder(message).toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.utils.suspendableApply

class KordOriginalInteractionEphemeralMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String
) : EphemeralMessage, EditableMessage, OriginalInteractionMessage() {
    override suspend fun editMessage(block: suspend InteractionOrFollowupMessageModifyBuilder.() -> Unit): EditableMessage = editMessage(InteractionOrFollowupMessageModifyBuilder().suspendableApply(block))

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            message.toInteractionMessageResponseModifyBuilder().toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
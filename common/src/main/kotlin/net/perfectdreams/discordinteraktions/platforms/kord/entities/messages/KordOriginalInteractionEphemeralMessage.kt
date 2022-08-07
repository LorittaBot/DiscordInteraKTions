package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage

class KordOriginalInteractionEphemeralMessage(
    private val kord: Kord,
    private val applicationId: Snowflake,
    private val interactionToken: String
) : EphemeralMessage, EditableMessage, OriginalInteractionMessage() {
    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = kord.rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            message.toInteractionMessageResponseModifyBuilder().toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            kord,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
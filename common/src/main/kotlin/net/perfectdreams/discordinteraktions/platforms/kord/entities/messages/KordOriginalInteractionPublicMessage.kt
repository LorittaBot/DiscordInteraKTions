package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage

class KordOriginalInteractionPublicMessage(
    private val kord: Kord,
    private val applicationId: Snowflake,
    private val interactionToken: String
) : PublicMessage, EditableMessage, OriginalInteractionMessage() {
    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = kord.rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            message.toInteractionMessageResponseModifyBuilder().toRequest()
        )

        return KordEditedOriginalInteractionPublicMessage(
            kord,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
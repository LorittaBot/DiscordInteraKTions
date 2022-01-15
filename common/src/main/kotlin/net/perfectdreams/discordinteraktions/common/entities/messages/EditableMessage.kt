package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder

interface EditableMessage {
    suspend fun editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EditableMessage
    suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage
}
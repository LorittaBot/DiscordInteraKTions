package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionOrFollowupMessageModifyBuilder

interface PublicMessage : Message {
    suspend fun editMessage(block: PublicInteractionOrFollowupMessageModifyBuilder.() -> (Unit)): PublicMessage
}